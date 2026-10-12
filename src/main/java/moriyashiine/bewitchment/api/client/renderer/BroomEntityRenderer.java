/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import moriyashiine.bewitchment.api.client.model.BroomEntityModel;
import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.client.BewitchmentClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;

public abstract class BroomEntityRenderer<T extends BroomEntity> extends EntityRenderer<T> {
	private final BroomEntityModel model;

	protected BroomEntityRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
		model = new BroomEntityModel(ctx.bakeLayer(BewitchmentClient.BROOM_MODEL_LAYER));
	}

	@Override
	public void render(T entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
		matrices.pushPose();
		matrices.translate(0, -1, 0);
		matrices.mulPose(Axis.YP.rotationDegrees(90 - yaw + 90));
		matrices.translate(0, 0, -0.35);
		model.setupAnim(entity, yaw, 0, entity.tickCount + entity.getId(), 0, 0);
		model.renderToBuffer(matrices, vertexConsumers.getBuffer(model.renderType(getTextureLocation(entity))), light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
		matrices.popPose();
		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
	}
}
