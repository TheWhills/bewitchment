/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.item;

import moriyashiine.bewitchment.api.entity.BroomEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class BroomItem extends Item {
	private final EntityType<?> broom;

	public BroomItem(Properties settings, EntityType<?> broom) {
		super(settings);
		this.broom = broom;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
		boolean client = world.isClientSide;
		if (world.getBlockState(pos).isAir()) {
			if (!client) {
				Entity entity = broom.create(world);
				if (entity instanceof BroomEntity broomEntity) {
					entity.absMoveTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, context.getRotation(), context.getPlayer() == null ? 0 : context.getPlayer().getXRot());
					if (context.getPlayer() != null) {
						context.getItemInHand().getOrCreateTag().putUUID("OwnerUUID", context.getPlayer().getUUID());
					}
					ItemStack stack = context.getItemInHand().split(1);
					broomEntity.init(stack);
					broomEntity.stack = stack;
					world.addFreshEntity(entity);
				}
			}
			return InteractionResult.sidedSuccess(client);
		}
		return super.useOn(context);
	}
}
