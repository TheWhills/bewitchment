/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.entity.living;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.entity.living.OwlEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.OwlEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class OwlEntityRenderer extends MobRenderer<OwlEntity, OwlEntityModel<OwlEntity>> {
	private static ResourceLocation[] TEXTURES;

	public OwlEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new OwlEntityModel<>(context.bakeLayer(BewitchmentClient.OWL_MODEL_LAYER)), 0.3f);
	}

	@Override
	public ResourceLocation getTextureLocation(OwlEntity entity) {
		if (TEXTURES == null) {
			int variants = entity.getVariants();
			TEXTURES = new ResourceLocation[variants];
			for (int i = 0; i < variants; i++) {
				TEXTURES[i] = Bewitchment.id("textures/entity/living/owl/" + i + ".png");
			}
		}
		return TEXTURES[entity.getVariant()];
	}
}
