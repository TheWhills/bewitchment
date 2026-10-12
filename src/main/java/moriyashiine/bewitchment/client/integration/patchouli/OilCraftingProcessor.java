/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.patchouli;

import moriyashiine.bewitchment.common.recipe.OilRecipe;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

@SuppressWarnings("ConstantConditions")
public class OilCraftingProcessor implements IComponentProcessor {
	protected OilRecipe recipe;

	@Override
	public void setup(Level level, IVariableProvider variables) {
		recipe = (OilRecipe) level.getRecipeManager().byKey(new ResourceLocation(variables.get("recipe").asString())).filter(recipe -> recipe.getType().equals(BWRecipeTypes.OIL_RECIPE_TYPE)).orElseThrow(IllegalArgumentException::new);
	}

	@Override
	public IVariable process(Level level, String key) {
		if (key.equals("output")) {
			return IVariable.from(recipe.getResultItem(level.registryAccess()));
		} else if (key.equals("header")) {
			return IVariable.from(recipe.getResultItem(level.registryAccess()).getHoverName());
		}
		for (int i = 0; i < recipe.input.size(); i++) {
			if (key.equals("ingredient" + i)) {
				ItemStack[] stack = recipe.input.get(i).getItems();
				return stack.length > 0 ? IVariable.from(stack[0]) : null;
			}

		}
		return null;
	}
}
