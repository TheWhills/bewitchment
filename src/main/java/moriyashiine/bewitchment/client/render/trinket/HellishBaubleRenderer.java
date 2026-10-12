package moriyashiine.bewitchment.client.render.trinket;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class HellishBaubleRenderer implements TrinketRenderer {
	@Override
	public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, PoseStack matrices, MultiBufferSource vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		TrinketRenderer.translateToChest(matrices, (PlayerModel<AbstractClientPlayer>) contextModel, (AbstractClientPlayer) entity);
		matrices.translate(0, -1 / 4.25f, 1 / 48f);
		matrices.scale(1 / 3f, 1 / 3f, 1 / 3f);
		matrices.mulPose(Axis.XP.rotationDegrees(180));
		Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, entity.level(), 0);
	}
}
