/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.elder;

import moriyashiine.bewitchment.common.block.entity.LockableBlockEntity;
import moriyashiine.bewitchment.common.block.entity.interfaces.Lockable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ConstantConditions")
public class ElderPressurePlateBlock extends PressurePlateBlock implements EntityBlock {
	public ElderPressurePlateBlock(Properties settings) {
		super(Sensitivity.EVERYTHING, settings, BlockSetType.OAK);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new LockableBlockEntity(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		InteractionResult result = Lockable.onUse(world, pos, player, hand);
		if (result != InteractionResult.PASS) {
			return result;
		}
		return super.use(state, world, pos, player, hand, hit);
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.setPlacedBy(world, pos, state, placer, itemStack);
		if (!world.isClientSide && placer != null) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			Lockable lockable = (Lockable) blockEntity;
			lockable.setOwner(placer.getUUID());
			lockable.syncLockable(blockEntity);
			blockEntity.setChanged();
		}
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (entity != null) {
			Lockable lockable = ((Lockable) world.getBlockEntity(pos));
			if (lockable.getLocked() && !lockable.test(entity)) {
				return;
			}
		}
		super.entityInside(state, world, pos, entity);
	}
}
