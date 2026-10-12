/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity.living;

import com.google.common.collect.Sets;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.entity.Pledgeable;
import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.client.packet.SyncContractsPacket;
import moriyashiine.bewitchment.client.packet.SyncDemonTradesPacket;
import moriyashiine.bewitchment.client.screen.BaphometScreenHandler;
import moriyashiine.bewitchment.common.entity.DemonMerchant;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWPledges;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
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
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class BaphometEntity extends BWHostileEntity implements Pledgeable, DemonMerchant {
	private final ServerBossEvent bossBar;

	private final Set<UUID> pledgedPlayerUUIDS = new HashSet<>();
	private int timeSinceLastAttack = 0;
	public int flameIndex = random.nextInt(8);

	private final List<DemonEntity.DemonTradeOffer> offers = new ArrayList<>();
	private Player customer = null;
	private int tradeResetTimer = 0;

	public BaphometEntity(EntityType<? extends Monster> entityType, Level world) {
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
		flameIndex = ++flameIndex % 8;
		if (!level().isClientSide) {
			bossBar.setProgress(getHealth() / getMaxHealth());
			tradeResetTimer++;
			if (tradeResetTimer >= 168000) {
				tradeResetTimer = 0;
				offers.clear();
			}
			LivingEntity target = getTarget();
			int timer = tickCount + getId();
			if (timer % 20 == 0) {
				heal(1);
			}
			if (target != null) {
				timeSinceLastAttack++;
				if (timeSinceLastAttack >= 600) {
					BWUtil.teleport(this, target.getX(), target.getY(), target.getZ(), true);
					timeSinceLastAttack = 0;
				}
				lookAt(target, 360, 360);
				if (timer % 60 == 0) {
					for (int i = -1; i <= 1; i++) {
						LargeFireball fireball = new LargeFireball(level(), this, target.getX() - getX() + (i * 2), target.getY(0.5) - getY(0.5), target.getZ() - getZ() + (i * 2), 1);
						fireball.absMoveTo(fireball.getX(), getY(0.5), fireball.getZ());
						fireball.setOwner(this);
						level().playSound(null, blockPosition(), BWSoundEvents.ENTITY_GENERIC_SHOOT, getSoundSource(), getSoundVolume(), getVoicePitch());
						level().addFreshEntity(fireball);
					}
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
		return BWPledges.BAPHOMET;
	}

	@Override
	public Collection<UUID> getPledgedPlayerUUIDs() {
		return pledgedPlayerUUIDS;
	}

	@Override
	public EntityType<?> getMinionType() {
		return EntityType.BLAZE;
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
		return BWSoundEvents.ENTITY_BAPHOMET_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_BAPHOMET_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_BAPHOMET_DEATH;
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (player instanceof ServerPlayer serverPlayer && isAlive() && getTarget() == null && BewitchmentAPI.isPledged(player, getPledgeID())) {
			if (BWUtil.rejectTrades(this)) {
				return InteractionResult.FAIL;
			}
			if (getCurrentCustomer() == null) {
				setCurrentCustomer(player);
			}
			if (!getOffers().isEmpty()) {
				SyncContractsPacket.send(serverPlayer);
				player.openMenu(new SimpleMenuProvider((id, playerInventory, customer) -> new BaphometScreenHandler(id, this), getDisplayName())).ifPresent(syncId -> SyncDemonTradesPacket.send(serverPlayer, this, syncId));
			} else {
				setCurrentCustomer(null);
			}
		}
		return InteractionResult.sidedSuccess(level().isClientSide);
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
	public void die(DamageSource source) {
		super.die(source);
		setCurrentCustomer(null);
	}

	@Override
	public void setTarget(@Nullable LivingEntity target) {
		super.setTarget(target);
		if (target != null) {
			setCurrentCustomer(null);
		}
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
		if (nbt.contains("Offers")) {
			offers.clear();
			ListTag offersList = nbt.getList("Offers", Tag.TAG_COMPOUND);
			for (Tag offerTag : offersList) {
				offers.add(new DemonEntity.DemonTradeOffer((CompoundTag) offerTag));
			}
		}
		tradeResetTimer = nbt.getInt("TradeResetTimer");
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		toNbtPledgeable(nbt);
		if (!offers.isEmpty()) {
			ListTag offersList = new ListTag();
			for (DemonEntity.DemonTradeOffer offer : offers) {
				offersList.add(offer.toTag());
			}
			nbt.put("Offers", offersList);
		}
		nbt.putInt("TradeResetTimer", tradeResetTimer);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, new DemonEntity.LookAtCustomerGoal<>(this));
		goalSelector.addGoal(2, new MeleeAttackGoal(this, 1, true));
		goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1));
		goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(5, new RandomLookAroundGoal(this));
		targetSelector.addGoal(0, new HurtByTargetGoal(this));
		targetSelector.addGoal(1, BWUtil.createGenericPledgeableTargetGoal(this));
	}

	@Override
	public List<DemonEntity.DemonTradeOffer> getOffers() {
		if (offers.isEmpty()) {
			List<Contract> availableContracts = BWRegistries.CONTRACT.stream().collect(Collectors.toList());
			for (int i = 0; i < 5; i++) {
				Contract contract = availableContracts.get(random.nextInt(availableContracts.size()));
				offers.add(new DemonEntity.DemonTradeOffer(contract, 72000, 2 + random.nextInt(2) * 2));
				availableContracts.remove(contract);
			}
		}
		return offers;
	}

	@Override
	public LivingEntity getDemonTrader() {
		return this;
	}

	@Override
	public void onSell(DemonEntity.DemonTradeOffer offer) {
		if (!level().isClientSide) {
			level().playSound(null, blockPosition(), BWSoundEvents.ITEM_CONTRACT_USE, getSoundSource(), getSoundVolume(), getVoicePitch());
			level().playSound(null, blockPosition(), getAmbientSound(), getSoundSource(), getSoundVolume(), getVoicePitch());
		}
	}

	@Override
	public void setCurrentCustomer(Player customer) {
		this.customer = customer;
	}

	@Override
	public @Nullable Player getCurrentCustomer() {
		return customer;
	}
}
