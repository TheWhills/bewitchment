/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.world.level.block.RedStoneWireBlock.*;

public class SaltLineBlock extends Block {
	private static final Map<Direction, VoxelShape> SIDE_SHAPES = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, box(3, 0, 0, 13, 1, 13), Direction.SOUTH, box(3, 0, 3, 13, 1, 16), Direction.EAST, box(3, 0, 3, 16, 1, 13), Direction.WEST, box(0, 0, 3, 13, 1, 13)));
	private static final Map<Direction, VoxelShape> NON_SIDE_SHAPES = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Shapes.or(SIDE_SHAPES.get(Direction.NORTH), box(3, 0, 0, 13, 16, 1)), Direction.SOUTH, Shapes.or(SIDE_SHAPES.get(Direction.SOUTH), box(3, 0, 15, 13, 16, 16)), Direction.EAST, Shapes.or(SIDE_SHAPES.get(Direction.EAST), box(15, 0, 3, 16, 16, 13)), Direction.WEST, Shapes.or(SIDE_SHAPES.get(Direction.WEST), box(0, 0, 3, 1, 16, 13))));
	private static final VoxelShape DOT_SHAPE = box(3, 0, 3, 13, 1, 13);
	private final Map<BlockState, VoxelShape> outlineShapes = new HashMap<>();

	public SaltLineBlock(Properties settings) {
		super(settings);
		registerDefaultState(defaultBlockState().setValue(NORTH, RedstoneSide.NONE).setValue(EAST, RedstoneSide.NONE).setValue(SOUTH, RedstoneSide.NONE).setValue(WEST, RedstoneSide.NONE));
		for (BlockState state : getStateDefinition().getPossibleStates()) {
			VoxelShape voxelShape = DOT_SHAPE;
			for (Direction direction : Direction.Plane.HORIZONTAL) {
				RedstoneSide wireConnection = state.getValue(PROPERTY_BY_DIRECTION.get(direction));
				if (wireConnection == RedstoneSide.SIDE) {
					voxelShape = Shapes.or(voxelShape, SIDE_SHAPES.get(direction));
				} else if (wireConnection == RedstoneSide.UP) {
					voxelShape = Shapes.or(voxelShape, NON_SIDE_SHAPES.get(direction));
				}
			}
			outlineShapes.put(state, voxelShape);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return outlineShapes.get(state);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (context instanceof EntityCollisionContext entityShapeContext && entityShapeContext.getEntity() instanceof LivingEntity living && BewitchmentAPI.isWeakToSilver(living)) {
			return Shapes.block();
		}
		return super.getCollisionShape(state, world, pos, context);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return getPlacementState(ctx.getLevel(), ctx.getClickedPos());
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		if (direction == Direction.DOWN) {
			return state;
		} else if (direction == Direction.UP) {
			return getPlacementState(world, pos);
		} else {
			RedstoneSide wireConnection = getConnectionFromNeighbors(world, pos, direction, !world.getBlockState(pos.above()).isRedstoneConductor(world, pos));
			return wireConnection.isConnected() == state.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected() && !isFullyConnected(state) ? state.setValue(PROPERTY_BY_DIRECTION.get(direction), wireConnection) : getPlacementState(world, pos);
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (player.getAbilities().mayBuild) {
			if (isFullyConnected(state) || isNotConnected(state)) {
				BlockState placementState = getPlacementState(world, pos);
				if (placementState != state) {
					world.setBlock(pos, placementState, 3);
					for (Direction direction : Direction.Plane.HORIZONTAL) {
						BlockPos offset = pos.relative(direction);
						if (state.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected() != placementState.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected() && world.getBlockState(offset).isRedstoneConductor(world, offset)) {
							world.updateNeighborsAtExceptFromFacing(offset, placementState.getBlock(), direction.getOpposite());
						}
					}
					return InteractionResult.SUCCESS;
				}
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		BlockPos blockPos = pos.below();
		return canRunOnTop(world, blockPos, world.getBlockState(blockPos));
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		if (!world.isClientSide && !state.canSurvive(world, pos)) {
			dropResources(state, world, pos);
			world.removeBlock(pos, false);
		}
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!oldState.is(state.getBlock()) && !world.isClientSide) {
			for (Direction direction : Direction.Plane.VERTICAL) {
				world.updateNeighborsAt(pos.relative(direction), this);
			}
			updateNeighbors(world, pos);
		}
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		if (!moved && !state.is(newState.getBlock())) {
			super.onRemove(state, world, pos, newState, false);
			if (!world.isClientSide) {
				for (Direction direction : Direction.values()) {
					world.updateNeighborsAt(pos.relative(direction), this);
				}
				updateNeighbors(world, pos);
			}
		}
	}

	@Override
	public void updateIndirectNeighbourShapes(BlockState state, LevelAccessor world, BlockPos pos, int flags, int maxUpdateDepth) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			RedstoneSide wireConnection = state.getValue(PROPERTY_BY_DIRECTION.get(direction));
			if (wireConnection != RedstoneSide.NONE && !world.getBlockState(mutable.setWithOffset(pos, direction)).is(this)) {
				mutable.move(Direction.DOWN);
				BlockState blockState = world.getBlockState(mutable);
				BlockPos offset;
				if (!blockState.is(Blocks.OBSERVER)) {
					offset = mutable.relative(direction.getOpposite());
					updateOrDestroy(blockState, blockState.updateShape(direction.getOpposite(), world.getBlockState(offset), world, mutable, offset), world, mutable, flags, maxUpdateDepth);
				}
				mutable.setWithOffset(pos, direction).move(Direction.UP);
				blockState = world.getBlockState(mutable);
				if (!blockState.is(Blocks.OBSERVER)) {
					offset = mutable.relative(direction.getOpposite());
					updateOrDestroy(blockState, blockState.updateShape(direction.getOpposite(), world.getBlockState(offset), world, mutable, offset), world, mutable, flags, maxUpdateDepth);
				}
			}
		}
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return switch (rotation) {
			case CLOCKWISE_180 ->
					state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
			case COUNTERCLOCKWISE_90 ->
					state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
			case CLOCKWISE_90 ->
					state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
			default -> state;
		};
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return switch (mirror) {
			case LEFT_RIGHT ->
					state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
			case FRONT_BACK ->
					state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
			default -> super.mirror(state, mirror);
		};
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(NORTH, SOUTH, EAST, WEST);
	}

	private static boolean isFullyConnected(BlockState state) {
		return state.getValue(NORTH).isConnected() && state.getValue(SOUTH).isConnected() && state.getValue(EAST).isConnected() && state.getValue(WEST).isConnected();
	}

	private static boolean isNotConnected(BlockState state) {
		return !state.getValue(NORTH).isConnected() && !state.getValue(SOUTH).isConnected() && !state.getValue(EAST).isConnected() && !state.getValue(WEST).isConnected();
	}

	private BlockState getPlacementState(BlockGetter world, BlockPos pos) {
		BlockState state = defaultBlockState();
		boolean notSolid = !world.getBlockState(pos.above()).isRedstoneConductor(world, pos);
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			if (!state.getValue(PROPERTY_BY_DIRECTION.get(direction)).isConnected()) {
				RedstoneSide wireConnection = getConnectionFromNeighbors(world, pos, direction, notSolid);
				state = state.setValue(PROPERTY_BY_DIRECTION.get(direction), wireConnection);
			}
		}
		if (!isNotConnected(state) || !isNotConnected(state)) {
			boolean north = state.getValue(NORTH).isConnected();
			boolean south = state.getValue(SOUTH).isConnected();
			boolean east = state.getValue(EAST).isConnected();
			boolean west = state.getValue(WEST).isConnected();
			boolean eastOrWest = !north && !south;
			boolean northOrSouth = !east && !west;
			if (!west && eastOrWest) {
				state = state.setValue(WEST, RedstoneSide.SIDE);
			}
			if (!east && eastOrWest) {
				state = state.setValue(EAST, RedstoneSide.SIDE);
			}
			if (!north && northOrSouth) {
				state = state.setValue(NORTH, RedstoneSide.SIDE);
			}
			if (!south && northOrSouth) {
				state = state.setValue(SOUTH, RedstoneSide.SIDE);
			}
		}
		return state;
	}

	private RedstoneSide getConnectionFromNeighbors(BlockGetter blockView, BlockPos blockPos, Direction direction, boolean notSolid) {
		BlockPos offset = blockPos.relative(direction);
		BlockState state = blockView.getBlockState(offset);
		if (notSolid && canRunOnTop(blockView, offset, state) && blockView.getBlockState(offset.above()).is(this)) {
			return state.isFaceSturdy(blockView, offset, direction.getOpposite()) ? RedstoneSide.UP : RedstoneSide.SIDE;
		}
		return !state.is(this) && (state.isRedstoneConductor(blockView, offset) || !blockView.getBlockState(offset.below()).is(this)) ? RedstoneSide.NONE : RedstoneSide.SIDE;
	}

	private boolean canRunOnTop(BlockGetter world, BlockPos pos, BlockState floor) {
		return floor.isFaceSturdy(world, pos, Direction.UP) || floor.is(Blocks.HOPPER);
	}

	private void updateNeighbors(Level world, BlockPos pos) {
		List<BlockPos> toUpdate = new ArrayList<>();
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			toUpdate.add(pos.relative(direction));
		}
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			BlockPos offset = pos.relative(direction);
			toUpdate.add(world.getBlockState(offset).isRedstoneConductor(world, offset) ? offset.above() : offset.below());
		}
		toUpdate.forEach(blockPos -> {
			if (world.getBlockState(blockPos).is(this)) {
				world.updateNeighborsAt(blockPos, this);
				for (Direction direction : Direction.values()) {
					world.updateNeighborsAt(blockPos.relative(direction), this);
				}
			}
		});
	}
}
