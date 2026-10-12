/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class WitchAltarBlockEntityRenderer implements BlockEntityRenderer<WitchAltarBlockEntity> {
	@Override
	public void render(WitchAltarBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		matrices.translate(0.5, 1, 0.5);
		Direction direction = entity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
		float rotation = direction.toYRot() + ((direction == Direction.WEST || direction == Direction.EAST) ? 180 : 0); // Offset rotation when facing WEST or EAST
		matrices.mulPose(Axis.YP.rotationDegrees(rotation));
		ItemStack sword = entity.getItem(0);
		ItemStack pentacle = entity.getItem(1);
		ItemStack wand = entity.getItem(2);
		matrices.pushPose();
		renderStack(entity.getLevel(), matrices, vertexConsumers, light, overlay, 0.25f, -0.25f, sword, direction);
		matrices.popPose();
		matrices.pushPose();
		renderStack(entity.getLevel(), matrices, vertexConsumers, light, overlay, 0f, 0.25f, pentacle, direction);
		matrices.popPose();
		matrices.pushPose();
		renderStack(entity.getLevel(), matrices, vertexConsumers, light, overlay, -0.25f, -0.25f, wand, direction);
		matrices.popPose();
	}

	private void renderStack(Level world, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, float x, float z, ItemStack stack, Direction direction) {
		if (stack.is(BWTags.SKULLS)) {
			matrices.translate(x, 0.125, z);
			matrices.scale(0.5f, 0.5f, 0.5f);
		} else if (stack.getItem() instanceof BlockItem blockItem) {
			matrices.translate(x + 0.25, 0, z - 0.25);
			matrices.scale(0.5f, 0.5f, 0.5f);
			matrices.mulPose(Axis.YP.rotationDegrees(-90));
			BlockState state = blockItem.getBlock().defaultBlockState();
			if (state.getProperties().contains(BlockStateProperties.HORIZONTAL_FACING)) {
				state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, direction.getOpposite());
			}
			if (state.getProperties().contains(BlockStateProperties.FACING)) {
				state = state.setValue(BlockStateProperties.FACING, Direction.UP);
			}
			Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(matrices.last(), vertexConsumers.getBuffer(ItemBlockRenderTypes.getRenderType(state, false)), state, Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(state), 1, 1, 1, light, overlay);
			return;
		} else {
			matrices.translate(x, 0.01, z);
			matrices.scale(1 / 3f, 1 / 3f, 1 / 3f);
			matrices.mulPose(Axis.XP.rotationDegrees(90));
		}
		Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, matrices, vertexConsumers, world, 0);
	}
}
