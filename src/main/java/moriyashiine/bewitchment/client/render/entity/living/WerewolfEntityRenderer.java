/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.entity.living;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.entity.living.WerewolfEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.WerewolfEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class WerewolfEntityRenderer extends MobRenderer<WerewolfEntity, WerewolfEntityModel<WerewolfEntity>> {
	private static ResourceLocation[] TEXTURES;

	public WerewolfEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new WerewolfEntityModel<>(context.bakeLayer(BewitchmentClient.WEREWOLF_MODEL_LAYER)), 0.5f);
		addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(WerewolfEntity entity) {
		if (TEXTURES == null) {
			int variants = entity.getVariants();
			TEXTURES = new ResourceLocation[variants];
			for (int i = 0; i < variants; i++) {
				TEXTURES[i] = Bewitchment.id("textures/entity/living/werewolf/" + i + ".png");
			}
		}
		return TEXTURES[entity.getVariant()];
	}
}
