/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.block;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.block.entity.UsesAltarPower;
import moriyashiine.bewitchment.api.registry.AltarMapEntry;
import moriyashiine.bewitchment.common.BWConfig;
import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWTags;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
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

@SuppressWarnings("ConstantConditions")
public class WitchAltarBlock extends HorizontalDirectionalBlock implements EntityBlock, SimpleWaterloggedBlock {
	private static final VoxelShape SHAPE = Shapes.or(box(0, 0, 0, 16, 2, 16), box(1, 2, 1, 15, 5, 15), box(2, 5, 2, 14, 10, 14), box(1, 10, 1, 15, 12, 15), box(0, 12, 0, 16, 16, 16));

	private final Block unformed;
	private final boolean formed;

	public WitchAltarBlock(Properties settings, Block unformed, boolean formed) {
		super(settings);
		this.unformed = unformed;
		this.formed = formed;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return formed ? new WitchAltarBlockEntity(pos, state) : null;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world0, BlockState state0, BlockEntityType<T> type) {
		return (world, pos, state, blockEntity) -> WitchAltarBlockEntity.tick(world, pos, state, (WitchAltarBlockEntity) blockEntity);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		boolean client = world.isClientSide;
		ItemStack stack = player.getItemInHand(hand);
		if (!formed) {
			AltarMapEntry entry = BewitchmentAPI.ALTAR_MAP_ENTRIES.stream().filter(e -> e.unformed() == this && e.carpet() == stack.getItem()).findFirst().orElse(null);
			if (entry != null) {
				if (!client) {
					if (!player.isCreative()) {
						stack.shrink(1);
					}
					Direction facing = world.getBlockState(pos).getValue(FACING);
					world.destroyBlock(pos, false);
					world.setBlockAndUpdate(pos, entry.formed().getStateForPlacement(new BlockPlaceContext(player, hand, stack, hit)).setValue(FACING, facing));
				}
				return InteractionResult.sidedSuccess(client);
			}
		} else {
			WitchAltarBlockEntity blockEntity = (WitchAltarBlockEntity) world.getBlockEntity(pos);
			if (!client) {
				if (!stack.isEmpty()) {
					boolean sword = stack.is(BWTags.SWORDS);
					boolean pentacle = stack.is(BWTags.PENTACLES);
					boolean wand = stack.is(BWTags.WANDS);
					if (sword || pentacle || wand) {
						int slot = sword ? 0 : pentacle ? 1 : 2;
						Containers.dropItemStack(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, blockEntity.removeItem(slot, 1));
						blockEntity.setItem(slot, stack.split(1));
						world.setBlock(pos, state.setValue(BlockStateProperties.LEVEL, calculateLuminance(blockEntity)), 11);
						world.updateNeighbourForOutputSignal(pos, this);
						blockEntity.markedForScan = true;
						blockEntity.setChanged();
						blockEntity.sync();
					}
				} else {
					if (player.isShiftKeyDown()) {
						Containers.dropContents(world, pos.offset(0, 1, 0), blockEntity);
						world.setBlock(pos, state.setValue(BlockStateProperties.LEVEL, 0), 11);
						world.updateNeighbourForOutputSignal(pos, this);
						blockEntity.markedForScan = true;
						blockEntity.setChanged();
						blockEntity.sync();
					} else {
						player.displayClientMessage(Component.literal(blockEntity.power + " / " + blockEntity.maxPower + " (" + blockEntity.gain + "x)"), true);
					}
				}
			}
			return InteractionResult.sidedSuccess(client);
		}
		return super.use(state, world, pos, player, hand, hit);
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
		if (unformed != null) {
			return new ItemStack(unformed);
		}
		return super.getCloneItemStack(world, pos, state);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return super.getStateForPlacement(ctx).setValue(BlockStateProperties.WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER).setValue(FACING, ctx.getHorizontalDirection());
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		if (state.getValue(BlockStateProperties.WATERLOGGED)) {
			world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return super.updateShape(state, direction, newState, world, pos, posFrom);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return formed;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		if (formed) {
			WitchAltarBlockEntity blockEntity = (WitchAltarBlockEntity) world.getBlockEntity(pos);
			int items = 0;
			for (int i = 0; i < blockEntity.getContainerSize(); i++) {
				if (!blockEntity.getItem(i).isEmpty()) {
					items++;
				}
			}
			return items;
		}
		return 0;
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		if (formed) {
			if (!world.isClientSide && state.getBlock() != oldState.getBlock()) {
				BWWorldState worldState = BWWorldState.get(world);
				worldState.potentialCandelabras.add(pos.asLong());
				worldState.setDirty();
			}
			for (BlockPos foundPos : BWUtil.getBlockPoses(pos, BWConfig.altarDistributionRadius)) {
				if (world.getWorldBorder().isWithinBounds(foundPos) && world.getBlockEntity(foundPos) instanceof UsesAltarPower usesAltarPower) {
					usesAltarPower.setAltarPos(getClosestAltarPos(world, foundPos));
					((BlockEntity) usesAltarPower).setChanged();
				}
			}
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
			if (world.getBlockEntity(pos) instanceof Container inventory) {
				Containers.dropContents(world, pos, inventory);
			}
		}
		super.onRemove(state, world, pos, newState, moved);
		if (!world.isClientSide && state.getBlock() != newState.getBlock()) {
			for (BlockPos foundPos : BWUtil.getBlockPoses(pos, BWConfig.altarDistributionRadius)) {
				if (world.getBlockEntity(foundPos) instanceof UsesAltarPower usesAltarPower) {
					usesAltarPower.setAltarPos(getClosestAltarPos(world, foundPos));
					((BlockEntity) usesAltarPower).setChanged();
				}
			}
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.WATERLOGGED, FACING, BlockStateProperties.LEVEL);
	}

	@Nullable
	public static BlockPos getClosestAltarPos(Level world, BlockPos pos) {
		return BWUtil.getClosestBlockPos(pos, BWConfig.altarDistributionRadius, currentPos -> world.getBlockEntity(currentPos) instanceof WitchAltarBlockEntity);
	}

	private static int calculateLuminance(WitchAltarBlockEntity blockEntity) {
		int luminance = 0;
		Item sword = blockEntity.getItem(0).getItem();
		Item pentacle = blockEntity.getItem(1).getItem();
		Item wand = blockEntity.getItem(2).getItem();
		if (sword instanceof BlockItem blockItem) {
			luminance = Math.max(luminance, blockItem.getBlock().defaultBlockState().getLightEmission());
		}
		if (pentacle instanceof BlockItem blockItem) {
			luminance = Math.max(luminance, blockItem.getBlock().defaultBlockState().getLightEmission());
		}
		if (wand instanceof BlockItem blockItem) {
			luminance = Math.max(luminance, blockItem.getBlock().defaultBlockState().getLightEmission());
		}
		return luminance;
	}
}
