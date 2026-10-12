/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.common.entity.boat.BWBoat;
import moriyashiine.bewitchment.common.entity.boat.BWBoatType;
import moriyashiine.bewitchment.common.entity.boat.BWChestBoat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

/**
 * Places a Bewitchment boat or chest boat. Same placement rules as vanilla's BoatItem, which
 * can only create vanilla boat types (its boat factory is private).
 */
public class BWBoatItem extends Item {
	private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);

	private final BWBoatType boatType;
	private final boolean chest;

	public BWBoatItem(BWBoatType boatType, boolean chest, Properties properties) {
		super(properties);
		this.boatType = boatType;
		this.chest = chest;
	}

	private Boat create(Level level, double x, double y, double z) {
		return chest ? new BWChestBoat(level, x, y, z, boatType) : new BWBoat(level, x, y, z, boatType);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		HitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
		if (hit.getType() == HitResult.Type.MISS) {
			return InteractionResultHolder.pass(stack);
		}
		Vec3 view = player.getViewVector(1);
		List<Entity> nearby = level.getEntities(player, player.getBoundingBox().expandTowards(view.scale(5)).inflate(1), ENTITY_PREDICATE);
		Vec3 eye = player.getEyePosition();
		for (Entity entity : nearby) {
			AABB box = entity.getBoundingBox().inflate(entity.getPickRadius());
			if (box.contains(eye)) {
				return InteractionResultHolder.pass(stack);
			}
		}
		if (hit.getType() != HitResult.Type.BLOCK) {
			return InteractionResultHolder.pass(stack);
		}
		Vec3 location = hit.getLocation();
		Boat boat = create(level, location.x, location.y, location.z);
		boat.setYRot(player.getYRot());
		if (!level.noCollision(boat, boat.getBoundingBox())) {
			return InteractionResultHolder.fail(stack);
		}
		if (!level.isClientSide) {
			level.addFreshEntity(boat);
			level.gameEvent(player, GameEvent.ENTITY_PLACE, location);
			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}
		}
		player.awardStat(Stats.ITEM_USED.get(this));
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}

	/** Dispensers place these boats on water, like vanilla boats. */
	public static class DispenseBehavior extends DefaultDispenseItemBehavior {
		private final DefaultDispenseItemBehavior fallback = new DefaultDispenseItemBehavior();

		@Override
		public ItemStack execute(BlockSource source, ItemStack stack) {
			if (!(stack.getItem() instanceof BWBoatItem item)) {
				return fallback.dispense(source, stack);
			}
			Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
			Level level = source.getLevel();
			double offset = 0.5625 + EntityType.BOAT.getWidth() / 2;
			double x = source.x() + direction.getStepX() * offset;
			double y = source.y() + direction.getStepY() * 1.125F;
			double z = source.z() + direction.getStepZ() * offset;
			BlockPos front = source.getPos().relative(direction);
			double lift;
			if (level.getFluidState(front).is(FluidTags.WATER)) {
				lift = 1;
			} else if (level.getBlockState(front).isAir() && level.getFluidState(front.below()).is(FluidTags.WATER)) {
				lift = 0;
			} else {
				return fallback.dispense(source, stack);
			}
			Boat boat = item.create(level, x, y + lift, z);
			boat.setYRot(direction.toYRot());
			level.addFreshEntity(boat);
			stack.shrink(1);
			return stack;
		}
	}
}
