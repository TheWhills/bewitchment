/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.entity.living;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.entity.living.LilithEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.LilithEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class LilithEntityRenderer extends MobRenderer<LilithEntity, LilithEntityModel<LilithEntity>> {
	private static final ResourceLocation TEXTURE = Bewitchment.id("textures/entity/living/lilith.png");

	public LilithEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new LilithEntityModel<>(context.bakeLayer(BewitchmentClient.LILITH_MODEL_LAYER)), 0.5f);
		addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(LilithEntity entity) {
		return TEXTURE;
	}
}
