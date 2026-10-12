/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.common.entity.living.util.BWTameableEntity;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class ToadEntity extends BWTameableEntity {
	public boolean isFromWednesdayRitual = false;

	public ToadEntity(EntityType<? extends TamableAnimal> type, Level world) {
		super(type, world);
		setPathfindingMalus(BlockPathTypes.WATER, -1);
		setPathfindingMalus(BlockPathTypes.WATER_BORDER, -1);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8).add(Attributes.MOVEMENT_SPEED, 0.6);
	}

	@Override
	public void tick() {
		super.tick();
		if (isPathFinding() && onGround()) {
			jumpFromGround();
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
		return stack.getItem() == Items.FERMENTED_SPIDER_EYE;
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
		ToadEntity child = BWEntityTypes.TOAD.get().create(world);
		if (child != null) {
			child.finalizeSpawn(world, world.getCurrentDifficultyAt(blockPosition()), MobSpawnType.BREEDING, null, null);
			UUID owner = getOwnerUUID();
			if (owner != null) {
				child.setOwnerUUID(owner);
				child.setTame(true);
			}
			if (entity instanceof ToadEntity && random.nextFloat() < 95 / 100f) {
				child.entityData.set(VARIANT, random.nextBoolean() ? entityData.get(VARIANT) : entity.getEntityData().get(VARIANT));
			}
		}
		return child;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return BWSoundEvents.ENTITY_TOAD_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_TOAD_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_TOAD_DEATH.get();
	}

	@Override
	protected boolean shouldDropLoot() {
		return super.shouldDropLoot() && !isFromWednesdayRitual;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return stack.getItem() == Items.SPIDER_EYE;
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
		if (tamed) {
			maxHealth.setBaseValue(20);
			setHealth(getMaxHealth());
		} else {
			maxHealth.setBaseValue(8);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		isFromWednesdayRitual = nbt.getBoolean("IsFromWednesdayRitual");
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putBoolean("IsFromWednesdayRitual", isFromWednesdayRitual);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
		goalSelector.addGoal(2, new FollowOwnerGoal(this, 1, 10, 2, false));
		goalSelector.addGoal(3, new BreedGoal(this, 1));
		goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1));
		goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(5, new RandomLookAroundGoal(this));
	}
}
