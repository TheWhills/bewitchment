/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import java.util.UUID;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

public class ZephyrHarnessItem extends TrinketItem {
	public ZephyrHarnessItem(Properties settings) {
		super(settings);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
		Multimap<Attribute, AttributeModifier> modifiers = super.getModifiers(stack, slot, entity, uuid);
		modifiers.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Trinket modifier", 0.1, AttributeModifier.Operation.ADDITION));
		return modifiers;
	}
}
