/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity.living;

import com.google.common.collect.Sets;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.packet.BWNetwork;
import moriyashiine.bewitchment.api.entity.Pledgeable;
import moriyashiine.bewitchment.client.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.item.HornedSpearItem;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.packet.TransformationAbilityPacket;
import moriyashiine.bewitchment.common.registry.*;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class HerneEntity extends BWHostileEntity implements Pledgeable {
	private final ServerBossEvent bossBar;

	private final Set<UUID> pledgedPlayerUUIDS = new HashSet<>();
	private int timeSinceLastAttack = 0;

	public HerneEntity(EntityType<? extends Monster> entityType, Level world) {
		super(entityType, world);
		bossBar = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
		setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0);
		setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0);
		xpReward = 75;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 500).add(Attributes.ATTACK_DAMAGE, 24).add(Attributes.ARMOR, 10).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.KNOCKBACK_RESISTANCE, 1).add(Attributes.FOLLOW_RANGE, 32);
	}

	@Override
	public void tick() {
		super.tick();
		if (!level().isClientSide) {
			bossBar.setProgress(getHealth() / getMaxHealth());
			LivingEntity target = getTarget();
			int timer = tickCount + getId();
			if (timer % 10 == 0) {
				heal(1);
			}
			if (target != null) {
				timeSinceLastAttack++;
				if (timeSinceLastAttack >= 600) {
					BWUtil.teleport(this, target.getX(), target.getY(), target.getZ(), true);
					timeSinceLastAttack = 0;
				}
				lookAt(target, 360, 360);
				if (timer % 80 == 0) {
					HornedSpearItem.spawnEntity(level(), this, new ItemStack(BWObjects.HORNED_SPEAR.get()));
					swing(InteractionHand.MAIN_HAND);
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
		return BWPledges.HERNE;
	}

	@Override
	public Collection<UUID> getPledgedPlayerUUIDs() {
		return pledgedPlayerUUIDS;
	}

	@Override
	public EntityType<?> getMinionType() {
		return BWEntityTypes.WEREWOLF.get();
	}

	@Override
	public Collection<MobEffectInstance> getMinionBuffs() {
		return Sets.newHashSet(new MobEffectInstance(MobEffects.DAMAGE_BOOST, Integer.MAX_VALUE, 1), new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1), new MobEffectInstance(BWStatusEffects.HARDENING.get(), Integer.MAX_VALUE, 1));
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
		return BWSoundEvents.ENTITY_HERNE_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_HERNE_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_HERNE_DEATH.get();
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (isAlive() && getTarget() == null && BewitchmentAPI.isWerewolf(player, true)) {
			ItemStack stack = player.getItemInHand(hand);
			if (stack.getItem() == BWObjects.ACONITE.get()) {
				if (player instanceof ServerPlayer serverPlayer) {
					if (!player.isCreative()) {
						stack.shrink(1);
					}
					BWNetwork.sendToTrackingAndSelf(player, new SpawnSmokeParticlesPacket(player));
					level().playSound(null, blockPosition(), BWSoundEvents.ENTITY_GENERIC_PLING.get(), player.getSoundSource(), 1, 1);
					BWComponents.TRANSFORMATION_COMPONENT.maybeGet(player).ifPresent(transformationComponent -> {
						if (transformationComponent.isAlternateForm()) {
							TransformationAbilityPacket.useAbility(player, true);
						}
						transformationComponent.getTransformation().onRemoved(player);
						transformationComponent.setTransformation(BWTransformations.HUMAN.get());
						transformationComponent.getTransformation().onAdded(player);
					});
				}
				return InteractionResult.sidedSuccess(level().isClientSide);
			}
		}
		return super.mobInteract(player, hand);
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
		if (flag && target instanceof LivingEntity livingEntity) {
			livingEntity.addEffect(new MobEffectInstance(BWStatusEffects.MORTAL_COIL.get(), 1200));
			target.setSecondsOnFire(16);
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
	public void setTarget(@Nullable LivingEntity target) {
		if (level().isDay()) {
			target = null;
		}
		super.setTarget(target);
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
}
