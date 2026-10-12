/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.entity.living;

import com.mojang.blaze3d.vertex.PoseStack;
import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.entity.living.DemonEntityModel;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.DemonEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class DemonEntityRenderer extends MobRenderer<DemonEntity, DemonEntityModel<DemonEntity>> {
	private static ResourceLocation[] MALE_TEXTURES, FEMALE_TEXTURES;

	private final DemonEntityModel<DemonEntity> MALE_MODEL, FEMALE_MODEL;

	public DemonEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new DemonEntityModel<>(context.bakeLayer(BewitchmentClient.MALE_DEMON_MODEL_LAYER), true), 0.5f);
		addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
		MALE_MODEL = model;
		FEMALE_MODEL = new DemonEntityModel<>(context.bakeLayer(BewitchmentClient.FEMALE_DEMON_MODEL_LAYER), false);
	}

	@Override
	public ResourceLocation getTexture(DemonEntity entity) {
		if (MALE_TEXTURES == null) {
			int variants = entity.getVariants();
			MALE_TEXTURES = new ResourceLocation[variants];
			FEMALE_TEXTURES = new ResourceLocation[variants];
			for (int i = 0; i < variants; i++) {
				MALE_TEXTURES[i] = Bewitchment.id("textures/entity/living/demon/male_" + i + ".png");
				FEMALE_TEXTURES[i] = Bewitchment.id("textures/entity/living/demon/female_" + i + ".png");
			}
		}
		int variant = entity.getVariant();
		return entity.getEntityData().get(DemonEntity.MALE) ? MALE_TEXTURES[variant] : FEMALE_TEXTURES[variant];
	}

	@Override
	public void render(DemonEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
		model = mobEntity.getEntityData().get(DemonEntity.MALE) ? MALE_MODEL : FEMALE_MODEL;
		super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}
}
