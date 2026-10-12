/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.entity.living;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.entity.living.HerneEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.HerneEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class HerneEntityRenderer extends MobRenderer<HerneEntity, HerneEntityModel<HerneEntity>> {
	private static final ResourceLocation TEXTURE = Bewitchment.id("textures/entity/living/herne.png");

	public HerneEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new HerneEntityModel<>(context.bakeLayer(BewitchmentClient.HERNE_MODEL_LAYER)), 0.5f);
		addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTexture(HerneEntity entity) {
		return TEXTURE;
	}
}
