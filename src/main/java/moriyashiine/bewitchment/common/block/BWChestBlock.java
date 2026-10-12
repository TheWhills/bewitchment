/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.common.block.entity.BWChestBlockEntity;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Supplier;

public class BWChestBlock extends ChestBlock {
	protected final boolean trapped;

	public BWChestBlock(Properties settings, Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier, boolean trapped) {
		super(settings, supplier);
		this.trapped = trapped;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST.get(), pos, state, getType(this), trapped);
	}

	@Override
	protected Stat<ResourceLocation> getOpenChestStat() {
		return trapped ? Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST) : super.getOpenChestStat();
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return trapped || super.isSignalSource(state);
	}

	@Override
	public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return trapped ? Mth.clamp(ChestBlockEntity.getOpenCount(world, pos), 0, 15) : super.getSignal(state, world, pos, direction);
	}

	@Override
	public int getDirectSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return trapped ? (direction == Direction.UP ? state.getSignal(world, pos, direction) : 0) : super.getDirectSignal(state, world, pos, direction);
	}

	public static BWChestBlockEntity.Type getType(Block block) {
		return block == BWObjects.JUNIPER_CHEST.get() || block == BWObjects.TRAPPED_JUNIPER_CHEST.get() ? BWChestBlockEntity.Type.JUNIPER : block == BWObjects.CYPRESS_CHEST.get() || block == BWObjects.TRAPPED_CYPRESS_CHEST.get() ? BWChestBlockEntity.Type.CYPRESS : block == BWObjects.ELDER_CHEST.get() || block == BWObjects.TRAPPED_ELDER_CHEST.get() ? BWChestBlockEntity.Type.ELDER : BWChestBlockEntity.Type.DRAGONS_BLOOD;
	}
}
