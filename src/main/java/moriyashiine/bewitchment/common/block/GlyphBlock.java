/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.api.block.WitchAltarBlock;
import moriyashiine.bewitchment.api.block.entity.UsesAltarPower;
import moriyashiine.bewitchment.common.block.entity.GlyphBlockEntity;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ConstantConditions")
public class GlyphBlock extends HorizontalDirectionalBlock implements EntityBlock {
	private static final VoxelShape SHAPE = box(0, 0, 0, 16, 0.125, 16);

	public GlyphBlock(Properties settings) {
		super(settings);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return state.getBlock() == BWObjects.GOLDEN_GLYPH.get() ? new GlyphBlockEntity(pos, state) : null;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return state.getBlock() == BWObjects.GOLDEN_GLYPH.get() ? (tickerWorld, pos, tickerState, blockEntity) -> GlyphBlockEntity.tick(tickerWorld, pos, tickerState, (GlyphBlockEntity) blockEntity) : null;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (this == BWObjects.GOLDEN_GLYPH.get()) {
			boolean client = world.isClientSide;
			if (!client) {
				((GlyphBlockEntity) world.getBlockEntity(pos)).onUse(world, pos, player, hand, null);
			}
			return InteractionResult.sidedSuccess(client);
		}
		return super.use(state, world, pos, player, hand, hit);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return super.getStateForPlacement(ctx).setValue(FACING, ctx.getHorizontalDirection()).setValue(BlockStateProperties.AGE_5, ctx.getLevel().random.nextInt(6));
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		return Blocks.TORCH.updateShape(state, direction, newState, world, pos, posFrom);
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
		return new ItemStack(this == BWObjects.GLYPH.get() ? BWObjects.CHALK.get() : this == BWObjects.GOLDEN_GLYPH.get() ? BWObjects.GOLDEN_CHALK.get() : this == BWObjects.FIERY_GLYPH.get() ? BWObjects.FIERY_CHALK.get() : BWObjects.ELDRITCH_CHALK.get());
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return BWObjects.SALT_LINE.get().canSurvive(state, world, pos);
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		return this != BWObjects.GOLDEN_GLYPH.get();
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!world.isClientSide && state.getBlock() != oldState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof UsesAltarPower usesAltarPower) {
				usesAltarPower.setAltarPos(WitchAltarBlock.getClosestAltarPos(world, pos));
				blockEntity.setChanged();
			}
		}
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		if (!world.isClientSide && state.getBlock() != newState.getBlock() && world.getBlockEntity(pos) instanceof Container inventory) {
			Containers.dropContents(world, pos, inventory);
		}
		super.onRemove(state, world, pos, newState, moved);
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		ParticleType<?> particleType = null;
		if (this == BWObjects.FIERY_GLYPH.get()) {
			particleType = ParticleTypes.FLAME;
		} else if (this == BWObjects.ELDRITCH_GLYPH.get()) {
			particleType = ParticleTypes.REVERSE_PORTAL;
		}
		if (particleType != null) {
			world.addParticle((ParticleOptions) particleType, pos.getX() + 0.5 + Mth.nextDouble(random, -0.5, 0.5), pos.getY() + 0.125, pos.getZ() + 0.5 + Mth.nextDouble(random, -0.5, 0.5), 0, 0, 0);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, BlockStateProperties.AGE_5);
	}
}
