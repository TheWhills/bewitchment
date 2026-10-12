/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.entity.living;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.entity.living.RavenEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.RavenEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RavenEntityRenderer extends MobRenderer<RavenEntity, RavenEntityModel<RavenEntity>> {
	private static ResourceLocation[] TEXTURES;

	public RavenEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new RavenEntityModel<>(context.bakeLayer(BewitchmentClient.RAVEN_MODEL_LAYER)), 0.3f);
	}

	@Override
	public ResourceLocation getTexture(RavenEntity entity) {
		if (TEXTURES == null) {
			int variants = entity.getVariants();
			TEXTURES = new ResourceLocation[variants];
			for (int i = 0; i < variants; i++) {
				TEXTURES[i] = Bewitchment.id("textures/entity/living/raven/" + i + ".png");
			}
		}
		return TEXTURES[entity.getVariant()];
	}
}
