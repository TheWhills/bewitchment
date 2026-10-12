/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.common.entity.living.util.BWTameableEntity;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class SnakeEntity extends BWTameableEntity {
	public int tongueFlick = 0, attackTick = 0;

	public SnakeEntity(EntityType<? extends TamableAnimal> type, Level world) {
		super(type, world);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8).add(Attributes.ATTACK_DAMAGE, 1).add(Attributes.MOVEMENT_SPEED, 0.3);
	}

	@Override
	public void tick() {
		super.tick();
		if (tongueFlick > 0) {
			tongueFlick--;
		}
		if (tickCount % 40 == 0 && random.nextFloat() < 0.25f) {
			tongueFlick = 10;
		}
		if (attackTick > 0) {
			attackTick--;
			tongueFlick = 0;
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
	protected boolean isTamingItem(ItemStack stack) {
		return stack.getItem() == Items.RABBIT;
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
		SnakeEntity child = BWEntityTypes.SNAKE.create(world);
		if (child != null) {
			child.finalizeSpawn(world, world.getCurrentDifficultyAt(blockPosition()), MobSpawnType.BREEDING, null, null);
			UUID owner = getOwnerUUID();
			if (owner != null) {
				child.setOwnerUUID(owner);
				child.setTame(true);
			}
			if (entity instanceof SnakeEntity && random.nextFloat() < 95 / 100f) {
				child.entityData.set(VARIANT, random.nextBoolean() ? entityData.get(VARIANT) : entity.getEntityData().get(VARIANT));
			}
		}
		return child;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return BWSoundEvents.ENTITY_SNAKE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_SNAKE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_SNAKE_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return stack.getItem() == Items.CHICKEN;
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		boolean flag = super.doHurtTarget(target);
		if (flag) {
			toggleAttack(false);
			if (target instanceof LivingEntity livingEntity) {
				livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 100));
			}
		}
		return flag;
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
	public void handleEntityEvent(byte id) {
		if (id == 4) {
			attackTick = 11;
		}
		if (id == 5) {
			attackTick = 2;
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
		goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.25f) {
			@Override
			public void start() {
				super.start();
				toggleAttack(true);
			}
		});
		goalSelector.addGoal(3, new MeleeAttackGoal(this, 1, true));
		goalSelector.addGoal(4, new FollowOwnerGoal(this, 1, 10, 2, false));
		goalSelector.addGoal(5, new BreedGoal(this, 1));
		goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		targetSelector.addGoal(0, new OwnerHurtByTargetGoal(this));
		targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
		targetSelector.addGoal(2, new HurtByTargetGoal(this));
		targetSelector.addGoal(3, new NonTameRandomTargetGoal<>(this, LivingEntity.class, false, entity -> entity instanceof Chicken || entity instanceof Rabbit));
	}

	public void toggleAttack(boolean attacking) {
		if (attacking) {
			attackTick = 11;
			level().broadcastEntityEvent(this, (byte) 4);
		} else {
			attackTick = 2;
			level().broadcastEntityEvent(this, (byte) 5);
		}
	}
}
