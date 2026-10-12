/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.equipment.armor.WitchArmorModel;
import moriyashiine.bewitchment.common.item.WitchArmorItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

/**
 * Supplies {@link WitchArmorModel} for witch armor, set up the same way the Fabric
 * ArmorRenderer did: hood or hat on the head, robes on body and arms, pants with a skirt
 * that only hangs low when no boots are worn, and a separate legs-only model for boots.
 */
public final class WitchArmorClientExtensions implements IClientItemExtensions {
	public static final WitchArmorClientExtensions INSTANCE = new WitchArmorClientExtensions();

	private WitchArmorModel<LivingEntity> armorModel;
	private WitchArmorModel<LivingEntity> bootsModel;

	private WitchArmorClientExtensions() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
		if (armorModel == null) {
			armorModel = new WitchArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(BewitchmentClient.WITCH_ARMOR_MODEL_LAYER));
			bootsModel = new WitchArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(BewitchmentClient.WITCH_ARMOR_MODEL_LAYER));
		}
		HumanoidModel<LivingEntity> parent = (HumanoidModel<LivingEntity>) original;
		if (slot == EquipmentSlot.FEET) {
			parent.copyPropertiesTo(bootsModel);
			bootsModel.setAllVisible(false);
			bootsModel.leftLeg.visible = true;
			bootsModel.rightLeg.visible = true;
			return bootsModel;
		}
		boolean hat = stack.getItem() instanceof WitchArmorItem armor && armor.isHat();
		parent.copyPropertiesTo(armorModel);
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
		return armorModel;
	}
}
