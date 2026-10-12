/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.entity.living;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.entity.living.ToadEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.ToadEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ToadEntityRenderer extends MobRenderer<ToadEntity, ToadEntityModel<ToadEntity>> {
	private static ResourceLocation[] TEXTURES;

	public ToadEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new ToadEntityModel<>(context.bakeLayer(BewitchmentClient.TOAD_MODEL_LAYER)), 0.3f);
	}

	@Override
	public ResourceLocation getTexture(ToadEntity entity) {
		if (TEXTURES == null) {
			int variants = entity.getVariants();
			TEXTURES = new ResourceLocation[variants];
			for (int i = 0; i < variants; i++) {
				TEXTURES[i] = Bewitchment.id("textures/entity/living/toad/" + i + ".png");
			}
		}
		return TEXTURES[entity.getVariant()];
	}
}
