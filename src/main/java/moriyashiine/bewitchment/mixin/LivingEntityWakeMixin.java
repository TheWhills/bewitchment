/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.event.BWCommonEvents;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fabric's STOP_SLEEPING fired for every living entity that wakes up (villagers too); Forge's
 * PlayerWakeUpEvent only covers players, so hook the vanilla method directly.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityWakeMixin {
	@Inject(method = "stopSleeping", at = @At("HEAD"))
	private void bewitchment$onStopSleeping(CallbackInfo ci) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (self.isSleeping()) {
			BWCommonEvents.onStopSleeping(self);
		}
	}
}
