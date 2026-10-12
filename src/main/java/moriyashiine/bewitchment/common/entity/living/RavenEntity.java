/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.common.entity.living.util.BWTameableEntity;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class RavenEntity extends BWTameableEntity {
	public RavenEntity(EntityType<? extends TamableAnimal> type, Level world) {
		super(type, world);
		moveControl = new FlyingMoveControl(this, 180, false);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8).add(Attributes.ATTACK_DAMAGE, 1).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.FLYING_SPEED, 0.6);
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
	protected boolean isTamingItem(ItemStack stack) {
		return stack.getItem() == Items.GOLD_NUGGET || stack.is(BWTags.SILVER_NUGGETS);
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
		RavenEntity child = BWEntityTypes.RAVEN.create(world);
		if (child != null) {
			child.finalizeSpawn(world, world.getCurrentDifficultyAt(blockPosition()), MobSpawnType.BREEDING, null, null);
			UUID owner = getOwnerUUID();
			if (owner != null) {
				child.setOwnerUUID(owner);
				child.setTame(true);
			}
			if (entity instanceof RavenEntity && random.nextFloat() < 95 / 100f) {
				child.entityData.set(VARIANT, random.nextBoolean() ? entityData.get(VARIANT) : entity.getEntityData().get(VARIANT));
			}
		}
		return child;
	}

	@Override
	protected PathNavigation createNavigation(Level world) {
		FlyingPathNavigation birdNavigation = new FlyingPathNavigation(this, world);
		birdNavigation.setCanOpenDoors(false);
		birdNavigation.setCanFloat(true);
		birdNavigation.setCanPassDoors(true);
		return birdNavigation;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return BWSoundEvents.ENTITY_RAVEN_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_RAVEN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_RAVEN_DEATH;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return stack.getItem() == Items.WHEAT_SEEDS;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		playSound(SoundEvents.PARROT_STEP, 0.15f, 1);
	}

	@Override
	protected void onFlap() {
		playSound(BWSoundEvents.ENTITY_RAVEN_FLY, 0.15f, 1);
	}

	@Override
	protected boolean isFlapping() {
		return true;
	}

	@Override
	public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
		return false;
	}

	@Override
	protected void checkFallDamage(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
	}

	@Override
	public void setTame(boolean tamed) {
		super.setTame(tamed);
		AttributeInstance maxHealth = getAttribute(Attributes.MAX_HEALTH);
		AttributeInstance attackDamage = getAttribute(Attributes.ATTACK_DAMAGE);
		if (tamed) {
			maxHealth.setBaseValue(20);
			attackDamage.setBaseValue(3);
			setHealth(getMaxHealth());
		} else {
			maxHealth.setBaseValue(8);
			attackDamage.setBaseValue(1);
		}
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
		goalSelector.addGoal(2, new MeleeAttackGoal(this, 1, true));
		goalSelector.addGoal(3, new FollowOwnerGoal(this, 1, 10, 2, false));
		goalSelector.addGoal(4, new BreedGoal(this, 1));
		goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		targetSelector.addGoal(0, new OwnerHurtByTargetGoal(this));
		targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
		targetSelector.addGoal(2, new HurtByTargetGoal(this).setAlertOthers());
	}
}
