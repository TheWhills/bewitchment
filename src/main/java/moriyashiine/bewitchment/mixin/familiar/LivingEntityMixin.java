/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.familiar;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.world.BWUniversalWorldState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@ModifyVariable(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), argsOnly = true)
	private MobEffectInstance modifyStatusEffect(MobEffectInstance effect) {
		if (!level().isClientSide && !effect.isAmbient() && effect.getEffect().getCategory() == MobEffectCategory.BENEFICIAL && (Object) this instanceof Player player && BewitchmentAPI.getFamiliar(player) == BWEntityTypes.TOAD) {
			return new MobEffectInstance(effect.getEffect(), (int) (effect.getDuration() * (effect.getEffect().isInstantenous() ? 1 : 1.5f)), effect.getAmplifier() + (effect.getEffect().isInstantenous() ? 1 : 0), false, effect.isVisible(), effect.showIcon());
		}
		return effect;
	}

	@ModifyVariable(method = "getDamageAfterArmorAbsorb", at = @At("HEAD"), argsOnly = true)
	private float modifyDamage(float amount, DamageSource source) {
		if (!level().isClientSide && BWComponents.FAMILIAR_COMPONENT.get(this).isFamiliar()) {
			amount /= 8;
		}
		return amount;
	}

	@Inject(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getCombatTracker()Lnet/minecraft/world/damagesource/CombatTracker;", ordinal = 1))
	private void removeFamiliar(DamageSource source, CallbackInfo ci) {
		if (!level().isClientSide) {
			BWUniversalWorldState universalWorldState = BWUniversalWorldState.get(level());
			for (int i = universalWorldState.familiars.size() - 1; i >= 0; i--) {
				if (getUUID().equals(universalWorldState.familiars.get(i).getB().getUUID("UUID"))) {
					universalWorldState.familiars.remove(i);
					universalWorldState.setDirty();
					break;
				}
			}
		}
	}

	@Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
	private void handleFallDamage(float fallDistance, float damageMultiplier, DamageSource source, CallbackInfoReturnable<Boolean> callbackInfo) {
		if ((Object) this instanceof Player player && BewitchmentAPI.getFamiliar(player) == BWEntityTypes.OWL) {
			callbackInfo.setReturnValue(false);
		}
	}

	@Inject(method = "checkFallDamage", at = @At("HEAD"), cancellable = true)
	private void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition, CallbackInfo callbackInfo) {
		if ((Object) this instanceof Player player && onGround && BewitchmentAPI.getFamiliar(player) == BWEntityTypes.OWL) {
			callbackInfo.cancel();
		}
	}
}
