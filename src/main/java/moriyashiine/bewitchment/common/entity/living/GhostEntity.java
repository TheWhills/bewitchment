/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.api.block.CandelabraBlock;
import moriyashiine.bewitchment.client.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class GhostEntity extends BWHostileEntity {
	public static final EntityDataAccessor<Boolean> HAS_TARGET = SynchedEntityData.defineId(GhostEntity.class, EntityDataSerializers.BOOLEAN);

	public GhostEntity(EntityType<? extends Monster> entityType, Level world) {
		super(entityType, world);
		moveControl = new GhostMoveControl(this);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20).add(Attributes.FLYING_SPEED, 0.25).add(Attributes.KNOCKBACK_RESISTANCE, 1).add(Attributes.FOLLOW_RANGE, 8);
	}

	public static boolean canSpawn(EntityType<GhostEntity> type, ServerLevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
		return findPos(world, pos) == null && Monster.checkMonsterSpawnRules(type, world, spawnReason, pos, random);
	}

	@Override
	public void tick() {
		noPhysics = true;
		super.tick();
		noPhysics = false;
		setNoGravity(true);
		if (!level().isClientSide) {
			if (tickCount % 20 == 0 && getTarget() != null) {
				int type = entityData.get(VARIANT);
				if (type == 0) {
					type = random.nextInt(getVariants() - 1) + 1;
				}
				getTarget().addEffect(getEffect(type, 100));
			}
			if (!hasCustomName() && level().isDay() && !level().isRaining() && level().canSeeSkyFromBelowWater(blockPosition())) {
				PlayerLookup.tracking(this).forEach(trackingPlayer -> SpawnSmokeParticlesPacket.send(trackingPlayer, this));
				remove(RemovalReason.DISCARDED);
			}
		}
	}

	@Override
	protected boolean hasShiny() {
		return true;
	}

	@Override
	public int getVariants() {
		return 5;
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return BWSoundEvents.ENTITY_GHOST_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_GHOST_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_GHOST_DEATH;
	}

	@Override
	public float getLightLevelDependentMagicValue() {
		return 1;
	}

	@Override
	public boolean canBeAffected(MobEffectInstance effect) {
		return false;
	}

	@Override
	public boolean isInWater() {
		return false;
	}

	@Override
	public boolean isInLava() {
		return false;
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	@Override
	public void setTarget(@Nullable LivingEntity target) {
		if (level() instanceof ServerLevelAccessor serverWorldAccess && findPos(serverWorldAccess, blockPosition()) != null) {
			target = null;
		}
		super.setTarget(target);
		if (!level().isClientSide) {
			entityData.set(HAS_TARGET, target != null);
		}
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(HAS_TARGET, false);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, new FlyRandomlyGoal());
		goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new HurtByTargetGoal(this));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static MobEffectInstance getEffect(int type, int duration) {
		return new MobEffectInstance(type == 1 ? MobEffects.HUNGER : type == 2 ? MobEffects.MOVEMENT_SLOWDOWN : type == 3 ? BWStatusEffects.CORROSION : BWStatusEffects.SINKING, duration);
	}

	private class GhostMoveControl extends MoveControl {
		public GhostMoveControl(Mob entity) {
			super(entity);
		}

		@Override
		public void tick() {
			if (operation == MoveControl.Operation.MOVE_TO && (getTarget() == null || (!hasLineOfSight(getTarget())))) {
				Vec3 targetPosition = new Vec3(wantedX - getX(), wantedY - getY(), wantedZ - getZ());
				double distance = targetPosition.length();
				if (distance < getBoundingBox().getSize()) {
					operation = MoveControl.Operation.WAIT;
					setDeltaMovement(getDeltaMovement().scale(0.5));
				} else {
					setDeltaMovement(getDeltaMovement().add(targetPosition.scale(speedModifier * 0.05 / distance)));
					if (getTarget() == null) {
						Vec3 velocity = getDeltaMovement();
						//noinspection SuspiciousNameCombination
						setYRot(-((float) Mth.atan2(velocity.x, velocity.z)) * 57);
					} else {
						setYRot(-((float) Mth.atan2(getTarget().getX() - getX(), getTarget().getZ() - getZ())) * 57);
					}
					yBodyRot = getYRot();
				}
			}
		}
	}

	private class FlyRandomlyGoal extends Goal {
		public FlyRandomlyGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			if (getTarget() == null || !hasLineOfSight(getTarget())) {
				MoveControl moveControl = getMoveControl();
				if (!moveControl.hasWanted()) {
					return true;
				} else {
					double distanceX = moveControl.getWantedX() - getX();
					double distanceY = moveControl.getWantedY() - getY();
					double distanceZ = moveControl.getWantedZ() - getZ();
					double fin = distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;
					return fin < 1 || fin > 3600;
				}
			}
			return false;
		}

		@Override
		public boolean canContinueToUse() {
			return false;
		}

		@Override
		public void start() {
			BlockPos target = findTarget(new BlockPos.MutableBlockPos(getX() + Mth.nextDouble(random, -8, 8), getY() + Mth.nextDouble(random, -4, 4), getZ() + Mth.nextDouble(random, -8, 8)), 0);
			if (target != null) {
				getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 0.2);
				lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(target.getX(), target.getY(), target.getZ()));
			}
		}

		private BlockPos findTarget(BlockPos.MutableBlockPos target, int tries) {
			if (tries <= 8) {
				while (isInsideBuildLimit(level(), target) && level().getBlockState(target).isSolid()) {
					target.set(target.getX(), target.getY() + 1, target.getZ());
				}
				while (isInsideBuildLimit(level(), target) && !level().getBlockState(target).isSolid()) {
					target.set(target.getX(), target.getY() - 1, target.getZ());
				}
				target.set(target.getX(), target.getY() + random.nextInt(8), target.getZ());
				Tuple<BlockPos, Integer> validSpot = findPos((ServerLevelAccessor) level(), target);
				if (validSpot != null) {
					int radius = validSpot.getB();
					return findTarget(target.set(validSpot.getA().getX() + Mth.nextDouble(random, -radius, radius), validSpot.getA().getY() + Mth.nextDouble(random, -radius, radius), validSpot.getA().getZ() + Mth.nextDouble(random, -radius, radius)), ++tries);
				}
				return target.immutable();
			}
			return null;
		}

		private static boolean isInsideBuildLimit(Level world, BlockPos pos) {
			return pos.getY() >= world.getMinBuildHeight() && pos.getY() <= world.getMaxBuildHeight();
		}
	}

	private static Tuple<BlockPos, Integer> findPos(ServerLevelAccessor world, BlockPos pos) {
		BWWorldState worldState = BWWorldState.get(world.getLevel());
		for (Long longPos : worldState.potentialCandelabras) {
			BlockPos candelabraPos = BlockPos.of(longPos);
			double distance = Math.sqrt(candelabraPos.distSqr(pos));
			if (distance <= Byte.MAX_VALUE) {
				int radius = -1;
				BlockEntity blockEntity = world.getBlockEntity(candelabraPos);
				BlockState state = world.getBlockState(candelabraPos);
				if (blockEntity instanceof WitchAltarBlockEntity witchAltar) {
					for (int i = 0; i < witchAltar.getContainerSize(); i++) {
						Block block = Block.byItem(witchAltar.getItem(i).getItem());
						if (block instanceof CandelabraBlock candelabraBlock) {
							radius = candelabraBlock.repellentRadius;
							break;
						}
					}
				} else if (state.getBlock() instanceof CandelabraBlock candelabra && state.getValue(BlockStateProperties.LIT)) {
					radius = candelabra.repellentRadius;
				}
				if (distance <= radius) {
					return new Tuple<>(candelabraPos, radius);
				}
			}
		}
		return null;
	}
}
