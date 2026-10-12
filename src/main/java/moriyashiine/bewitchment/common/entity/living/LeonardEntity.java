/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity.living;

import com.google.common.collect.Sets;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.entity.Pledgeable;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWPledges;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LeonardEntity extends BWHostileEntity implements Pledgeable {
	private final ServerBossEvent bossBar;

	private final Set<UUID> pledgedPlayerUUIDS = new HashSet<>();
	private int timeSinceLastAttack = 0;

	public LeonardEntity(EntityType<? extends Monster> entityType, Level world) {
		super(entityType, world);
		bossBar = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
		setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0);
		setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0);
		xpReward = 50;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 375).add(Attributes.ATTACK_DAMAGE, 12).add(Attributes.ARMOR, 6).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.KNOCKBACK_RESISTANCE, 0.75).add(Attributes.FOLLOW_RANGE, 32);
	}

	@Override
	public void tick() {
		super.tick();
		if (!level().isClientSide) {
			bossBar.setProgress(getHealth() / getMaxHealth());
			LivingEntity target = getTarget();
			int timer = tickCount + getId();
			if (timer % 300 == 0 && getHealth() < getMaxHealth() && (target == null || distanceTo(target) < 4)) {
				spawnPotion(blockPosition(), Potions.AWKWARD);
				addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 1));
			}
			if (target != null) {
				timeSinceLastAttack++;
				if (timeSinceLastAttack >= 600) {
					BWUtil.teleport(this, target.getX(), target.getY(), target.getZ(), true);
					timeSinceLastAttack = 0;
				}
				lookAt(target, 360, 360);
				if (timer % 40 == 0) {
					spawnPotion(target.blockPosition(), target.isInvertedHealAndHarm() ? Potions.STRONG_HEALING : Potions.STRONG_HARMING);
				}
				if (timer % 600 == 0) {
					summonMinions(this);
				}
			} else {
				if (getY() > -64) {
					heal(8);
				}
				timeSinceLastAttack = 0;
			}
		}
	}

	@Override
	public String getPledgeID() {
		return BWPledges.LEONARD;
	}

	@Override
	public Collection<UUID> getPledgedPlayerUUIDs() {
		return pledgedPlayerUUIDS;
	}

	@Override
	public EntityType<?> getMinionType() {
		return EntityType.WITCH;
	}

	@Override
	public Collection<MobEffectInstance> getMinionBuffs() {
		return Sets.newHashSet(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, Integer.MAX_VALUE), new MobEffectInstance(BWStatusEffects.HARDENING, Integer.MAX_VALUE, 1));
	}

	@Override
	public int getTimeSinceLastAttack() {
		return timeSinceLastAttack;
	}

	@Override
	public void setTimeSinceLastAttack(int timeSinceLastAttack) {
		this.timeSinceLastAttack = timeSinceLastAttack;
	}

	@Override
	protected boolean hasShiny() {
		return false;
	}

	@Override
	public int getVariants() {
		return 1;
	}

	@Override
	public MobType getMobType() {
		return BewitchmentAPI.DEMON;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return BWSoundEvents.ENTITY_LEONARD_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_LEONARD_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_LEONARD_DEATH;
	}

	@Override
	public boolean canBeLeashed(Player player) {
		return false;
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	@Override
	public boolean canBeAffected(MobEffectInstance effect) {
		return effect.getEffect().getCategory() == MobEffectCategory.BENEFICIAL;
	}

	@Override
	public boolean isAffectedByPotions() {
		return false;
	}

	@Override
	public boolean requiresCustomPersistence() {
		return true;
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		boolean flag = super.doHurtTarget(target);
		if (flag && target instanceof LivingEntity) {
			target.setSecondsOnFire(8);
			target.push(0, 0.2, 0);
			swing(InteractionHand.MAIN_HAND);
		}
		return flag;
	}

	@Override
	public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
		return false;
	}

	@Override
	protected void checkFallDamage(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
	}

	@Override
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		bossBar.setName(getDisplayName());
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		bossBar.addPlayer(player);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		bossBar.removePlayer(player);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		if (hasCustomName()) {
			bossBar.setName(getDisplayName());
		}
		fromNbtPledgeable(nbt);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		toNbtPledgeable(nbt);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, new MeleeAttackGoal(this, 1, true));
		goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1));
		goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		targetSelector.addGoal(0, new HurtByTargetGoal(this));
		targetSelector.addGoal(1, BWUtil.createGenericPledgeableTargetGoal(this));
	}

	private void spawnPotion(BlockPos target, Potion potionType) {
		ThrownPotion potion = new ThrownPotion(level(), this);
		potion.setItem(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), potionType));
		potion.absMoveTo(potion.getX(), getY(0.5), potion.getZ());
		double targetX = target.getX() - getX();
		double targetY = target.getY() - 1 - getY();
		double targetZ = target.getZ() - getZ();
		potion.shoot(targetX, targetY + (Mth.sqrt((float) (targetX * targetX + targetZ * targetZ)) * 0.4), targetZ, 1, 0);
		level().playSound(null, blockPosition(), SoundEvents.SPLASH_POTION_THROW, getSoundSource(), getSoundVolume(), getVoicePitch());
		level().addFreshEntity(potion);
		swing(InteractionHand.MAIN_HAND);
	}
}
