/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.poppet;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.item.PoppetItem;
import moriyashiine.bewitchment.common.BWConfig;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
	@Shadow
	public abstract ItemStack getItem();

	public ItemEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (getItem().getItem() == BWObjects.VOODOO_POPPET.get() && !BWConfig.disabledPoppets.contains("bewitchment:voodoo_poppet")) {
			LivingEntity owner = BewitchmentAPI.getTaglockOwner(level(), getItem());
			if (owner != null) {
				if (getDeltaMovement().length() > 1 / 8f) {
					int damage = random.nextFloat() < 1 / 4f ? 1 : 0;
					if (getItem().hurt(damage, random, null) && getItem().getDamageValue() >= getItem().getMaxDamage()) {
						getItem().shrink(1);
					}
					if (!BewitchmentAPI.hasVoodooProtection(owner, damage)) {
						owner.push(getDeltaMovement().x / 2, getDeltaMovement().y / 2, getDeltaMovement().z / 2);
						owner.hurtMarked = true;
					}
				}
				if (isUnderWater()) {
					int damage = tickCount % 20 == 0 ? 1 : 0;
					if (getItem().hurt(damage, random, null) && getItem().getDamageValue() >= getItem().getMaxDamage()) {
						getItem().shrink(1);
					}
					if (!BewitchmentAPI.hasVoodooProtection(owner, damage)) {
						BWComponents.ADDITIONAL_WATER_DATA_COMPONENT.get(owner).setSubmerged(true);
					}
				}
			}
		}
	}

	@Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
	private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (getItem().getItem() == BWObjects.VOODOO_POPPET.get() && !BWConfig.disabledPoppets.contains("bewitchment:voodoo_poppet")) {
			if (!level().isClientSide) {
				LivingEntity owner = BewitchmentAPI.getTaglockOwner(level(), getItem());
				if (owner != null) {
					if (source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypes.LIGHTNING_BOLT)) {
						remove(RemovalReason.DISCARDED);
						if (!BewitchmentAPI.hasVoodooProtection(owner, getItem().getMaxDamage() / 4)) {
							owner.setRemainingFireTicks(Integer.MAX_VALUE);
						}
					}
					if (source.is(DamageTypes.CACTUS) && owner.hurtTime == 0) {
						if (getItem().hurt(1, random, null) && getItem().getDamageValue() >= getItem().getMaxDamage()) {
							getItem().shrink(1);
						}
						if (!BewitchmentAPI.hasVoodooProtection(owner, 1)) {
							owner.hurt(level().damageSources().cactus(), 1);
						}
					}
				}
			}
			if (source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypes.LIGHTNING_BOLT) || source.is(DamageTypes.CACTUS)) {
				callbackInfo.setReturnValue(false);
			}
		}
	}

	@Inject(method = "setUnderwaterMovement", at = @At("HEAD"), cancellable = true)
	private void bewitchment$stopPoppetsFromFloating(CallbackInfo ci) {
		if (getItem().getItem() instanceof PoppetItem) {
			Vec3 velocity = getDeltaMovement();
			setDeltaMovement(velocity.x * 0.99, velocity.y * 0.9, velocity.z * 0.99);
			ci.cancel();
		}
	}
}
