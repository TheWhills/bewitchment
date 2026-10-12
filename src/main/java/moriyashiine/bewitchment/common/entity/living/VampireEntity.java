/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class VampireEntity extends BWHostileEntity {
	public static final EntityDataAccessor<Boolean> HAS_TARGET = SynchedEntityData.defineId(VampireEntity.class, EntityDataSerializers.BOOLEAN);

	private boolean onFireFromSun = false;

	public VampireEntity(EntityType<? extends Monster> entityType, Level world) {
		super(entityType, world);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20).add(Attributes.ATTACK_DAMAGE, 5).add(Attributes.ARMOR, 8).add(Attributes.MOVEMENT_SPEED, 0.35);
	}

	public static boolean canSpawn(EntityType<VampireEntity> type, ServerLevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
		return world.getLevel().isNight() && BewitchmentAPI.getMoonPhase(world.getLevel()) == 4 && Monster.checkMonsterSpawnRules(type, world, spawnReason, pos, random);
	}

	@Override
	public void tick() {
		super.tick();
		if (!level().isClientSide) {
			BWComponents.BLOOD_COMPONENT.maybeGet(this).ifPresent(bloodComponent -> {
				if (getHealth() < getMaxHealth() && (tickCount + getId()) % 40 == 0 && bloodComponent.getBlood() > 0) {
					heal(1);
					if (random.nextFloat() < 1 / 4f) {
						bloodComponent.drainBlood(1, false);
					}
				}
			});
			if (!hasCustomName() && level().isDay() && !level().isRaining() && level().canSeeSky(blockPosition())) {
				setSecondsOnFire(8);
				onFireFromSun = true;
			}
		}
	}

	@Override
	protected boolean hasShiny() {
		return true;
	}

	@Override
	public int getVariants() {
		return 2;
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return BWSoundEvents.ENTITY_VAMPIRE_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_VAMPIRE_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_VAMPIRE_DEATH.get();
	}

	@Override
	protected boolean shouldDropLoot() {
		return super.shouldDropLoot() && !onFireFromSun;
	}

	@Override
	public void setTarget(@Nullable LivingEntity target) {
		super.setTarget(target);
		entityData.set(HAS_TARGET, target != null);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		onFireFromSun = nbt.getBoolean("OnFireFromSun");
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putBoolean("OnFireFromSun", onFireFromSun);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(HAS_TARGET, false);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, new MeleeAttackGoal(this, 1, true));
		goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1));
		goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		targetSelector.addGoal(0, new HurtByTargetGoal(this));
		targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> entity instanceof Player || entity instanceof AbstractVillager || entity.getMobType() == MobType.ILLAGER));
	}
}
