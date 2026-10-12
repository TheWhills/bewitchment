/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.client.packet.SyncContractsPacket;
import moriyashiine.bewitchment.client.packet.SyncDemonTradesPacket;
import moriyashiine.bewitchment.client.screen.DemonScreenHandler;
import moriyashiine.bewitchment.common.entity.DemonMerchant;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWMaterials;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class DemonEntity extends BWHostileEntity implements DemonMerchant {
	public static final EntityDataAccessor<Boolean> MALE = SynchedEntityData.defineId(DemonEntity.class, EntityDataSerializers.BOOLEAN);

	private final List<DemonEntity.DemonTradeOffer> offers = new ArrayList<>();
	private Player customer = null;
	private int tradeResetTimer = 0;

	public DemonEntity(EntityType<? extends Monster> entityType, Level world) {
		super(entityType, world);
		setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0);
		setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0);
		xpReward = 20;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 200).add(Attributes.ATTACK_DAMAGE, 6).add(Attributes.ARMOR, 4).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.KNOCKBACK_RESISTANCE, 0.75);
	}

	@Override
	public void tick() {
		super.tick();
		if (!level().isClientSide) {
			tradeResetTimer++;
			if (tradeResetTimer >= 168000) {
				tradeResetTimer = 0;
				offers.clear();
			}
			LivingEntity target = getTarget();
			if (target != null) {
				lookAt(target, 360, 360);
				if ((tickCount + getId()) % 40 == 0) {
					SmallFireball fireball = new SmallFireball(level(), this, target.getX() - getX(), target.getY(0.5) - getY(0.5), target.getZ() - getZ());
					fireball.absMoveTo(fireball.getX(), getY(0.5), fireball.getZ());
					level().playSound(null, blockPosition(), BWSoundEvents.ENTITY_GENERIC_SHOOT, getSoundSource(), getSoundVolume(), getVoicePitch());
					level().addFreshEntity(fireball);
					swing(InteractionHand.MAIN_HAND);
				}
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
		return BewitchmentAPI.DEMON;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return BWSoundEvents.ENTITY_DEMON_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_DEMON_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_DEMON_DEATH;
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (player instanceof ServerPlayer serverPlayer && isAlive() && getTarget() == null) {
			if (BWUtil.rejectTrades(this)) {
				return InteractionResult.FAIL;
			}
			if (getCurrentCustomer() == null) {
				setCurrentCustomer(player);
			}
			if (!getOffers().isEmpty()) {
				SyncContractsPacket.send(serverPlayer);
				player.openMenu(new SimpleMenuProvider((id, playerInventory, customer) -> new DemonScreenHandler(id, this), getDisplayName())).ifPresent(syncId -> SyncDemonTradesPacket.send(serverPlayer, this, syncId));
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
	public boolean requiresCustomPersistence() {
		return true;
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		boolean flag = super.doHurtTarget(target);
		if (flag && target instanceof LivingEntity) {
			target.setSecondsOnFire(6);
			target.push(0, 0.2, 0);
			swing(InteractionHand.MAIN_HAND);
		}
		return flag;
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData, @Nullable CompoundTag entityTag) {
		entityData.set(MALE, random.nextBoolean());
		return super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityTag);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		entityData.set(MALE, nbt.getBoolean("Male"));
		if (nbt.contains("Offers")) {
			offers.clear();
			ListTag offersList = nbt.getList("Offers", Tag.TAG_COMPOUND);
			for (Tag offerTag : offersList) {
				offers.add(new DemonTradeOffer((CompoundTag) offerTag));
			}
		}
		tradeResetTimer = nbt.getInt("TradeResetTimer");
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putBoolean("Male", entityData.get(MALE));
		if (!offers.isEmpty()) {
			ListTag offersList = new ListTag();
			for (DemonTradeOffer offer : offers) {
				offersList.add(offer.toTag());
			}
			nbt.put("Offers", offersList);
		}
		nbt.putInt("TradeResetTimer", tradeResetTimer);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(MALE, false);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, new LookAtCustomerGoal<>(this));
		goalSelector.addGoal(2, new MeleeAttackGoal(this, 1, true));
		goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1));
		goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(5, new RandomLookAroundGoal(this));
		targetSelector.addGoal(0, new HurtByTargetGoal(this));
		targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> !(entity instanceof ArmorStand) && BWUtil.getArmorPieces(entity, stack -> stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getMaterial() == BWMaterials.BESMIRCHED_ARMOR) < 3 && (entity.getMobType() != BewitchmentAPI.DEMON || entity instanceof Player)));
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
	public List<DemonEntity.DemonTradeOffer> getOffers() {
		if (offers.isEmpty()) {
			List<Contract> availableContracts = BWRegistries.CONTRACT.stream().collect(Collectors.toList());
			for (int i = 0; i < 3; i++) {
				Contract contract = availableContracts.get(random.nextInt(availableContracts.size()));
				offers.add(new DemonTradeOffer(contract, 168000, Mth.nextInt(random, 3, 6)));
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

	@SuppressWarnings("ConstantConditions")
	public static class DemonTradeOffer {
		private final Contract contract;
		private final int duration, cost;

		public DemonTradeOffer(CompoundTag tag) {
			this(BWRegistries.CONTRACT.get(new ResourceLocation(tag.getString("Contract"))), tag.getInt("Duration"), tag.getInt("Cost"));
		}

		public DemonTradeOffer(Contract contract, int duration, int cost) {
			this.contract = contract;
			this.duration = duration;
			this.cost = cost;
		}

		public CompoundTag toTag() {
			CompoundTag tag = new CompoundTag();
			tag.putString("Contract", BWRegistries.CONTRACT.getKey(contract).toString());
			tag.putInt("Duration", duration);
			tag.putInt("Cost", cost);
			return tag;
		}

		public static void toPacket(List<DemonTradeOffer> offers, FriendlyByteBuf buf) {
			buf.writeInt(offers.size());
			for (DemonTradeOffer offer : offers) {
				buf.writeResourceLocation(BWRegistries.CONTRACT.getKey(offer.getContract()));
				buf.writeInt(offer.duration);
				buf.writeInt(offer.cost);
			}
		}

		public static List<DemonTradeOffer> fromPacket(FriendlyByteBuf buf) {
			int count = buf.readInt();
			List<DemonTradeOffer> offers = new ArrayList<>(count);
			for (int i = 0; i < count; i++) {
				offers.add(new DemonTradeOffer(BWRegistries.CONTRACT.get(buf.readResourceLocation()), buf.readInt(), buf.readInt()));
			}
			return offers;
		}

		public void apply(DemonMerchant merchant) {
			if (merchant.getCurrentCustomer() != null) {
				BWComponents.CONTRACTS_COMPONENT.get(merchant.getCurrentCustomer()).addContract(new Contract.Instance(contract, getDuration(), getCost(merchant)));
			}
		}

		public Contract getContract() {
			return contract;
		}

		public int getDuration() {
			return duration;
		}

		public int getCost(DemonMerchant merchant) {
			return merchant.isDiscount() ? 1 : cost;
		}
	}

	public static class LookAtCustomerGoal<T extends Mob & DemonMerchant> extends LookAtPlayerGoal {
		private final T merchant;

		public LookAtCustomerGoal(T merchant) {
			super(merchant, Player.class, 8);
			this.merchant = merchant;
			setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
		}

		public boolean canUse() {
			if (this.merchant.getCurrentCustomer() != null) {
				this.lookAt = this.merchant.getCurrentCustomer();
				return true;
			} else {
				return false;
			}
		}
	}
}
