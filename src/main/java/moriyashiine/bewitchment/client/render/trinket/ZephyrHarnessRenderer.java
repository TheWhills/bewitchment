package moriyashiine.bewitchment.client.render.trinket;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.equipment.trinket.ZephyrHarnessModel;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ZephyrHarnessRenderer implements TrinketRenderer {
	private static final ResourceLocation TEXTURE = Bewitchment.id("textures/entity/trinket/zephyr_harness.png");
	private static Model model;

	@Override
	public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, PoseStack matrices, MultiBufferSource vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (model == null) {
			model = new ZephyrHarnessModel(BewitchmentClient.getPart(BewitchmentClient.ZEPHYR_HARNESS_MODEL_LAYER));
		}
		TrinketRenderer.translateToChest(matrices, (PlayerModel<AbstractClientPlayer>) contextModel, (AbstractClientPlayer) entity);
		model.renderToBuffer(matrices, vertexConsumers.getBuffer(RenderType.armorCutoutNoCull(TEXTURE)), light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
	}
}
