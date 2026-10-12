/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import moriyashiine.bewitchment.common.block.entity.PoppetShelfBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class PoppetShelfBlockEntityRenderer implements BlockEntityRenderer<PoppetShelfBlockEntity> {
	@Override
	public void render(PoppetShelfBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		Direction direction = entity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
		float rotation = -direction.toYRot();
		matrices.translate(0.5f, 0, 0.5f);
		matrices.mulPose(Axis.YP.rotationDegrees(rotation));
		matrices.scale(1 / 5f, 1 / 5f, 1 / 5f);
		renderRow(entity.getLevel(), entity.clientInventory.get(0), entity.clientInventory.get(1), entity.clientInventory.get(2), 0.8f, matrices, vertexConsumers, light, overlay);
		renderRow(entity.getLevel(), entity.clientInventory.get(3), entity.clientInventory.get(4), entity.clientInventory.get(5), 0.5f, matrices, vertexConsumers, light, overlay);
		renderRow(entity.getLevel(), entity.clientInventory.get(6), entity.clientInventory.get(7), entity.clientInventory.get(8), 0.2f, matrices, vertexConsumers, light, overlay);
	}

	private static void renderRow(Level world, ItemStack one, ItemStack two, ItemStack three, float yOffset, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		matrices.pushPose();
		double xOffset = 0.32 * 5;
		double zOffset = 0.45 * 5;
		yOffset *= 5;
		matrices.translate(0, yOffset, zOffset);
		Minecraft.getInstance().getItemRenderer().renderStatic(two, ItemDisplayContext.FIXED, light, overlay, matrices, vertexConsumers, world, 0);
		matrices.translate(xOffset, 0, 0);
		Minecraft.getInstance().getItemRenderer().renderStatic(one, ItemDisplayContext.FIXED, light, overlay, matrices, vertexConsumers, world, 0);
		matrices.translate(-xOffset * 2, 0, 0);
		Minecraft.getInstance().getItemRenderer().renderStatic(three, ItemDisplayContext.FIXED, light, overlay, matrices, vertexConsumers, world, 0);
		matrices.popPose();
	}
}
