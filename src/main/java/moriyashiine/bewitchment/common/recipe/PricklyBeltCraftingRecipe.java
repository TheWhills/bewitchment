/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.recipe;

import moriyashiine.bewitchment.common.item.PricklyBeltItem;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class PricklyBeltCraftingRecipe extends CustomRecipe {
	public PricklyBeltCraftingRecipe(ResourceLocation id) {
		super(id, CraftingBookCategory.EQUIPMENT);
	}

	@Override
	public boolean matches(CraftingContainer inventory, Level world) {
		boolean foundPricklyBelt = false, foundPotion = false;
		int foundItems = 0;
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (stack.getItem() instanceof PricklyBeltItem) {
				if (!foundPricklyBelt) {
					foundPricklyBelt = true;
				}
				foundItems++;
			} else if (stack.getItem() == Items.POTION && (!PotionUtils.getMobEffects(stack).isEmpty() || !PotionUtils.getCustomEffects(stack).isEmpty())) {
				if (!foundPotion) {
					foundPotion = true;
				}
				foundItems++;
			}
		}
		return foundPricklyBelt && foundPotion && foundItems == 2;
	}

	@Override
	public ItemStack craft(CraftingContainer inventory, RegistryAccess registryManager) {
		ItemStack pricklyBelt = null, potion = null;
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (stack.getItem() instanceof PricklyBeltItem) {
				pricklyBelt = stack.copy();
			} else if (stack.getItem() == Items.POTION) {
				potion = stack.copy();
			}
		}
		int uses = 1;
		List<MobEffectInstance> effects = !PotionUtils.getCustomEffects(potion).isEmpty() ? PotionUtils.getCustomEffects(potion) : PotionUtils.getMobEffects(potion);
		for (int i = 0; i < effects.size(); i++) {
			MobEffectInstance effect = effects.get(i);
			int duration = effect.getDuration();
			int amplifier = effect.getAmplifier();
			while (duration > 600) {
				uses++;
				duration /= 2;
			}
			while (amplifier > 0) {
				uses += 2;
				amplifier--;
			}
			effects.set(i, new MobEffectInstance(effect.getEffect(), duration, amplifier, effect.isAmbient(), effect.isVisible(), effect.showIcon()));
		}
		if (potion.getOrCreateTag().contains("PolymorphUUID")) {
			pricklyBelt.getOrCreateTag().putUUID("PolymorphUUID", potion.getTag().getUUID("PolymorphUUID"));
			pricklyBelt.getOrCreateTag().putString("PolymorphName", potion.getTag().getString("PolymorphName"));
		}
		PotionUtils.setCustomEffects(pricklyBelt, effects);
		pricklyBelt.getOrCreateTag().putInt("PotionUses", uses);
		return pricklyBelt;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return BWRecipeTypes.PRICKLY_BELT_CRAFTING_SERIALIZER.get();
	}
}
