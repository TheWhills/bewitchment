/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.block.WitchAltarBlock;
import moriyashiine.bewitchment.api.component.FortuneComponent;
import moriyashiine.bewitchment.api.registry.Fortune;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import moriyashiine.bewitchment.common.block.entity.interfaces.SigilHolder;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.*;
import moriyashiine.bewitchment.common.world.BWUniversalWorldState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
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
import org.joml.Vector3f;

@SuppressWarnings("ConstantConditions")
public class CrystalBallBlock extends Block implements SimpleWaterloggedBlock {
	private static final VoxelShape SHAPE = Shapes.or(box(5, 11, 5, 11, 12, 11), box(3.5, 2.5, 4, 4.5, 3.5, 12), box(5, 4, 3, 11, 10, 4), box(3, 4, 5, 4, 10, 11), box(4, 3, 4, 12, 11, 12), box(12, 4, 5, 13, 10, 11), box(3.6, 0, 11.4, 4.6, 3, 12.4), box(11.4, 0, 11.4, 12.4, 3, 12.4), box(3.5, 2.5, 11.5, 12.5, 3.5, 12.5), box(11.5, 2.5, 4, 12.5, 3.5, 12), box(11.4, 0, 3.6, 12.4, 3, 4.6), box(5, 2, 5, 11, 3, 11), box(5, 4, 12, 11, 10, 13), box(3.5, 2.5, 3.5, 12.5, 3.5, 4.5), box(3.6, 0, 3.6, 4.6, 3, 4.6));

	public CrystalBallBlock(Properties settings) {
		super(settings);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		boolean client = world.isClientSide;
		if (client) {
			for (int i = 0; i < 10; i++) {
				world.addParticle(new DustParticleOptions(new Vector3f(1, 1, 1), 1), pos.getX() + world.random.nextFloat(), pos.getY() + world.random.nextFloat(), pos.getZ() + world.random.nextFloat(), 0, 0, 0);
			}
		} else {
			SoundEvent sound = BWSoundEvents.BLOCK_CRYSTAL_BALL_FAIL.get();
			BlockPos nearestAltarPos = WitchAltarBlock.getClosestAltarPos(world, pos);
			if (nearestAltarPos != null && ((WitchAltarBlockEntity) world.getBlockEntity(nearestAltarPos)).drain(500, false)) {
				ItemStack stack = player.getItemInHand(hand);
				if (stack.getItem() instanceof TaglockItem && TaglockItem.isTaglockFromPlayer(stack) && !stack.getTag().contains("UsedForScrying")) {
					if (BewitchmentAPI.getTaglockOwner(world, stack) instanceof Player owner) {
						boolean failed = false;
						BlockPos sigilPos = BWUtil.getClosestBlockPos(owner.blockPosition(), 16, currentPos -> world.getBlockEntity(currentPos) instanceof SigilHolder sigilHolder && sigilHolder.getSigil() == BWSigils.SHADOWS.get());
						if (sigilPos == null) {
							sigilPos = BWUtil.getClosestBlockPos(pos, 16, currentPos -> world.getBlockEntity(currentPos) instanceof SigilHolder sigilHolder && sigilHolder.getSigil() == BWSigils.SHADOWS.get());
						}
						if (sigilPos != null) {
							BlockEntity blockEntity = world.getBlockEntity(sigilPos);
							SigilHolder sigilHolder = (SigilHolder) blockEntity;
							if (sigilHolder.test(player)) {
								sigilHolder.setUses(sigilHolder.getUses() - 1);
								blockEntity.setChanged();
								failed = true;
							}
						}
						ItemStack newTaglock = new ItemStack(BWObjects.TAGLOCK.get());
						CompoundTag taglockCompound = newTaglock.getOrCreateTag().merge(stack.getTag());
						taglockCompound.putBoolean("UsedForScrying", true);
						if (!failed) {
							taglockCompound.putLong("LocationPos", owner.blockPosition().asLong());
							taglockCompound.putString("LocationWorld", world.dimension().location().toString());
							taglockCompound.putInt("Level", owner.experienceLevel);
							taglockCompound.put("Curses", BWComponents.CURSES_COMPONENT.get(owner).toNbtCurse());
							taglockCompound.put("Contracts", BWComponents.CONTRACTS_COMPONENT.get(owner).toNbtContract());
							taglockCompound.putString("Transformation", "transformation." + BWRegistries.TRANSFORMATION.getKey(BWComponents.TRANSFORMATION_COMPONENT.get(owner).getTransformation()).toString().replace(":", "."));
							BWUniversalWorldState universalWorldState = BWUniversalWorldState.get(world);
							String familiar = "none";
							for (int i = 0; i < universalWorldState.familiars.size(); i++) {
								if (universalWorldState.familiars.get(i).getA().equals(owner.getUUID())) {
									familiar = universalWorldState.familiars.get(i).getB().getString("id");
									break;
								}
							}
							taglockCompound.putString("Familiar", familiar);
							taglockCompound.putString("Pledge", BWComponents.PLEDGE_COMPONENT.get(owner).getPledge());
							sound = BWSoundEvents.BLOCK_CRYSTAL_BALL_FIRE.get();
						} else {
							taglockCompound.putBoolean("Failed", true);
							player.displayClientMessage(Component.translatable(Bewitchment.MOD_ID + ".message.blocked_by_shadows"), true);
						}
						BWUtil.addItemToInventoryAndConsume(player, hand, newTaglock);
					} else {
						player.displayClientMessage(Component.translatable(Bewitchment.MOD_ID + ".message.invalid_entity"), true);
					}
				} else {
					FortuneComponent fortuneComponent = BWComponents.FORTUNE_COMPONENT.get(player);
					if (fortuneComponent.getFortune() == null) {
						sound = BWSoundEvents.BLOCK_CRYSTAL_BALL_FIRE.get();
						Fortune fortune = BWRegistries.FORTUNE.byId(world.random.nextInt(BWRegistries.FORTUNE.size()));
						if (BWComponents.CURSES_COMPONENT.get(player).hasCurse(BWCurses.UNLUCKY.get())) {
							while (fortune.positive) {
								fortune = BWRegistries.FORTUNE.byId(world.random.nextInt(BWRegistries.FORTUNE.size()));
							}
						}
						fortuneComponent.setFortune(new Fortune.Instance(fortune, world.random.nextInt(120000)));
						player.displayClientMessage(Component.translatable("fortune." + BWRegistries.FORTUNE.getKey(fortune).toString().replace(":", ".")), true);

					} else {
						player.displayClientMessage(Component.translatable(Bewitchment.MOD_ID + ".message.has_fortune"), true);
					}
				}
			} else {
				player.displayClientMessage(Component.translatable(Bewitchment.MOD_ID + ".message.insufficent_altar_power"), true);
			}
			world.playSound(null, pos, sound, SoundSource.BLOCKS, 1, 1);
		}
		return InteractionResult.sidedSuccess(client);
	}

	@SuppressWarnings("ConstantConditions")
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return super.getStateForPlacement(ctx).setValue(BlockStateProperties.WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER);
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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.WATERLOGGED);
	}
}
