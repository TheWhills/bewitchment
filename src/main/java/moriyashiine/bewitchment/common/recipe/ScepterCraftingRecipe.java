/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.recipe;

import moriyashiine.bewitchment.common.item.ScepterItem;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

@SuppressWarnings("ConstantConditions")
public class ScepterCraftingRecipe extends CustomRecipe {
	public ScepterCraftingRecipe(ResourceLocation id) {
		super(id, CraftingBookCategory.EQUIPMENT);
	}

	@Override
	public boolean matches(CraftingContainer inventory, Level world) {
		boolean foundScepter = false, foundPotion = false;
		int foundItems = 0;
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (stack.getItem() instanceof ScepterItem) {
				if (!foundScepter) {
					foundScepter = true;
				}
				foundItems++;
			} else if (stack.getItem() instanceof SplashPotionItem && (!PotionUtils.getMobEffects(stack).isEmpty() || !PotionUtils.getCustomEffects(stack).isEmpty())) {
				if (!foundPotion) {
					foundPotion = true;
				}
				foundItems++;
			}
		}
		return foundScepter && foundPotion && foundItems == 2;
	}

	@Override
	public ItemStack craft(CraftingContainer inventory, RegistryAccess registryManager) {
		ItemStack scepter = null, potion = null;
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (stack.getItem() instanceof ScepterItem) {
				scepter = stack.copy();
			} else if (stack.getItem() instanceof SplashPotionItem) {
				potion = stack.copy();
			}
		}
		scepter.getOrCreateTag().putInt("PotionUses", 4);
		PotionUtils.setCustomEffects(scepter, !PotionUtils.getCustomEffects(potion).isEmpty() ? PotionUtils.getCustomEffects(potion) : PotionUtils.getMobEffects(potion));
		if (potion.getOrCreateTag().contains("PolymorphUUID")) {
			scepter.getOrCreateTag().putUUID("PolymorphUUID", potion.getOrCreateTag().getUUID("PolymorphUUID"));
			scepter.getOrCreateTag().putString("PolymorphName", potion.getOrCreateTag().getString("PolymorphName"));
		}
		return scepter;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return BWRecipeTypes.SCEPTER_CRAFTING_SERIALIZER.get();
	}
}
