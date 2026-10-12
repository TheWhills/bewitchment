/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.client;

import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(MobRenderer.class)
public abstract class MobEntityRendererMixin<T extends Mob, M extends EntityModel<T>> {
	@Inject(method = "shouldRender(Lnet/minecraft/world/entity/Mob;Lnet/minecraft/client/renderer/culling/Frustum;DDD)Z", at = @At("RETURN"), cancellable = true)
	private void shouldRender(T mobEntity, Frustum frustum, double d, double e, double f, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValueZ()) {
			BWComponents.FAKE_MOB_COMPONENT.maybeGet(mobEntity).ifPresent(fakeMobComponent -> {
				if (fakeMobComponent.getTarget() != null && !Minecraft.getInstance().player.getUUID().equals(fakeMobComponent.getTarget())) {
					callbackInfo.setReturnValue(false);
				}
			});
		}
	}
}
