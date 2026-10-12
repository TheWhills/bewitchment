/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.transformation;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWDamageSources;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	@Shadow
	public abstract SoundSource getSoundSource();

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@ModifyVariable(method = "actuallyHurt", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/world/entity/player/Player;getHealth()F"), ordinal = 0, argsOnly = true)
	private float modifyDamage(float amount, DamageSource source) {
		if (!level().isClientSide) {
			amount = BWDamageSources.handleDamage(this, source, amount);
		}
		return amount;
	}

	@ModifyVariable(method = "causeFoodExhaustion", at = @At("HEAD"), argsOnly = true)
	private float modifyExhaustion(float exhaustion) {
		if (!level().isClientSide) {
			if (BewitchmentAPI.isWerewolf(this, true)) {
				exhaustion *= 1.25f;
			}
			if (BewitchmentAPI.isWerewolf(this, false)) {
				exhaustion *= 2;
			}
		}
		return exhaustion;
	}

	@Inject(method = "isHurt", at = @At("RETURN"), cancellable = true)
	private void canFoodHeal(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValueZ() && BewitchmentAPI.isVampire(this, true)) {
			callbackInfo.setReturnValue(false);
		}
	}

	@Inject(method = "eat", at = @At("HEAD"))
	private void eat(Level world, ItemStack stack, CallbackInfoReturnable<ItemStack> callbackInfo) {
		if (!world.isClientSide) {
			FoodProperties foodComponent = stack.getItem().getFoodProperties();
			if (foodComponent != null) {
				boolean vampire = BewitchmentAPI.isVampire(this, true);
				if (vampire || (BewitchmentAPI.isWerewolf(this, true) && !foodComponent.isMeat())) {
					addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 1));
					addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
					addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
					addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 1));
				}
				if (vampire && (stack.getItem() == BWObjects.GARLIC || stack.getItem() == BWObjects.GRILLED_GARLIC || stack.getItem() == BWObjects.GARLIC_BREAD)) {
					hurt(BWDamageSources.create(world, BWDamageSources.MAGIC_COPY), Float.MAX_VALUE);
				}
			}
		}
	}
}
