/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.block;

import moriyashiine.bewitchment.common.world.BWWorldState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CandelabraBlock extends Block implements SimpleWaterloggedBlock {
	private static final VoxelShape SHAPE = Shapes.or(box(6, 0, 6, 10, 2, 10), box(6.5, 2, 6.5, 9.5, 3, 9.5), box(7, 3, 7, 9, 16, 9), box(1, 7.5, 7, 15, 8.5, 9), box(7, 7.5, 1, 9, 8.5, 15), box(7, 8.5, 1, 9, 14.5, 3), box(1, 8.5, 7, 3, 14.5, 9), box(13, 8.5, 7, 15, 14.5, 9), box(7, 8.5, 13, 9, 14.5, 15), box(6.5, 9.5, 0.5, 9.5, 10.5, 3.5), box(0.5, 9.5, 6.5, 3.5, 10.5, 9.5), box(12.5, 9.5, 6.5, 15.5, 10.5, 9.5), box(6.5, 9.5, 12.5, 9.5, 10.5, 15.5), box(6.5, 11, 6.5, 9.5, 12, 9.5));

	public final byte repellentRadius;

	public CandelabraBlock(Properties settings, byte repellentRadius) {
		super(settings);
		this.repellentRadius = repellentRadius;
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false).setValue(BlockStateProperties.LIT, true));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		boolean client = world.isClientSide;
		if (!state.getValue(BlockStateProperties.WATERLOGGED)) {
			ItemStack stack = player.getItemInHand(hand);
			if (stack.getItem() instanceof FlintAndSteelItem && !state.getValue(BlockStateProperties.LIT)) {
				if (!client) {
					world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LIT, true));
					world.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1, 1);
					stack.hurtAndBreak(1, player, stackUser -> stackUser.broadcastBreakEvent(hand));
				}
				return InteractionResult.sidedSuccess(client);
			} else if (stack.isEmpty() && state.getValue(BlockStateProperties.LIT)) {
				if (!client) {
					world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LIT, false));
					world.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1, 2);
				}
				return InteractionResult.sidedSuccess(client);
			}
		}
		return super.use(state, world, pos, player, hand, hit);
	}

	@SuppressWarnings("ConstantConditions")
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return super.getStateForPlacement(ctx).setValue(BlockStateProperties.WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER).setValue(BlockStateProperties.LIT, false);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		if (state.getValue(BlockStateProperties.WATERLOGGED)) {
			world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
			if (state.getValue(BlockStateProperties.LIT)) {
				world.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1, 2);
				state = state.setValue(BlockStateProperties.LIT, false);
			}
		}
		return super.updateShape(state, direction, newState, world, pos, posFrom);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!world.isClientSide && state.getBlock() != oldState.getBlock()) {
			BWWorldState worldState = BWWorldState.get(world);
			worldState.potentialCandelabras.add(pos.asLong());
			worldState.setDirty();
		}
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		if (!world.isClientSide && state.getBlock() != newState.getBlock()) {
			BWWorldState worldState = BWWorldState.get(world);
			for (int i = worldState.potentialCandelabras.size() - 1; i >= 0; i--) {
				if (worldState.potentialCandelabras.get(i) == pos.asLong()) {
					worldState.potentialCandelabras.remove(i);
					worldState.setDirty();
				}
			}
		}
		super.onRemove(state, world, pos, newState, moved);
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		if (state.getValue(BlockStateProperties.LIT)) {
			world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 1.175, pos.getZ() + 0.5, 0, 0, 0);
			world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.125, pos.getY() + 1.075, pos.getZ() + 0.5, 0, 0, 0);
			world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.875, pos.getY() + 1.075, pos.getZ() + 0.5, 0, 0, 0);
			world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 1.075, pos.getZ() + 0.875, 0, 0, 0);
			world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 1.075, pos.getZ() + 0.125, 0, 0, 0);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.WATERLOGGED, BlockStateProperties.LIT);
	}
}
