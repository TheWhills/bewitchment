/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.entity.living;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.entity.living.BaphometEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.BaphometEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class BaphometEntityRenderer extends MobRenderer<BaphometEntity, BaphometEntityModel<BaphometEntity>> {
	private static final ResourceLocation TEXTURE = Bewitchment.id("textures/entity/living/baphomet/baphomet.png");
	private static ResourceLocation[] TEXTURES;

	public BaphometEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new BaphometEntityModel<>(context.bakeLayer(BewitchmentClient.BAPHOMET_MODEL_LAYER)), 0.5f);
		addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
		addLayer(new RenderLayer<>(this) {
			@Override
			public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, BaphometEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
				if (TEXTURES == null) {
					TEXTURES = new ResourceLocation[8];
					for (int i = 0; i < 8; i++) {
						TEXTURES[i] = Bewitchment.id("textures/entity/living/baphomet/flame_" + i + ".png");
					}
				}
				VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderType.entityTranslucent(TEXTURES[entity.flameIndex]));
				getParentModel().renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
			}
		});
	}

	@Override
	public ResourceLocation getTexture(BaphometEntity entity) {
		return TEXTURE;
	}
}
