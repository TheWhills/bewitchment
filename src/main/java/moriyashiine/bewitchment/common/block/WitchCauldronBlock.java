/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.api.block.WitchAltarBlock;
import moriyashiine.bewitchment.api.block.entity.UsesAltarPower;
import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import moriyashiine.bewitchment.common.block.entity.WitchCauldronBlockEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.recipe.OilRecipe;
import moriyashiine.bewitchment.common.registry.BWProperties;
import moriyashiine.bewitchment.common.registry.BWTags;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.NameTagItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
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
public class WitchCauldronBlock extends Block implements EntityBlock, SimpleWaterloggedBlock {
	private static final VoxelShape SHAPE = Shapes.or(box(2, 1, 2, 14, 2, 14), box(14, 2, 1, 15, 6, 15), box(1, 2, 1, 2, 6, 15), box(2, 2, 14, 14, 6, 15), box(13, 5, 3, 14, 8.5, 13), box(2, 2, 1, 14, 6, 2), box(2, 5, 2, 14, 8.5, 3), box(1, 8.5, 14, 15, 11, 15), box(2, 5, 3, 3, 8.5, 13), box(2, 5, 13, 14, 8.5, 14), box(14, 8.5, 2, 15, 11, 14), box(1, 8.5, 1, 15, 11, 2), box(1, 8.5, 2, 2, 11, 14), box(11, 0, 3, 13, 1, 5), box(3, 0, 3, 5, 1, 5), box(3, 0, 11, 5, 1, 13), box(11, 0, 11, 13, 1, 13));

	public WitchCauldronBlock(Properties settings) {
		super(settings);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false).setValue(BWProperties.LEVEL, 0));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new WitchCauldronBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return (tickerWorld, pos, tickerState, blockEntity) -> WitchCauldronBlockEntity.tick(tickerWorld, pos, tickerState, (WitchCauldronBlockEntity) blockEntity);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return super.getStateForPlacement(ctx).setValue(BlockStateProperties.WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER).setValue(BlockStateProperties.LIT, ctx.getLevel().getBlockState(ctx.getClickedPos().below()).is(BWTags.HEATS_CAULDRON));
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		if (state.getValue(BlockStateProperties.WATERLOGGED)) {
			world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
			state = state.setValue(BWProperties.LEVEL, 0);
			state = state.setValue(BlockStateProperties.LIT, false);
		} else {
			state = state.setValue(BlockStateProperties.LIT, world.getBlockState(pos.below()).is(BWTags.HEATS_CAULDRON));
		}
		return super.updateShape(state, direction, newState, world, pos, posFrom);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.getBlockEntity(pos) instanceof WitchCauldronBlockEntity cauldron) {
			boolean client = world.isClientSide;
			ItemStack stack = player.getItemInHand(hand);
			boolean nameTag = stack.getItem() instanceof NameTagItem, bucket = stack.getItem() == Items.BUCKET, waterBucket = stack.getItem() == Items.WATER_BUCKET, glassBottle = stack.getItem() == Items.GLASS_BOTTLE, waterBottle = stack.getItem() == Items.POTION && PotionUtils.getPotion(stack) == Potions.WATER;
			if (nameTag || bucket || waterBucket || glassBottle || waterBottle) {
				if (!client) {
					if (nameTag && stack.hasCustomHoverName()) {
						cauldron.name = stack.getHoverName().getString();
						cauldron.setChanged();
						BWWorldState worldState = BWWorldState.get(world);
						worldState.witchCauldrons.put(pos.asLong(), cauldron.name);
						worldState.setDirty();
						if (!player.isCreative()) {
							stack.shrink(1);
						}
					} else {
						int targetLevel = cauldron.getTargetLevel(stack);
						if (targetLevel > -1) {
							if (bucket) {
								BWUtil.addItemToInventoryAndConsume(player, hand, new ItemStack(Items.WATER_BUCKET));
							} else if (waterBucket) {
								BWUtil.addItemToInventoryAndConsume(player, hand, new ItemStack(Items.BUCKET));
							} else if (glassBottle) {
								ItemStack bottle = null;
								if (cauldron.mode == WitchCauldronBlockEntity.Mode.NORMAL) {
									bottle = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
								} else if (cauldron.mode == WitchCauldronBlockEntity.Mode.OIL_CRAFTING) {
									OilRecipe recipe = cauldron.oilRecipe;
									if (recipe != null) {
										bottle = recipe.getResultItem(world.registryAccess()).copy();
									}
								} else {
									bottle = cauldron.getPotion(player);
									if (targetLevel == 2) {
										boolean failed = true;
										BlockPos altarPos = cauldron.getAltarPos();
										if (altarPos != null && ((WitchAltarBlockEntity) world.getBlockEntity(altarPos)).drain(cauldron.getBrewCost(), false)) {
											failed = false;
										}
										if (failed) {
											cauldron.mode = cauldron.fail();
											cauldron.syncCauldron();
											return InteractionResult.FAIL;
										}
									}
								}
								if (bottle != null) {
									BWUtil.addItemToInventoryAndConsume(player, hand, bottle);
								}
							} else if (waterBottle) {
								BWUtil.addItemToInventoryAndConsume(player, hand, new ItemStack(Items.GLASS_BOTTLE));
							}
							if (targetLevel == 0) {
								cauldron.mode = cauldron.reset();
							}
							world.setBlockAndUpdate(pos, state.setValue(BWProperties.LEVEL, targetLevel));
							world.playSound(null, pos, bucket ? SoundEvents.BUCKET_FILL : waterBucket ? SoundEvents.BUCKET_EMPTY : glassBottle ? SoundEvents.BOTTLE_FILL : SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1, 1);
						}
					}
					cauldron.syncCauldron();
				}
			}
			return InteractionResult.sidedSuccess(client);
		}
		return super.use(state, world, pos, player, hand, hit);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return state.getValue(BWProperties.LEVEL);
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!world.isClientSide && state.getBlock() != oldState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			((UsesAltarPower) blockEntity).setAltarPos(WitchAltarBlock.getClosestAltarPos(world, pos));
			blockEntity.setChanged();
		}
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		if (!world.isClientSide && state.getBlock() != newState.getBlock()) {
			BWWorldState worldState = BWWorldState.get(world);
			if (worldState.witchCauldrons.remove(pos.asLong()) != null) {
				worldState.setDirty();
			}
		}
		super.onRemove(state, world, pos, newState, moved);
	}

	@Override
	public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
		if (world.getBlockState(pos).getValue(BWProperties.LEVEL) > 0 && entity instanceof LivingEntity) {
			WitchCauldronBlockEntity blockEntity = (WitchCauldronBlockEntity) world.getBlockEntity(pos);
			if (blockEntity.heatTimer >= 60 && blockEntity.mode != WitchCauldronBlockEntity.Mode.TELEPORTATION) {
				entity.hurt(world.damageSources().hotFloor(), 1);
			}
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.WATERLOGGED, BWProperties.LEVEL, BlockStateProperties.LIT);
	}
}
