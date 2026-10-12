/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity.living.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public abstract class BWTameableEntity extends TamableAnimal {
	public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(BWTameableEntity.class, EntityDataSerializers.INT);

	protected BWTameableEntity(EntityType<? extends TamableAnimal> type, Level world) {
		super(type, world);
	}

	@Override
	public EntityGetter level() {
		return level();
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (isInvulnerableTo(source)) {
			return false;
		} else {
			Entity entity = source.getEntity();
			setOrderedToSit(false);
			if (entity != null && !(entity instanceof Player) && !(entity instanceof AbstractArrow)) {
				amount = (amount + 1) / 2f;
			}
			return super.hurt(source, amount);
		}
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData, @Nullable CompoundTag entityTag) {
		initializeVariant();
		return super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityTag);
	}

	public void initializeVariant() {
		int variants = getVariants();
		if (variants > 1) {
			if (hasShiny()) {
				if (random.nextInt(8192) == 0) {
					entityData.set(VARIANT, 0);
				} else {
					entityData.set(VARIANT, random.nextInt(variants - 1) + 1);
				}
			} else {
				entityData.set(VARIANT, random.nextInt(variants));
			}
		}
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		boolean client = level().isClientSide;
		ItemStack stack = player.getItemInHand(hand);
		if (isFood(stack)) {
			if (getHealth() < getMaxHealth()) {
				if (!client) {
					usePlayerItem(player, hand, stack);
					heal(4);
				}
				return InteractionResult.sidedSuccess(client);
			}
		} else {
			if (!isTame()) {
				if (isTamingItem(stack)) {
					if (!client) {
						usePlayerItem(player, hand, stack);
						if (random.nextInt(4) == 0) {
							tame(player);
							setOrderedToSit(true);
							setTarget(null);
							navigation.stop();
							level().broadcastEntityEvent(this, (byte) 7);
						} else {
							level().broadcastEntityEvent(this, (byte) 6);
						}
					}
					return InteractionResult.sidedSuccess(client);
				}
			} else if (isOwnedBy(player)) {
				if (!client) {
					setOrderedToSit(!isOrderedToSit());
				}
				return InteractionResult.sidedSuccess(client);
			}
		}
		return super.mobInteract(player, hand);
	}

	@Override
	public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
		if (target instanceof TamableAnimal && ((TamableAnimal) target).isTame()) {
			return false;
		}
		if (target instanceof AbstractHorse horse && horse.isTamed()) {
			return false;
		}
		if (target instanceof Player targetPlayer && owner instanceof Player ownerPlayer && !ownerPlayer.canHarmPlayer(targetPlayer)) {
			return false;
		}
		return !(target instanceof Creeper) && !(target instanceof Ghast);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		if (getVariants() > 1) {
			entityData.define(VARIANT, 1);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		if (getVariants() > 1) {
			entityData.set(VARIANT, nbt.getInt("Variant"));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		if (getVariants() > 1) {
			nbt.putInt("Variant", entityData.get(VARIANT));
		}
	}

	protected abstract boolean hasShiny();

	public abstract int getVariants();

	public int getVariant() {
		if (this.getEntityData().get(BWTameableEntity.VARIANT) < 0) initializeVariant();
		return this.getEntityData().get(BWTameableEntity.VARIANT);
	}

	protected abstract boolean isTamingItem(ItemStack stack);
}
