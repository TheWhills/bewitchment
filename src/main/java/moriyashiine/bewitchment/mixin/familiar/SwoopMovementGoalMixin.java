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
import org.spongepowered.asm.mixin.injection.Redirect;

// PhantomSweepAttackGoal is package-private; target it by name (was an access widener on Fabric).
@Mixin(targets = "net.minecraft.world.entity.monster.Phantom$PhantomSweepAttackGoal")
public class SwoopMovementGoalMixin {
	// Redirect the target's isAlive() check (receiver = the target) instead of capturing the local:
	// production Minecraft has no local variable table. Treating the target as "not alive" makes
	// canContinueToUse return false, exactly as the original early return did.
	@Redirect(method = "canContinueToUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isAlive()Z", ordinal = 0))
	private boolean shouldContinue(LivingEntity livingEntity) {
		if (livingEntity instanceof Player player && BewitchmentAPI.getFamiliar(player) == EntityType.CAT) {
			return false;
		}
		return livingEntity.isAlive();
	}
}
