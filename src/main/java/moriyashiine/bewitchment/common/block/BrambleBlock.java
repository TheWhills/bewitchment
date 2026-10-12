/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.block;

import moriyashiine.bewitchment.common.component.entity.TeleportTimerComponent;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.misc.interfaces.EntityShapeContextAdditionAccessor;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWProperties;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BrambleBlock extends SugarCaneBlock {

	public BrambleBlock(Properties settings) {
		super(settings);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (this == BWObjects.THICK_BRAMBLE.get() && context instanceof EntityShapeContextAdditionAccessor accessor) {
			Entity entity = accessor.bw_getEntity();
			if (entity instanceof LivingEntity && !entity.isShiftKeyDown()) {
				return Shapes.block();
			}
		}
		return super.getCollisionShape(state, world, pos, context);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		BlockState downState = world.getBlockState(pos.below());
		return downState.is(this) || downState.is(BlockTags.DIRT) || downState.is(BlockTags.SAND);
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		if (this == BWObjects.GLOWING_BRAMBLE.get() && !world.isClientSide && state.getBlock() != oldState.getBlock()) {
			BWWorldState worldState = BWWorldState.get(world);
			worldState.glowingBrambles.add(pos.asLong());
			worldState.setDirty();
		}
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		if (this == BWObjects.GLOWING_BRAMBLE.get() && !world.isClientSide && state.getBlock() != newState.getBlock()) {
			BWWorldState worldState = BWWorldState.get(world);
			for (int i = worldState.glowingBrambles.size() - 1; i >= 0; i--) {
				if (worldState.glowingBrambles.get(i) == pos.asLong()) {
					worldState.glowingBrambles.remove(i);
					worldState.setDirty();
				}
			}
		}
		super.onRemove(state, world, pos, newState, moved);
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (!world.isClientSide && entity instanceof LivingEntity) {
			if (this == BWObjects.ENDER_BRAMBLE.get() && !entity.isPassenger()) {
				boolean teleport = true;
				if (entity instanceof Player player) {
					TeleportTimerComponent teleportTimerComponent = BWComponents.TELEPORT_TIMER_COMPONENT.get(player);
					if (teleportTimerComponent.getTeleportTimer() > 0) {
						teleport = false;
					} else {
						teleportTimerComponent.setTeleportTimer(20);
					}
				}
				if (teleport) {
					BWUtil.attemptTeleport(entity, entity.blockPosition(), 64, true);
				}
			}
			if (this == BWObjects.SCORCHED_BRAMBLE.get()) {
				entity.setSecondsOnFire(10);
			}
		}
		if (this == BWObjects.THICK_BRAMBLE.get() && !entity.isShiftKeyDown()) {
			entity.makeStuckInBlock(state, new Vec3(0.25, 0.5, 0.25));
		}
		if (this == BWObjects.FLEETING_BRAMBLE.get()) {
			if (entity.isCrouching()) {
				entity.setDeltaMovement(entity.getDeltaMovement().multiply(1, 0.5, 1));
				entity.fallDistance = 0;
			} else {
				entity.push(0, 0.4, 0);
			}
		}
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		ParticleType<?> particleType = null;
		if (this == BWObjects.ENDER_BRAMBLE.get()) {
			particleType = ParticleTypes.PORTAL;
		} else if (this == BWObjects.SCORCHED_BRAMBLE.get()) {
			particleType = ParticleTypes.FLAME;
		} else if (this == BWObjects.FLEETING_BRAMBLE.get()) {
			particleType = ParticleTypes.CLOUD;
		}
		if (particleType != null) {
			world.addParticle((ParticleOptions) particleType, pos.getX() + 0.5 + Mth.nextDouble(random, -0.5, 0.5), pos.getY() + 0.5 + Mth.nextDouble(random, -0.5, 0.5), pos.getZ() + 0.5 + Mth.nextDouble(random, -0.5, 0.5), 0, 0, 0);
		}
	}

	public static class Fruiting extends BrambleBlock {
		public Fruiting(Properties settings) {
			super(settings);
			registerDefaultState(defaultBlockState().setValue(BWProperties.HAS_FRUIT, false));
		}

		@Override
		public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
			boolean client = world.isClientSide;
			if (state.getValue(BWProperties.HAS_FRUIT)) {
				if (!client) {
					world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LEVEL, 0).setValue(BWProperties.HAS_FRUIT, false));
					world.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1, 1);
					ItemStack stack = new ItemStack(BWObjects.WITCHBERRY.get());
					if (!player.getInventory().add(stack)) {
						player.spawnAtLocation(stack);
					}
				}
				return InteractionResult.sidedSuccess(client);
			} else {
				ItemStack stack = player.getItemInHand(hand);
				if (stack.getItem() instanceof BoneMealItem) {
					if (!world.isClientSide) {
						if (!player.isCreative()) {
							stack.shrink(1);
						}
						int level = Math.min(8, state.getValue(BlockStateProperties.LEVEL) + world.random.nextInt(3) + 2);
						world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LEVEL, level).setValue(BWProperties.HAS_FRUIT, level == 8));
					}
					return InteractionResult.sidedSuccess(client);
				}
			}
			return super.use(state, world, pos, player, hand, hit);
		}

		@Override
		public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
			super.randomTick(state, world, pos, random);
			state = world.getBlockState(pos);
			if (!state.getValue(BWProperties.HAS_FRUIT)) {
				int level = state.getValue(BlockStateProperties.LEVEL);
				if (level == 15) {
					world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LEVEL, 0).setValue(BWProperties.HAS_FRUIT, true));
				} else {
					world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LEVEL, level + 1));
				}
			}
		}

		@Override
		protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
			super.createBlockStateDefinition(builder.add(BlockStateProperties.LEVEL, BWProperties.HAS_FRUIT));
		}
	}
}
