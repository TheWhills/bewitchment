/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.dragonsblood;

import moriyashiine.bewitchment.common.block.entity.SigilBlockEntity;
import moriyashiine.bewitchment.common.block.entity.interfaces.SigilHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.Nullable;

public class DragonsBloodPressurePlateBlock extends PressurePlateBlock implements EntityBlock {
	public DragonsBloodPressurePlateBlock(Properties settings) {
		super(Sensitivity.EVERYTHING, settings, BlockSetType.OAK);
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
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (entity instanceof LivingEntity livingEntity) {
			SigilHolder.onUse(world, pos, livingEntity, InteractionHand.MAIN_HAND);
		}
		super.entityInside(state, world, pos, entity);
	}
}
