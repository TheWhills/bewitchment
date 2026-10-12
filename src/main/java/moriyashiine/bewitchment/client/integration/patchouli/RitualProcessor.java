/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.patchouli;

import moriyashiine.bewitchment.common.recipe.RitualRecipe;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

@SuppressWarnings("ConstantConditions")
public class RitualProcessor implements IComponentProcessor {
	protected RitualRecipe recipe;

	@Override
	public void setup(Level level, IVariableProvider variables) {
		recipe = (RitualRecipe) level.getRecipeManager().byKey(new ResourceLocation(variables.get("recipe").asString())).filter(recipe -> recipe.getType().equals(BWRecipeTypes.RITUAL_RECIPE_TYPE)).orElseThrow(IllegalArgumentException::new);
	}

	@Override
	public IVariable process(Level level, String key) {
		switch (key) {
			case "header" -> {
				return IVariable.from(Component.translatable("ritual." + recipe.getId().toString().replaceAll(":", ".").replaceAll("/", ".")));
			}
			case "inner" -> {
				return IVariable.wrap(recipe.inner);
			}
			case "outer" -> {
				return IVariable.wrap(recipe.outer);
			}
			case "cost" -> {
				return IVariable.wrap("$(o)" + I18n.get("bewitchment.tooltip.cost", recipe.cost));
			}
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
