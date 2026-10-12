/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.familiar;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEffect.class)
public class StatusEffectMixin {
	@Inject(method = "applyUpdateEffect", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, ordinal = 0, target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), cancellable = true)
	private void applyUpdateEffect(LivingEntity entity, int amplifier, CallbackInfo callbackInfo) {
		if (entity instanceof Player player && BewitchmentAPI.getFamiliar(player) == BWEntityTypes.SNAKE) {
			BewitchmentAPI.fillMagic(player, 1, false);
			callbackInfo.cancel();
		}
	}
}
