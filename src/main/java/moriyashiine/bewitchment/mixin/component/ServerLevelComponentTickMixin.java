/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.component;

import moriyashiine.bewitchment.common.component.base.ComponentContainer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Ticks Bewitchment components on the logical server right after their entity ticks. */
@Mixin(ServerLevel.class)
public class ServerLevelComponentTickMixin {
	@Inject(method = "tickNonPassenger", at = @At("TAIL"))
	private void bewitchment$tickComponents(Entity entity, CallbackInfo ci) {
		bewitchment$tick(entity);
	}

	@Inject(method = "tickPassenger", at = @At("TAIL"))
	private void bewitchment$tickPassengerComponents(Entity vehicle, Entity passenger, CallbackInfo ci) {
		bewitchment$tick(passenger);
	}

	private static void bewitchment$tick(Entity entity) {
		if (!entity.isRemoved()) {
			ComponentContainer container = ComponentContainer.of(entity);
			if (container != null) {
				container.serverTick();
			}
		}
	}
}
