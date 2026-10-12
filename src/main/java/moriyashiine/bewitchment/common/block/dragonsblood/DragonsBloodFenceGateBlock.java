/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.dragonsblood;

import moriyashiine.bewitchment.common.block.entity.SigilBlockEntity;
import moriyashiine.bewitchment.common.block.entity.interfaces.SigilHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class DragonsBloodFenceGateBlock extends FenceGateBlock implements EntityBlock {
	public DragonsBloodFenceGateBlock(Properties settings) {
		super(settings, WoodType.OAK);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SigilBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return (tickerWorld, pos, tickerState, blockEntity) -> SigilBlockEntity.tick(tickerWorld, pos, tickerState, (SigilBlockEntity) blockEntity);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		SigilHolder.onUse(world, pos, player, hand);
		return super.use(state, world, pos, player, hand, hit);
	}
}
