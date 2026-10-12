/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.entity.living;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.entity.living.SnakeEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.SnakeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SnakeEntityRenderer extends MobRenderer<SnakeEntity, SnakeEntityModel<SnakeEntity>> {
	private static ResourceLocation[] TEXTURES;

	public SnakeEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new SnakeEntityModel<>(context.bakeLayer(BewitchmentClient.SNAKE_MODEL_LAYER)), 0.3f);
	}

	@Override
	public ResourceLocation getTexture(SnakeEntity entity) {
		if (TEXTURES == null) {
			int variants = entity.getVariants();
			TEXTURES = new ResourceLocation[variants];
			for (int i = 0; i < variants; i++) {
				TEXTURES[i] = Bewitchment.id("textures/entity/living/snake/" + i + ".png");
			}
		}
		return TEXTURES[entity.getVariant()];
	}
}
