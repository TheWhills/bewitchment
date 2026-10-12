/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.familiar;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

// PhantomSweepAttackGoal is package-private; target it by name (was an access widener on Fabric).
@Mixin(targets = "net.minecraft.world.entity.monster.Phantom$PhantomSweepAttackGoal")
public class SwoopMovementGoalMixin {
	@Inject(method = "canContinueToUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isAlive()Z"), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
	private void shouldContinue(CallbackInfoReturnable<Boolean> callbackInfo, LivingEntity livingEntity) {
		if (livingEntity instanceof Player player && BewitchmentAPI.getFamiliar(player) == EntityType.CAT) {
			callbackInfo.setReturnValue(false);
		}
	}
}
