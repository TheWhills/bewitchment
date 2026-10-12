/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.transformation;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@ModifyVariable(method = "applyArmorToDamage", at = @At("HEAD"), argsOnly = true)
	private float modifyDamage(float amount, DamageSource source) {
		if (!level().isClientSide && source.getEntity() instanceof Player player && BewitchmentAPI.isVampire(player, false)) {
			amount /= 8;
		}
		return amount;
	}

	@ModifyArg(method = "applyMovementInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V"))
	private float bewitchment$werewolfAirSpeed(float value) {
		if (!onGround() && BewitchmentAPI.isWerewolf(this, false)) {
			return value * 1.5F;
		}
		return value;
	}

	@Inject(method = "getJumpVelocity", at = @At("RETURN"), cancellable = true)
	private void getJumpVelocity(CallbackInfoReturnable<Float> callbackInfo) {
		if ((Object) this instanceof Player && BewitchmentAPI.isWerewolf(this, false)) {
			callbackInfo.setReturnValue(callbackInfo.getReturnValueF() * 1.5f);
		}
	}

	@Inject(method = "getGroup", at = @At("HEAD"), cancellable = true)
	private void getGroup(CallbackInfoReturnable<MobType> callbackInfo) {
		if ((Object) this instanceof Player) {
			if (BewitchmentAPI.isVampire(this, true)) {
				callbackInfo.setReturnValue(MobType.UNDEAD);
			} else if (BewitchmentAPI.isWerewolf(this, true)) {
				callbackInfo.setReturnValue(BewitchmentAPI.DEMON);
			}
		}
	}

	@Inject(method = "blockedByShield", at = @At("HEAD"), cancellable = true)
	private void blockedByShield(DamageSource source, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (BewitchmentAPI.isWerewolf(source.getDirectEntity(), false)) {
			callbackInfo.setReturnValue(false);
		}
	}

	@Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
	private void handleFallDamage(float fallDistance, float damageMultiplier, DamageSource source, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (BewitchmentAPI.isWerewolf(this, false) && fallDistance <= 6) {
			callbackInfo.setReturnValue(false);
		}
	}
}
