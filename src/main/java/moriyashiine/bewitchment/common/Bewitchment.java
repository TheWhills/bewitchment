/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common;

import eu.midnightdust.lib.config.MidnightConfig;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.api.component.BloodComponent;
import moriyashiine.bewitchment.api.event.BloodSuckEvents;
import moriyashiine.bewitchment.common.block.CoffinBlock;
import moriyashiine.bewitchment.common.block.entity.BrazierBlockEntity;
import moriyashiine.bewitchment.common.block.entity.GlyphBlockEntity;
import moriyashiine.bewitchment.common.block.entity.SigilBlockEntity;
import moriyashiine.bewitchment.common.item.AthameItem;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.packet.CauldronTeleportPacket;
import moriyashiine.bewitchment.common.packet.TogglePressingForwardPacket;
import moriyashiine.bewitchment.common.packet.TransformationAbilityPacket;
import moriyashiine.bewitchment.common.recipe.AthameDropRecipe;
import moriyashiine.bewitchment.common.recipe.IncenseRecipe;
import moriyashiine.bewitchment.common.registry.*;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Bewitchment.MOD_ID)
public class Bewitchment {
	public static final String MOD_ID = "bewitchment";

	public Bewitchment() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		BWRegistration.register(modBus);
		modBus.addListener(this::commonSetup);
		onInitialize();
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> BewitchmentClient::init);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> BewitchmentAPI.registerAltarMapEntries(
				BWObjects.STONE_WITCH_ALTAR.get(),
				BWObjects.MOSSY_COBBLESTONE_WITCH_ALTAR.get(),
				BWObjects.PRISMARINE_WITCH_ALTAR.get(),
				BWObjects.NETHER_BRICK_WITCH_ALTAR.get(),
				BWObjects.BLACKSTONE_WITCH_ALTAR.get(),
				BWObjects.GOLDEN_WITCH_ALTAR.get(),
				BWObjects.END_STONE_WITCH_ALTAR.get(),
				BWObjects.OBSIDIAN_WITCH_ALTAR.get(),
				BWObjects.PURPUR_WITCH_ALTAR.get()));
	}

	// TODO(forge-port): split into event subscribers; body is still Fabric-shaped.
	@SuppressWarnings("ConstantConditions")
	private void onInitialize() {
		MidnightConfig.init(MOD_ID, BWConfig.class);
		ServerPlayNetworking.registerGlobalReceiver(CauldronTeleportPacket.ID, new CauldronTeleportPacket.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(TransformationAbilityPacket.ID, new TransformationAbilityPacket.Receiver());
		ServerPlayNetworking.registerGlobalReceiver(TogglePressingForwardPacket.ID, new TogglePressingForwardPacket.Receiver());
		CommandRegistrationCallback.EVENT.register(BWCommands::init);
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> BewitchmentAPI.fakePlayer = null);
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
			BWComponents.TRANSFORMATION_COMPONENT.get(newPlayer).setAlternateForm(false);
			BWComponents.ADDITIONAL_WEREWOLF_DATA_COMPONENT.get(newPlayer).setForcedTransformation(false);
		});
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			if (alive && BWComponents.TRANSFORMATION_COMPONENT.get(oldPlayer).isAlternateForm()) {
				TransformationAbilityPacket.useAbility(newPlayer, true);
			}
		});
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
			if (entity instanceof LivingEntity livingEntity) {
				if (livingEntity instanceof Player player && killedEntity.getMobType() == MobType.ARTHROPOD && BewitchmentAPI.getFamiliar(player) == BWEntityTypes.TOAD.get()) {
					livingEntity.heal(livingEntity.getMaxHealth() * 1 / 4f);
				}
				if (livingEntity instanceof Player player && BWComponents.CONTRACTS_COMPONENT.get(player).hasContract(BWContracts.DEATH.get())) {
					livingEntity.heal(livingEntity.getMaxHealth() / 4f);
				}
				if (livingEntity.getMainHandItem().getItem() instanceof AthameItem) {
					BlockPos glyphPos = BWUtil.getClosestBlockPos(killedEntity.blockPosition(), 6, currentPos -> world.getBlockEntity(currentPos) instanceof GlyphBlockEntity);
					if (glyphPos != null) {
						((GlyphBlockEntity) world.getBlockEntity(glyphPos)).onUse(world, glyphPos, livingEntity, InteractionHand.MAIN_HAND, killedEntity);
					}
					if (world.isNight()) {
						boolean chicken = killedEntity instanceof Chicken, wolf = killedEntity instanceof Wolf;
						if (chicken || wolf) {
							Holder<Biome> biome = world.getBiome(killedEntity.blockPosition());
							Mob boss = null;
							if (chicken) {
								if (biome.is(ConventionalBiomeTags.EXTREME_HILLS) || biome.is(ConventionalBiomeTags.ICY) || biome.is(ConventionalBiomeTags.MOUNTAIN)) {
									boss = BWEntityTypes.LILITH.get().create(world);
								}
							}
							if (wolf) {
								if (biome.is(ConventionalBiomeTags.TAIGA) || biome.is(ConventionalBiomeTags.FOREST)) {
									boss = BWEntityTypes.HERNE.get().create(world);
								}
							}
							if (boss != null) {
								BlockPos brazierPos = BWUtil.getClosestBlockPos(killedEntity.blockPosition(), 8, currentPos -> world.getBlockEntity(currentPos) instanceof BrazierBlockEntity brazier && brazier.incenseRecipe.effect == BWStatusEffects.MORTAL_COIL.get());
								if (brazierPos != null) {
									world.destroyBlock(brazierPos, false);
									world.explode(null, brazierPos.getX() + 0.5, brazierPos.getY() + 0.5, brazierPos.getZ() + 0.5, 3, Level.ExplosionInteraction.TNT);
									boss.finalizeSpawn(world, world.getCurrentDifficultyAt(brazierPos), MobSpawnType.EVENT, null, null);
									boss.absMoveTo(brazierPos.getX() + 0.5, brazierPos.getY(), brazierPos.getZ() + 0.5, world.random.nextFloat() * 360, 0);
									world.addFreshEntity(boss);
								}
							}
						}
					}
					for (AthameDropRecipe recipe : world.getRecipeManager().getAllRecipesFor(BWRecipeTypes.ATHAME_DROP_RECIPE_TYPE.get())) {
						if (recipe.entity_type.equals(killedEntity.getType()) && world.random.nextFloat() < recipe.chance * (EnchantmentHelper.getMobLooting(livingEntity) + 1)) {
							ItemStack drop = recipe.getResultItem(entity.level().registryAccess()).copy();
							if (recipe.entity_type == EntityType.PLAYER) {
								drop.getOrCreateTag().putString("SkullOwner", killedEntity.getName().getString());
							}
							Containers.dropItemStack(world, killedEntity.getX() + 0.5, killedEntity.getY() + 0.5, killedEntity.getZ() + 0.5, drop);
						}
					}
					if (livingEntity instanceof Player player && livingEntity.getOffhandItem().getItem() == Items.GLASS_BOTTLE && BWComponents.BLOOD_COMPONENT.get(killedEntity).getBlood() > 20 && killedEntity.getType().is(BWTags.HAS_BLOOD)) {
						world.playSound(null, livingEntity.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1, 0.5f);
						BWUtil.addItemToInventoryAndConsume(player, InteractionHand.OFF_HAND, new ItemStack(BWObjects.BOTTLE_OF_BLOOD.get()));
					}
				}
			}
		});
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (entity instanceof LivingEntity livingEntity && hand == InteractionHand.MAIN_HAND && player.isShiftKeyDown() && entity.isAlive() && BewitchmentAPI.isVampire(player, true) && player.getItemInHand(hand).isEmpty()) {
				int toGive = entity.getType().is(BWTags.HAS_BLOOD) ? 5 : entity instanceof Animal ? 1 : 0;
				toGive = BloodSuckEvents.BLOOD_AMOUNT.invoker().onBloodSuck(player, livingEntity, toGive);
				if (toGive > 0) {
					BloodComponent playerBloodComponent = BWComponents.BLOOD_COMPONENT.get(player);
					BloodComponent entityBloodComponent = BWComponents.BLOOD_COMPONENT.get(livingEntity);
					if (playerBloodComponent.fillBlood(toGive, true) && entityBloodComponent.drainBlood(10, true)) {
						if (!world.isClientSide && livingEntity.hurtTime == 0) {
							BloodSuckEvents.ON_BLOOD_SUCK.invoker().onBloodSuck(player, livingEntity, toGive);
							world.playSound(null, player.blockPosition(), SoundEvents.HONEY_DRINK, player.getSoundSource(), 1, 0.5f);
							if (!livingEntity.isSleeping() || entityBloodComponent.getBlood() < BloodComponent.MAX_BLOOD / 2) {
								entity.hurt(BWDamageSources.create(entity.level(), BWDamageSources.VAMPIRE), 2);
							}
							playerBloodComponent.fillBlood(toGive, false);
							entityBloodComponent.drainBlood(10, false);
						}
						return InteractionResult.sidedSuccess(world.isClientSide);
					}
				}
			}
			return InteractionResult.PASS;
		});
		EntitySleepEvents.ALLOW_SLEEP_TIME.register((player, sleepingPos, vanillaResult) -> player.level().getBlockState(sleepingPos).getBlock() instanceof CoffinBlock && player.level().isDay() ? InteractionResult.sidedSuccess(player.level().isClientSide) : InteractionResult.PASS);
		EntitySleepEvents.ALLOW_SLEEPING.register((player, sleepingPos) -> {
			if (BWComponents.TRANSFORMATION_COMPONENT.get(player).isAlternateForm()) {
				player.displayClientMessage(Component.translatable("block.minecraft.bed.transformation"), true);
				return Player.BedSleepingProblem.OTHER_PROBLEM;
			}
			if (player.level().getBlockState(sleepingPos).getBlock() instanceof CoffinBlock) {
				if (player.level().isNight()) {
					player.displayClientMessage(Component.translatable("block.minecraft.bed.coffin"), true);
					return Player.BedSleepingProblem.OTHER_PROBLEM;
				}
				return null;
			}
			return null;
		});
		EntitySleepEvents.STOP_SLEEPING.register((entity, sleepingPos) -> {
			if (!entity.level().isClientSide) {
				BWWorldState worldState = BWWorldState.get(entity.level());
				for (BlockPos foundPos : BWUtil.getBlockPoses(entity.blockPosition(), 12)) {
					if (entity.level().getWorldBorder().isWithinBounds(foundPos) && entity.level().getBlockEntity(foundPos) instanceof BrazierBlockEntity brazier && brazier.incenseRecipe != null) {
						IncenseRecipe recipe = brazier.incenseRecipe;
						int durationMultiplier = 1;
						for (BlockPos sigilPos : BWUtil.getBlockPoses(foundPos, 16)) {
							if (entity.level().getWorldBorder().isWithinBounds(foundPos) && worldState.potentialSigils.contains(sigilPos.asLong()) && entity.level().getBlockEntity(sigilPos) instanceof SigilBlockEntity sigil && sigil.getSigil() == BWSigils.EXTENDING.get()) {
								if (sigil.test(entity)) {
									sigil.setUses(sigil.getUses() - 1);
									sigil.setChanged();
									durationMultiplier = 2;
									break;
								}
							}
						}
						entity.addEffect(new MobEffectInstance(recipe.effect, 24000 * durationMultiplier, recipe.amplifier, true, false));
					}
				}
			}
		});
		// TODO(forge-port): Pehkui scale types, Terraform boats and biome modifications are ported in later steps.
		BWScaleTypes.init();
		BWBoatTypes.init();
		BWWorldGenerators.init();
	}

	public static ResourceLocation id(String value) {
		return new ResourceLocation(MOD_ID, value);
	}
}
