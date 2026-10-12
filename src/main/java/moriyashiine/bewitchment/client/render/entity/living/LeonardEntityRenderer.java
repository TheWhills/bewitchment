/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.entity.living;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.entity.living.LeonardEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.LeonardEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class LeonardEntityRenderer extends MobRenderer<LeonardEntity, LeonardEntityModel<LeonardEntity>> {
	private static final ResourceLocation TEXTURE = Bewitchment.id("textures/entity/living/leonard.png");

	public LeonardEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new LeonardEntityModel<>(context.bakeLayer(BewitchmentClient.LEONARD_MODEL_LAYER)), 0.5f);
		addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTexture(LeonardEntity entity) {
		return TEXTURE;
	}
}
