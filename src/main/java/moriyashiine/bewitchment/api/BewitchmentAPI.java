/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api;

import com.mojang.authlib.GameProfile;
import moriyashiine.bewitchment.api.component.PledgeComponent;
import moriyashiine.bewitchment.api.component.TransformationComponent;
import moriyashiine.bewitchment.api.item.PoppetItem;
import moriyashiine.bewitchment.api.misc.PoppetData;
import moriyashiine.bewitchment.api.registry.AltarMapEntry;
import moriyashiine.bewitchment.common.BWConfig;
import moriyashiine.bewitchment.common.block.entity.PoppetShelfBlockEntity;
import moriyashiine.bewitchment.common.entity.living.VampireEntity;
import moriyashiine.bewitchment.common.entity.living.WerewolfEntity;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.entity.projectile.SilverArrowEntity;
import moriyashiine.bewitchment.common.item.AthameItem;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.registry.*;
import moriyashiine.bewitchment.common.world.BWUniversalWorldState;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import java.util.*;

@SuppressWarnings("ConstantConditions")
public class BewitchmentAPI {
	public static final Set<AltarMapEntry> ALTAR_MAP_ENTRIES = new HashSet<>();

	@SuppressWarnings("InstantiationOfUtilityClass")
	public static final MobType DEMON = new MobType();

	public static ServerPlayer fakePlayer = null;

	public static LivingEntity getTaglockOwner(Level world, ItemStack taglock) {
		if (world instanceof ServerLevel && (taglock.getItem() instanceof TaglockItem || taglock.getItem() instanceof PoppetItem) && TaglockItem.hasTaglock(taglock)) {
			UUID ownerUUID = TaglockItem.getTaglockUUID(taglock);
			for (ServerLevel serverWorld : world.getServer().getAllLevels()) {
				if (serverWorld.getEntity(ownerUUID) instanceof LivingEntity livingEntity && livingEntity.isAlive()) {
					return livingEntity;
				}
			}
		}
		return null;
	}

