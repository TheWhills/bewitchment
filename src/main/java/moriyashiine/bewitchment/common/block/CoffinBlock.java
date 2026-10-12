/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CoffinBlock extends BedBlock {
	private static final VoxelShape SHAPE = box(0, 0, 0, 16, 10, 16);

	public CoffinBlock(DyeColor color, Properties settings) {
		super(color, settings);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public void updateEntityAfterFallOn(BlockGetter world, Entity entity) {
		BWObjects.CYPRESS_PLANKS.updateEntityAfterFallOn(world, entity);
	}
}
