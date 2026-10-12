/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import moriyashiine.bewitchment.common.entity.living.*;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public abstract class HeldItemFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T> & ArmedModel> extends RenderLayer<T, M> {
	public HeldItemFeatureRendererMixin(RenderLayerParent<T, M> context) {
		super(context);
	}

	@Inject(method = "renderArmWithItem", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
	private void renderItem(LivingEntity entity, ItemStack stack, ItemDisplayContext transformationMode, HumanoidArm arm, PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo ci) {
		if (entity instanceof WerewolfEntity) {
			matrices.translate((arm == HumanoidArm.LEFT ? 0.75f : -0.75f) / 3.5f, 1 / 3f, -7 / 8f);
			matrices.mulPose(Axis.XP.rotationDegrees(45));
		} else if (entity instanceof DemonEntity) {
			matrices.translate((arm == HumanoidArm.LEFT ? 0.75f : -0.75f) / 24f, 0, -1 / 8f);
		} else if (entity instanceof LeonardEntity) {
			matrices.translate((arm == HumanoidArm.LEFT ? 0.75f : -0.75f) / 24f, 0, -5.75 / 16f);
		} else if (entity instanceof BaphometEntity) {
			matrices.translate((arm == HumanoidArm.LEFT ? 0.75f : -0.75f) / 24f, 0, -1 / 4f);
		} else if (entity instanceof LilithEntity) {
			matrices.translate((arm == HumanoidArm.LEFT ? 0.75f : -0.75f) / 24f, 0, -5.75 / 16f);
		} else if (entity instanceof HerneEntity) {
			matrices.translate((arm == HumanoidArm.LEFT ? 0.75f : -0.75f) / 24f, 0, -4 / 7f);
		}
	}
}
