/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.dragonsblood.DragonsBloodLogBlock;
import moriyashiine.bewitchment.common.block.entity.interfaces.Lockable;
import moriyashiine.bewitchment.common.block.entity.interfaces.SigilHolder;
import moriyashiine.bewitchment.common.block.entity.interfaces.TaglockHolder;
import moriyashiine.bewitchment.common.recipe.AthameStrippingRecipe;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWProperties;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import java.util.UUID;

public class AthameItem extends SwordItem {
	private static final AttributeModifier REACH_MODIFIER = new AttributeModifier(UUID.fromString("1f362972-c5c5-4e9d-b69f-1fd13bd269e3"), "Weapon modifier", -0.5, AttributeModifier.Operation.ADDITION);

	private static final DispenseItemBehavior DISPENSER_BEHAVIOR = new OptionalDispenseItemBehavior() {
		@Override
		protected ItemStack execute(BlockSource pointer, ItemStack stack) {
			Level world = pointer.getLevel();
			BlockPos pos = pointer.getPos().relative(pointer.getBlockState().getValue(BlockStateProperties.FACING));
			if (cutLog(world, pos, stack)) {
				setSuccess(true);
			} else if (world.getBlockState(pos).getBlock() == BWObjects.GOLDEN_GLYPH.get()) {
				BWObjects.GOLDEN_GLYPH.get().use(world.getBlockState(pos), world, pos, BewitchmentAPI.getFakePlayer(world), InteractionHand.MAIN_HAND, BlockHitResult.miss(new Vec3(pos.getX(), pos.getY(), pos.getZ()), Direction.DOWN, pos));
				setSuccess(true);
			}
			return stack;
		}
	};

	public AthameItem(Tier toolMaterial, int attackDamage, float attackSpeed, Properties settings) {
		super(toolMaterial, attackDamage, attackSpeed, settings);
		DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = world.getBlockState(pos);
		Player player = context.getPlayer();
		boolean client = world.isClientSide;
		AthameStrippingRecipe entry = world.getRecipeManager().getAllRecipesFor(BWRecipeTypes.ATHAME_STRIPPING_RECIPE_TYPE.get()).stream().filter(recipe -> recipe.log == state.getBlock()).findFirst().orElse(null);
		if (entry != null) {
			world.playSound(player, pos, BWSoundEvents.ITEM_ATHAME_STRIP.get(), SoundSource.BLOCKS, 1, 1);
			if (!client) {
				world.setBlock(pos, entry.strippedLog.defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)), 11);
				if (player != null) {
					context.getItemInHand().hurtAndBreak(1, player, stackUser -> stackUser.broadcastBreakEvent(context.getHand()));
					if (world.random.nextFloat() < 2 / 3f) {
						ItemStack bark = entry.getResultItem(world.registryAccess()).copy();
						if (!player.getInventory().add(bark)) {
							player.spawnAtLocation(bark);
						}
					}
				}
			}
			return InteractionResult.sidedSuccess(client);
		}
		BlockEntity blockEntity = world.getBlockEntity(state.getBlock() instanceof DoorBlock && state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos);
		if (blockEntity instanceof SigilHolder sigilHolder) {
			if (player != null && player.getUUID().equals(sigilHolder.getOwner())) {
				if (!client && !sigilHolder.getEntities().isEmpty()) {
					boolean whitelist = sigilHolder.getModeOnWhitelist();
					world.playSound(null, pos, BWSoundEvents.BLOCK_SIGIL_PLING.get(), SoundSource.BLOCKS, 1, whitelist ? 0.5f : 1);
					player.displayClientMessage(Component.translatable(Bewitchment.MOD_ID + ".message.toggle_" + (!whitelist ? "whitelist" : "blacklist")), true);
					sigilHolder.setModeOnWhitelist(!whitelist);
					blockEntity.setChanged();
				}
				return InteractionResult.sidedSuccess(client);
			}
		} else if (blockEntity instanceof TaglockHolder taglockHolder) {
			if (player != null && player.getUUID().equals(taglockHolder.getOwner()) && taglockHolder.getFirstEmptySlot() != 0) {
				if (!client) {
					Containers.dropContents(world, pos, taglockHolder.getTaglockInventory());
					taglockHolder.syncTaglockHolder(blockEntity);
					blockEntity.setChanged();
				}
				return InteractionResult.sidedSuccess(client);
			}
		} else if (blockEntity instanceof Lockable lockable) {
			if (player != null && player.getUUID().equals(lockable.getOwner()) && !lockable.getEntities().isEmpty()) {
				if (!client) {
					boolean whitelist = lockable.getModeOnWhitelist();
					world.playSound(null, pos, BWSoundEvents.BLOCK_SIGIL_PLING.get(), SoundSource.BLOCKS, 1, whitelist ? 0.5f : 1);
					player.displayClientMessage(Component.translatable(Bewitchment.MOD_ID + ".message.toggle_" + (!whitelist ? "whitelist" : "blacklist")), true);
					lockable.setModeOnWhitelist(!whitelist);
					blockEntity.setChanged();
				}
				return InteractionResult.sidedSuccess(client);
			}
		}
		return super.useOn(context);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		Multimap<Attribute, AttributeModifier> map = LinkedHashMultimap.create(super.getDefaultAttributeModifiers(slot));
		if (slot == EquipmentSlot.MAINHAND) {
			map.put(ReachEntityAttributes.ATTACK_RANGE, REACH_MODIFIER);
		}
		return map;
	}

	private static boolean cutLog(Level world, BlockPos pos, ItemStack stack) {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof DragonsBloodLogBlock && state.getValue(BWProperties.NATURAL) && !state.getValue(BWProperties.CUT)) {
			world.playSound(null, pos, BWSoundEvents.ITEM_ATHAME_STRIP.get(), SoundSource.BLOCKS, 1, 1);
			world.setBlockAndUpdate(pos, state.setValue(BWProperties.CUT, true));
			if (stack.hurt(1, world.random, null) && stack.getDamageValue() >= stack.getMaxDamage()) {
				stack.shrink(1);
			}
			return true;
		}
		return false;
	}
}
