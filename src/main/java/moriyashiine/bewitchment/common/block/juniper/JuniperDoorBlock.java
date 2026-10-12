/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.juniper;

import moriyashiine.bewitchment.common.block.entity.TaglockHolderBlockEntity;
import moriyashiine.bewitchment.common.block.entity.interfaces.TaglockHolder;
import moriyashiine.bewitchment.common.block.util.interfaces.SpecialDoor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ConstantConditions")
public class JuniperDoorBlock extends DoorBlock implements EntityBlock, SpecialDoor {
	public JuniperDoorBlock(Properties settings) {
		super(settings, BlockSetType.OAK);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TaglockHolderBlockEntity(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		TaglockHolder.onUse(world, state.getValue(HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos, player);
		return super.use(state, world, pos, player, hand, hit);
	}

	@Override
	public InteractionResult onSpecialUse(BlockState state, Level world, BlockPos pos, LivingEntity user, InteractionHand hand) {
		TaglockHolder.onUse(world, state.getValue(HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos, user);
		return InteractionResult.PASS;
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.setPlacedBy(world, pos, state, placer, itemStack);
		if (!world.isClientSide && placer != null) {
			BlockEntity blockEntity = world.getBlockEntity(state.getValue(HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos);
			TaglockHolder taglockHolder = (TaglockHolder) blockEntity;
			taglockHolder.setOwner(placer.getUUID());
			taglockHolder.syncTaglockHolder(blockEntity);
			blockEntity.setChanged();
		}
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		if (!world.isClientSide && state.getBlock() != newState.getBlock() && world.getBlockEntity(pos) instanceof TaglockHolder taglockHolder) {
			Containers.dropContents(world, pos, taglockHolder.getTaglockInventory());
		}
		super.onRemove(state, world, pos, newState, moved);
	}
}
