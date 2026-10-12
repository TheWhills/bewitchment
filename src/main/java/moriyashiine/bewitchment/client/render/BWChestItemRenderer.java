/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import moriyashiine.bewitchment.common.block.entity.BWChestBlockEntity;
import moriyashiine.bewitchment.common.item.BWChestItem;
import moriyashiine.bewitchment.common.registry.BWBlockEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/** Renders chest items by drawing a throwaway chest block entity, like vanilla chests. */
public class BWChestItemRenderer extends BlockEntityWithoutLevelRenderer {
	private static BWChestItemRenderer instance;

	private BWChestItemRenderer() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
	}

	public static BWChestItemRenderer get() {
		if (instance == null) {
			instance = new BWChestItemRenderer();
		}
		return instance;
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		if (stack.getItem() instanceof BWChestItem chest) {
			BWChestBlockEntity blockEntity = new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST.get(), BlockPos.ZERO, chest.getBlock().defaultBlockState(), chest.getChestType(), chest.isTrapped());
			Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(blockEntity, matrices, vertexConsumers, light, overlay);
		}
	}
}
