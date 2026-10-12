/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
	@Inject(method = "renderShadow", at = @At("HEAD"), cancellable = true)
	private static void renderShadow(PoseStack matrices, MultiBufferSource vertexConsumers, Entity entity, float opacity, float tickDelta, LevelReader world, float radius, CallbackInfo callbackInfo) {
		if (entity instanceof Player player && BWComponents.FULL_INVISIBILITY_COMPONENT.get(player).isFullInvisible()) {
			callbackInfo.cancel();
		}
	}
}
