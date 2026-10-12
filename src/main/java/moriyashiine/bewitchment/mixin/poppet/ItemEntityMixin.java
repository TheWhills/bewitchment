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
	public abstract ItemStack getStack();

	public ItemEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (getStack().getItem() == BWObjects.VOODOO_POPPET && !BWConfig.disabledPoppets.contains("bewitchment:voodoo_poppet")) {
			LivingEntity owner = BewitchmentAPI.getTaglockOwner(level(), getStack());
			if (owner != null) {
				if (getDeltaMovement().length() > 1 / 8f) {
					int damage = random.nextFloat() < 1 / 4f ? 1 : 0;
					if (getStack().hurt(damage, random, null) && getStack().getDamageValue() >= getStack().getMaxDamage()) {
						getStack().shrink(1);
					}
					if (!BewitchmentAPI.hasVoodooProtection(owner, damage)) {
						owner.push(getDeltaMovement().x / 2, getDeltaMovement().y / 2, getDeltaMovement().z / 2);
						owner.hurtMarked = true;
					}
				}
				if (isUnderWater()) {
					int damage = tickCount % 20 == 0 ? 1 : 0;
					if (getStack().hurt(damage, random, null) && getStack().getDamageValue() >= getStack().getMaxDamage()) {
						getStack().shrink(1);
					}
					if (!BewitchmentAPI.hasVoodooProtection(owner, damage)) {
						BWComponents.ADDITIONAL_WATER_DATA_COMPONENT.get(owner).setSubmerged(true);
					}
				}
			}
		}
	}

	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (getStack().getItem() == BWObjects.VOODOO_POPPET && !BWConfig.disabledPoppets.contains("bewitchment:voodoo_poppet")) {
			if (!level().isClientSide) {
				LivingEntity owner = BewitchmentAPI.getTaglockOwner(level(), getStack());
				if (owner != null) {
					if (source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypes.LIGHTNING_BOLT)) {
						remove(RemovalReason.DISCARDED);
						if (!BewitchmentAPI.hasVoodooProtection(owner, getStack().getMaxDamage() / 4)) {
							owner.setRemainingFireTicks(Integer.MAX_VALUE);
						}
					}
					if (source.is(DamageTypes.CACTUS) && owner.hurtTime == 0) {
						if (getStack().hurt(1, random, null) && getStack().getDamageValue() >= getStack().getMaxDamage()) {
							getStack().shrink(1);
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

	@Inject(method = "applyWaterBuoyancy", at = @At("HEAD"), cancellable = true)
	private void bewitchment$stopPoppetsFromFloating(CallbackInfo ci) {
		if (getStack().getItem() instanceof PoppetItem) {
			Vec3 velocity = getDeltaMovement();
			setDeltaMovement(velocity.x * 0.99, velocity.y * 0.9, velocity.z * 0.99);
			ci.cancel();
		}
	}
}