	public static PoppetData getPoppetFromInventory(Level world, PoppetItem item, Entity owner, List<ItemStack> inventory) {
		if (inventory == null) {
			return PoppetData.EMPTY;
		}
		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.get(i);
			if (BWConfig.disabledPoppets.contains(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString())) {
				continue;
			}
			if (stack.getItem() == item && TaglockItem.hasTaglock(stack)) {
				UUID uuid = null;
				if (owner != null) {
					uuid = owner.getUUID();
				} else {
					LivingEntity taglockOwner = getTaglockOwner(world, stack);
					if (taglockOwner != null) {
						uuid = taglockOwner.getUUID();
					}
				}
				if (TaglockItem.getTaglockUUID(stack).equals(uuid)) {
					return new PoppetData(stack, null, i);
				}
			}
		}
		return PoppetData.EMPTY;
	}

	public static PoppetData getPoppet(Level world, PoppetItem item, Entity owner) {
		if (world.isClientSide) {
			return PoppetData.EMPTY;
		}
		ServerLevel serverWorld = (ServerLevel) world;
		if (item.worksInShelf) {
			for (ServerLevel foundWorld : serverWorld.getServer().getAllLevels()) {
				for (Map.Entry<Long, NonNullList<ItemStack>> entry : BWWorldState.get(foundWorld).poppetShelves.entrySet()) {
					PoppetData result = getPoppetFromInventory(foundWorld, item, owner, entry.getValue());
					if (result != PoppetData.EMPTY) {
						BlockPos pos = BlockPos.of(entry.getKey());
						if (foundWorld.hasChunkAt(pos) && foundWorld.getBlockEntity(pos) instanceof PoppetShelfBlockEntity poppetShelf) {
							poppetShelf.sync();
						}
						return new PoppetData(result.stack(), entry.getKey(), result.index());
					}
				}
			}
		}
		for (Player player : serverWorld.getServer().getPlayerList().getPlayers()) {
			List<ItemStack> stacks = new ArrayList<>(player.getInventory().items);
			stacks.add(player.getOffhandItem());
			PoppetData result = getPoppetFromInventory(player.level(), item, owner, stacks);
			if (result != PoppetData.EMPTY) {
				return result;
			}
		}
		return PoppetData.EMPTY;
	}

	public static ServerPlayer getFakePlayer(Level world) {
		if (!world.isClientSide) {
			if (fakePlayer == null) {
				fakePlayer = new ServerPlayer(world.getServer(), (ServerLevel) world, new GameProfile(UUID.randomUUID(), "FAKE_PLAYER"));
				fakePlayer.connection = new ServerGamePacketListenerImpl(world.getServer(), new Connection(PacketFlow.SERVERBOUND), fakePlayer);
				ItemStack axe = new ItemStack(Items.WOODEN_AXE);
				axe.getOrCreateTag().putBoolean("Unbreakable", true);
				fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, axe);
			}
			return fakePlayer;
		}
		return null;
	}

	public static LivingEntity getTransformedPlayerEntity(Player player) {
		if (BewitchmentAPI.isVampire(player, false)) {
			Bat entity = EntityType.BAT.create(player.level());
			entity.setResting(false);
			return entity;
		} else if (BewitchmentAPI.isWerewolf(player, false)) {
			WerewolfEntity entity = BWEntityTypes.WEREWOLF.create(player.level());
			entity.getEntityData().set(BWHostileEntity.VARIANT, BWComponents.ADDITIONAL_WEREWOLF_DATA_COMPONENT.get(player).getVariant());
			return entity;
		}
		return null;
	}

	public static EntityType<?> getFamiliar(Player player) {
		if (!player.level().isClientSide) {
			BWUniversalWorldState universalWorldState = BWUniversalWorldState.get(player.level());
			for (Tuple<UUID, CompoundTag> pair : universalWorldState.familiars) {
				if (player.getUUID().equals(pair.getA())) {
					return BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(pair.getB().getString("id")));
				}
			}
		}
		return null;
	}

	public static boolean fillMagic(Player player, int amount, boolean simulate) {
		if (player.level().isClientSide) {
			return false;
		}
		return BWComponents.MAGIC_COMPONENT.get(player).fillMagic(amount, simulate);
	}

	public static boolean drainMagic(Player player, int amount, boolean simulate) {
		if (player.level().isClientSide) {
			return false;
		}
		if (player.isCreative()) {
			return true;
		}
		if (player.hasEffect(BWStatusEffects.INHIBITED)) {
			return false;
		}
		return BWComponents.MAGIC_COMPONENT.get(player).drainMagic(amount, simulate);
	}

	public static boolean isVampire(Entity entity, boolean includeHumanForm) {
		if (entity instanceof Player player) {
			TransformationComponent transformationComponent = BWComponents.TRANSFORMATION_COMPONENT.get(player);
			if (transformationComponent.getTransformation() == BWTransformations.VAMPIRE) {
				return includeHumanForm || transformationComponent.isAlternateForm();
			}
		}
		return entity instanceof VampireEntity;
	}

	public static boolean isWerewolf(Entity entity, boolean includeHumanForm) {
		if (entity instanceof Player player) {
			TransformationComponent transformationComponent = BWComponents.TRANSFORMATION_COMPONENT.get(player);
			if (transformationComponent.getTransformation() == BWTransformations.WEREWOLF) {
				return includeHumanForm || transformationComponent.isAlternateForm();
			}
		}
		return entity instanceof WerewolfEntity;
	}

	public static boolean isSourceFromSilver(DamageSource source) {
		if (source.getDirectEntity() instanceof LivingEntity livingEntity && livingEntity.getMainHandItem().getItem() instanceof AthameItem) {
			return true;
		}
		return source.getDirectEntity() instanceof SilverArrowEntity;
	}

	public static boolean isWeakToSilver(LivingEntity livingEntity) {
		if (livingEntity.getType().is(BWTags.IMMUNE_TO_SILVER)) {
			return false;
		}
		return livingEntity.isInvertedHealAndHarm() || livingEntity.getMobType() == DEMON || livingEntity.getType().is(BWTags.VULNERABLE_TO_SILVER);
	}

	public static boolean isPledged(Player player, String pledge) {
		PledgeComponent pledgeComponent = BWComponents.PLEDGE_COMPONENT.get(player);
		if (!player.level().isClientSide) {
			BWUniversalWorldState universalWorldState = BWUniversalWorldState.get(player.getCommandSenderWorld());
			for (int i = universalWorldState.pledgesToRemove.size() - 1; i >= 0; i--) {
				if (universalWorldState.pledgesToRemove.get(i).equals(player.getUUID())) {
					pledgeComponent.setPledgeNextTick(BWPledges.NONE);
					universalWorldState.pledgesToRemove.remove(i);
					return false;
				}
			}
		}
		return pledgeComponent.getPledge().equals(pledge);
	}

	public static boolean hasVoodooProtection(LivingEntity living, int damage) {
		PoppetData poppetData = BewitchmentAPI.getPoppet(living.level(), BWObjects.VOODOO_PROTECTION_POPPET, living);
		if (!poppetData.stack().isEmpty()) {
			boolean sync = false;
			if (poppetData.stack().hurt(damage, living.getRandom(), null) && poppetData.stack().getDamageValue() >= poppetData.stack().getMaxDamage()) {
				poppetData.stack().shrink(1);
				sync = true;
			}
			poppetData.update(living.level(), sync);
			return true;
		}
		return false;
	}

	public static void unpledge(Player player) {
		BWComponents.PLEDGE_COMPONENT.get(player).setPledge(BWPledges.NONE);
	}

	public static int getMoonPhase(LevelAccessor world) {
		return world.dimensionType().moonPhase(world.dayTime());
	}

	public static void registerAltarMapEntries(Block[]... altarArray) {
		for (Block[] altars : altarArray) {
			ALTAR_MAP_ENTRIES.add(new AltarMapEntry(altars[0], altars[1], Blocks.MOSS_CARPET.asItem()));
		}
		for (int i = 0; i < 16; i++) {
			Item carpet = switch (DyeColor.byId(i)) {
				case WHITE -> Items.WHITE_CARPET;
				case ORANGE -> Items.ORANGE_CARPET;
				case MAGENTA -> Items.MAGENTA_CARPET;
				case LIGHT_BLUE -> Items.LIGHT_BLUE_CARPET;
				case YELLOW -> Items.YELLOW_CARPET;
				case LIME -> Items.LIME_CARPET;
				case PINK -> Items.PINK_CARPET;
				case GRAY -> Items.GRAY_CARPET;
				case LIGHT_GRAY -> Items.LIGHT_GRAY_CARPET;
				case CYAN -> Items.CYAN_CARPET;
				case PURPLE -> Items.PURPLE_CARPET;
				case BLUE -> Items.BLUE_CARPET;
				case BROWN -> Items.BROWN_CARPET;
				case GREEN -> Items.GREEN_CARPET;
				case RED -> Items.RED_CARPET;
				case BLACK -> Items.BLACK_CARPET;
			};
			for (Block[] altars : altarArray) {
				ALTAR_MAP_ENTRIES.add(new AltarMapEntry(altars[0], altars[i + 2], carpet));
			}
		}
		for (Block[] altars : altarArray) {
			ALTAR_MAP_ENTRIES.add(new AltarMapEntry(altars[0], altars[18], BWObjects.HEDGEWITCH_CARPET.asItem()));
			ALTAR_MAP_ENTRIES.add(new AltarMapEntry(altars[0], altars[19], BWObjects.ALCHEMIST_CARPET.asItem()));
			ALTAR_MAP_ENTRIES.add(new AltarMapEntry(altars[0], altars[20], BWObjects.BESMIRCHED_CARPET.asItem()));
		}
	}
}
