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
	Minecraft minecraft;

	@Shadow
	@Final
	private Camera mainCamera;

	@Inject(method = "renderLevel", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/minecraft/renderer/LevelRenderer;renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/minecraft/Camera;Lnet/minecraft/minecraft/renderer/GameRenderer;Lnet/minecraft/minecraft/renderer/LightTexture;Lorg/joml/Matrix4f;)V"))
	private void renderWorld(float tickDelta, long limitTime, PoseStack matrix, CallbackInfo info) {
		if (minecraft.player.getVehicle() instanceof BroomEntity && !mainCamera.isDetached()) {
			matrix.translate(0, -(Mth.sin((minecraft.player.getVehicle().tickCount + minecraft.player.getVehicle().getId()) / 4f) / 16f), 0);
		}
	}
}
