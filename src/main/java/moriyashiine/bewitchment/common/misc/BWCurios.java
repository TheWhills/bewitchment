/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.misc;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

/** Small helpers over the Curios inventory (replacing TrinketsApi lookups). */
public final class BWCurios {
	private BWCurios() {
	}

	public static boolean isEquipped(LivingEntity entity, Item item) {
		return CuriosApi.getCuriosInventory(entity).map(inventory -> inventory.isEquipped(item)).orElse(false);
	}

	/** The first equipped stack of {@code item}, or {@link ItemStack#EMPTY}. */
	public static ItemStack findFirst(LivingEntity entity, Item item) {
		return CuriosApi.getCuriosInventory(entity).resolve().flatMap(inventory -> inventory.findFirstCurio(item)).map(SlotResult::stack).orElse(ItemStack.EMPTY);
	}
}
