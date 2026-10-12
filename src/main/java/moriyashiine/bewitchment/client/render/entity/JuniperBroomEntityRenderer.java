/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.entity;

import moriyashiine.bewitchment.api.client.renderer.BroomEntityRenderer;
import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class JuniperBroomEntityRenderer extends BroomEntityRenderer<BroomEntity> {
	private static final ResourceLocation TEXTURE = Bewitchment.id("textures/entity/broom/juniper.png");

	public JuniperBroomEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTexture(BroomEntity entity) {
		return TEXTURE;
	}
}
