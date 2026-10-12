/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.recipe;

import moriyashiine.bewitchment.api.item.PoppetItem;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class TaglockCraftingRecipe extends CustomRecipe {
	public TaglockCraftingRecipe(ResourceLocation id) {
		super(id, CraftingBookCategory.EQUIPMENT);
	}

	@Override
	public boolean matches(CraftingContainer inventory, Level world) {
		boolean foundTaglock = false, foundCraftable = false;
		int foundItems = 0;
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (stack.getItem() instanceof TaglockItem && TaglockItem.hasTaglock(stack)) {
				if (!foundTaglock) {
					foundTaglock = true;
				}
				foundItems++;
			} else if (isTaglockCraftable(stack)) {
				if (!foundCraftable) {
					foundCraftable = true;
				}
				foundItems++;
			}
		}
		return foundTaglock && foundCraftable && foundItems == 2;
	}

	@Override
	public ItemStack assemble(CraftingContainer inventory, RegistryAccess registryManager) {
		ItemStack taglock = null, craftedStack = null;
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (stack.getItem() instanceof TaglockItem) {
				taglock = stack.copy();
			} else if (isTaglockCraftable(stack)) {
				craftedStack = stack.copy();
			}
		}
		return TaglockItem.copyTo(taglock, craftedStack);
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return BWRecipeTypes.TAGLOCK_CRAFTING_SERIALIZER.get();
	}

	private static boolean isTaglockCraftable(ItemStack stack) {
		if (stack.getItem() instanceof PoppetItem) {
			return TaglockItem.getTaglockUUID(stack) == null;
		}
		return false;
	}
}
