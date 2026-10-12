package moriyashiine.bewitchment.client.render.trinket;

import com.mojang.blaze3d.vertex.PoseStack;
import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.equipment.trinket.DruidBandModel;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.client.model.EntityModel;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import top.theillusivec4.curios.api.SlotContext;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class DruidBandRenderer implements ICurioRenderer {
	private static final ResourceLocation TEXTURE = Bewitchment.id("textures/entity/trinket/druid_band.png");
	private static Model model;

	@Override
	public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrices, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource vertexConsumers, int light, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		LivingEntity entity = slotContext.entity();
		if (!(renderLayerParent.getModel() instanceof HumanoidModel<?> contextModel)) {
			return;
		}
		if (model == null) {
			model = new DruidBandModel(BewitchmentClient.getPart(BewitchmentClient.DRUID_BAND_MODEL_LAYER));
		}
		AccessoryTransforms.translateToLeftLeg(matrices, contextModel, entity);
		model.renderToBuffer(matrices, vertexConsumers.getBuffer(RenderType.armorCutoutNoCull(TEXTURE)), light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
	}
}
