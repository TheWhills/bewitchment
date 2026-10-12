/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import moriyashiine.bewitchment.common.block.entity.BrazierBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BrazierBlockEntityRenderer implements BlockEntityRenderer<BrazierBlockEntity> {
	@Override
	public void render(BrazierBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		double yOffset = entity.getBlockState().getValue(BlockStateProperties.HANGING) ? -0.815 : 0;
		matrices.translate(0.5, 0.925 + yOffset, 0.5);
		matrices.scale(0.5f, 0.5f, 0.5f);
		for (int i = 0; i < 4; i++) {
			matrices.translate(0, 0.06, 0);
			matrices.pushPose();
			matrices.mulPose(Axis.XP.rotationDegrees((90 + (180 * i)) % 360));
			matrices.mulPose(Axis.ZP.rotationDegrees(90 * i));
			Minecraft.getInstance().getItemRenderer().renderStatic(entity.getItem(i), ItemDisplayContext.FIXED, light, overlay, matrices, vertexConsumers, entity.getLevel(), 0);
			matrices.popPose();
		}
	}
}
