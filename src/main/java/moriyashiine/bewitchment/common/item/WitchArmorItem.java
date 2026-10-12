/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.client.render.WitchArmorClientExtensions;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

/**
 * Witch robes, hoods, hats and the harbinger boots: rendered with {@code WitchArmorModel}
 * instead of the vanilla armor layers (on Fabric this was an ArmorRenderer).
 */
public class WitchArmorItem extends ArmorItem {
	private static final ResourceLocation WITCH_HAT_VARIANT_TEXTURE = Bewitchment.id("textures/entity/armor/witch_hat_variant.png");

	private final ResourceLocation texture;
	private final boolean hat;

	public WitchArmorItem(ArmorMaterial material, Type type, Properties properties, ResourceLocation texture, boolean hat) {
		super(material, type, properties);
		this.texture = texture;
		this.hat = hat;
	}

	public boolean isHat() {
		return hat;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		boolean variant = hat && stack.getHoverName().getString().toLowerCase().contains("faith");
		return (variant ? WITCH_HAT_VARIANT_TEXTURE : texture).toString();
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(WitchArmorClientExtensions.INSTANCE);
	}
}
