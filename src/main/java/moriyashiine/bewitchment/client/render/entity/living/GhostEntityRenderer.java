/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.entity.living;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.entity.living.GhostEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.GhostEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class GhostEntityRenderer extends MobRenderer<GhostEntity, GhostEntityModel<GhostEntity>> {
	public static final ResourceLocation EMPTY = new ResourceLocation("textures/block/redstone_dust_overlay.png");
	private static ResourceLocation[] TEXTURES;

	public GhostEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new GhostEntityModel<>(context.bakeLayer(BewitchmentClient.GHOST_MODEL_LAYER)), 0);
		addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
		addLayer(new RenderLayer<>(this) {
			@Override
			public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, GhostEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
				if (TEXTURES == null) {
					int variants = entity.getVariants();
					TEXTURES = new ResourceLocation[variants];
					for (int i = 0; i < variants; i++) {
						TEXTURES[i] = Bewitchment.id("textures/entity/living/ghost/" + i + ".png");
					}
				}
				VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderType.entityTranslucent(TEXTURES[entity.getVariant()]));
				getParentModel().renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(GhostEntity entity) {
		return EMPTY;
	}

	@Override
	protected int getBlockLightLevel(GhostEntity entity, BlockPos blockPos) {
		return 15;
	}
}
