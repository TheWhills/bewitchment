/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.component;

import moriyashiine.bewitchment.common.component.base.ComponentContainer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Ticks Bewitchment components on the logical client right after their entity ticks. */
@Mixin(ClientLevel.class)
public class ClientLevelComponentTickMixin {
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
				container.clientTick();
			}
		}
	}
}
