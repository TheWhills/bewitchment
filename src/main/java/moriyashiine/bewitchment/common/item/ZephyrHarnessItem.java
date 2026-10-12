/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import top.theillusivec4.curios.api.SlotContext;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.UUID;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

public class ZephyrHarnessItem extends AccessoryItem {
	public ZephyrHarnessItem(Properties settings) {
		super(settings);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create(super.getAttributeModifiers(slotContext, uuid, stack));
		modifiers.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Accessory modifier", 0.1, AttributeModifier.Operation.ADDITION));
		return modifiers;
	}
}
