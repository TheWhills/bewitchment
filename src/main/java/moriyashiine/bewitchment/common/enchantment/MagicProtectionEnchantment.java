/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.enchantment;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;

public class MagicProtectionEnchantment extends ProtectionEnchantment {
	public MagicProtectionEnchantment(Rarity weight, Type type, EquipmentSlot... equipmentSlots) {
		super(weight, type, equipmentSlots);
	}

	@Override
	public int getDamageProtection(int level, DamageSource source) {
		return source.is(DamageTypeTags.WITCH_RESISTANT_TO) ? level * 2 : 0;
	}
}
