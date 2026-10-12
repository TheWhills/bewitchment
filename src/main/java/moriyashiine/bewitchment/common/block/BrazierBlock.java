/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.api.block.WitchAltarBlock;
import moriyashiine.bewitchment.api.block.entity.UsesAltarPower;
import moriyashiine.bewitchment.common.block.entity.BrazierBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class BrazierBlock extends LanternBlock implements EntityBlock {
	private static final VoxelShape STANDING_SHAPE = Shapes.or(box(7, 0, 5, 9, 13, 6), box(5, 0, 7, 6, 13, 9), box(10, 0, 7, 11, 13, 9), box(7, 0, 10, 9, 13, 11), box(7.01, 0, 11, 9.01, 1, 13), box(7.01, 0, 3, 9.01, 1, 5), box(3, 0, 7.01, 5, 1, 9.01), box(11, 0, 7.01, 13, 1, 9.01), box(7, 4.09823, 8.05128, 9, 5.09823, 10.05128), box(6, 4.08, 7, 8, 5.08, 9), box(8, 4.08, 7, 10, 5.08, 9), box(7, 4.09823, 5.94872, 9, 5.09823, 7.94872), box(4.5, 13.5, 4.5, 11.5, 14.5, 11.5), box(4.5, 14.5, 11.5, 11.5, 15.5, 12.5), box(4.5, 14.5, 3.5, 11.5, 15.5, 4.5), box(11.5, 14.5, 3.5, 12.5, 15.5, 12.5), box(3.5, 14.5, 3.5, 4.5, 15.5, 12.5), box(3.5, 15.5, 2.5, 12.5, 16.5, 3.5), box(12.5, 15.5, 3.5, 13.5, 16.5, 12.5), box(3.5, 15.5, 12.5, 12.5, 16.5, 13.5), box(2.5, 15.5, 3.5, 3.5, 16.5, 12.5));
	private static final VoxelShape HANGING_SHAPE = Shapes.or(box(2.5, 2.5, 3.5, 3.5, 3.5, 12.5), box(3.5, 2.5, 12.5, 12.5, 3.5, 13.5), box(12.5, 2.5, 3.5, 13.5, 3.5, 12.5), box(3.5, 2.5, 2.5, 12.5, 3.5, 3.5), box(3.5, 1.5, 3.5, 4.5, 2.5, 12.5), box(4.5, 1.5, 3.5, 11.5, 2.5, 4.5), box(11.5, 1.5, 3.5, 12.5, 2.5, 12.5), box(4.5, 1.5, 11.5, 11.5, 2.5, 12.5), box(4.5, 0.5, 4.5, 11.5, 1.5, 11.5), box(7, -0.7, 5.3, 9, 0.3, 7.3), box(8.7, -0.7, 7, 10.7, 0.3, 9), box(5.3, -0.7, 7, 7.3, 0.3, 9), box(7, -0.7, 8.7, 9, 0.3, 10.7), box(7.5, 3.5, 2.5, 8.5, 5.5, 3.5), box(2.5, 3.5, 7.5, 3.5, 5.5, 8.5), box(7.5, 3.5, 12.5, 8.5, 5.5, 13.5), box(12.5, 3.5, 7.5, 13.5, 5.5, 8.5), box(7, 14, 7, 9, 16, 9));

	public BrazierBlock(Properties settings) {
		super(settings);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.LIT, false));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BrazierBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return (tickerWorld, pos, tickerState, blockEntity) -> BrazierBlockEntity.tick(tickerWorld, pos, tickerState, (BrazierBlockEntity) blockEntity);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return state.getValue(BlockStateProperties.HANGING) ? HANGING_AABB : AABB;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		boolean client = world.isClientSide;
		if (!client) {
			((BrazierBlockEntity) world.getBlockEntity(pos)).onUse(world, pos, player, hand);
		}
		return InteractionResult.sidedSuccess(client);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		if (state.getValue(BlockStateProperties.WATERLOGGED)) {
			if (state.getValue(BlockStateProperties.LIT)) {
				world.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1, 2);
				state = state.setValue(BlockStateProperties.LIT, false);
			}
		}
		return super.updateShape(state, direction, newState, world, pos, posFrom);
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
		if (!world.isClientSide && state.getBlock() != newState.getBlock() && world.getBlockEntity(pos) instanceof BrazierBlockEntity brazier && brazier.incenseRecipe == null) {
			Containers.dropContents(world, pos, brazier);
		}
		super.onRemove(state, world, pos, newState, moved);
	}


	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		if (state.getValue(BlockStateProperties.LIT)) {
			double y = pos.getY() + (state.getValue(BlockStateProperties.HANGING) ? 0.3 : 1.15);
			world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5 + Mth.nextDouble(random, -0.2, 0.2), y, pos.getZ() + 0.5 + Mth.nextDouble(random, -0.2, 0.2), 0, 0, 0);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag options) {
		if (!PotionUtils.getCustomEffects(stack).isEmpty()) {
			PotionUtils.addPotionTooltip(stack, tooltip, 1);
		}
		if (stack.hasTag() && stack.getTag().contains("Cost")) {
			tooltip.add(Component.literal("Cost: " + stack.getOrCreateTag().getInt("Cost")).withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(BlockStateProperties.LIT));
	}
}
