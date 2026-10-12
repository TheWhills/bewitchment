/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

/**
 * Base for Bewitchment accessories worn in Curios slots (necklace, belt, feet). Replaces
 * Trinkets' TrinketItem, including equipping by right-clicking the item.
 */
public class AccessoryItem extends Item implements ICurioItem {
	public AccessoryItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
		return true;
	}
}
