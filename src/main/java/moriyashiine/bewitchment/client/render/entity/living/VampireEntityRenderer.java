/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.entity.living;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.entity.living.VampireEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.VampireEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class VampireEntityRenderer extends MobRenderer<VampireEntity, VampireEntityModel<VampireEntity>> {
	private static ResourceLocation[] TEXTURES;

	public VampireEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new VampireEntityModel<>(context.bakeLayer(BewitchmentClient.VAMPIRE_MODEL_LAYER)), 0.3f);
	}

	@Override
	public ResourceLocation getTexture(VampireEntity entity) {
		if (TEXTURES == null) {
			int variants = entity.getVariants();
			TEXTURES = new ResourceLocation[variants];
			for (int i = 0; i < variants; i++) {
				TEXTURES[i] = Bewitchment.id("textures/entity/living/vampire/" + i + ".png");
			}
		}
		return TEXTURES[entity.getVariant()];
	}
}
