/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import moriyashiine.bewitchment.api.entity.BroomEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ConstantConditions")
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Shadow
	@Final
	Minecraft client;

	@Shadow
	@Final
	private Camera camera;

	@Inject(method = "renderWorld", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lorg/joml/Matrix4f;)V"))
	private void renderWorld(float tickDelta, long limitTime, PoseStack matrix, CallbackInfo info) {
		if (client.player.getVehicle() instanceof BroomEntity && !camera.isDetached()) {
			matrix.translate(0, -(Mth.sin((client.player.getVehicle().tickCount + client.player.getVehicle().getId()) / 4f) / 16f), 0);
		}
	}
}
