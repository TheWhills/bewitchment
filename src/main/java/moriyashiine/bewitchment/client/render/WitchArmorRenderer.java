/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.equipment.armor.WitchArmorModel;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class WitchArmorRenderer implements ArmorRenderer {
	private static final ResourceLocation WITCH_HAT_VARIANT_TEXTURE = Bewitchment.id("textures/entity/armor/witch_hat_variant.png");
	private static WitchArmorModel<LivingEntity> armorModel;
	private static WitchArmorModel<LivingEntity> bootsModel;

	private final ResourceLocation texture;
	private final Item hatItem;

	public WitchArmorRenderer(ResourceLocation texture, @Nullable Item hatItem) {
		this.texture = texture;
		this.hatItem = hatItem;
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
		if (armorModel == null) {
			armorModel = new WitchArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(BewitchmentClient.WITCH_ARMOR_MODEL_LAYER));
			bootsModel = new WitchArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(BewitchmentClient.WITCH_ARMOR_MODEL_LAYER));
		}
		if (slot == EquipmentSlot.FEET) {
			contextModel.copyPropertiesTo(bootsModel);
			bootsModel.setAllVisible(false);
			bootsModel.leftLeg.visible = true;
			bootsModel.rightLeg.visible = true;
			ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, bootsModel, texture);
		} else {
			boolean hat = stack.getItem() == hatItem;
			contextModel.copyPropertiesTo(armorModel);
			armorModel.setAllVisible(false);
			armorModel.hood01.visible = !hat;
			armorModel.hat1.visible = hat;
			armorModel.head.visible = slot == EquipmentSlot.HEAD;
			armorModel.body.visible = slot == EquipmentSlot.CHEST;
			armorModel.leftArm.visible = slot == EquipmentSlot.CHEST;
			armorModel.rightArm.visible = slot == EquipmentSlot.CHEST;
			armorModel.leftLeg.visible = slot == EquipmentSlot.LEGS;
			armorModel.rightLeg.visible = slot == EquipmentSlot.LEGS;
			armorModel.lowerLeftSkirt.visible = entity.getItemBySlot(EquipmentSlot.FEET).isEmpty();
			armorModel.lowerRightSkirt.visible = armorModel.lowerLeftSkirt.visible;
			ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, armorModel, hat && stack.getHoverName().getString().toLowerCase().contains("faith") ? WITCH_HAT_VARIANT_TEXTURE : texture);
		}
	}
}
