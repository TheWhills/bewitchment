/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block.entity;

import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BWChestBlockEntity extends ChestBlockEntity {
	public final Type type;
	public final boolean trapped;

	public BWChestBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, Type type, boolean trapped) {
		super(blockEntityType, blockPos, blockState);
		this.type = type;
		this.trapped = trapped;
	}

	public BWChestBlockEntity(BlockPos blockPos, BlockState blockState) {
		this(BWBlockEntityTypes.BW_CHEST.get(), blockPos, blockState, Type.JUNIPER, false);
	}

	@Override
	protected void signalOpenCount(Level world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
		super.signalOpenCount(world, pos, state, oldViewerCount, newViewerCount);
		if (trapped && world != null) {
			world.updateNeighborsAt(pos.below(), getBlockState().getBlock());
		}
	}

	public enum Type {
		JUNIPER, CYPRESS, ELDER, DRAGONS_BLOOD
	}
}
