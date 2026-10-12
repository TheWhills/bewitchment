/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.integration.patchouli;

import moriyashiine.bewitchment.common.recipe.CurseRecipe;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

@SuppressWarnings("ConstantConditions")
public class CurseProcessor implements IComponentProcessor {
	protected CurseRecipe recipe;

	@Override
	public void setup(Level level, IVariableProvider variables) {
		recipe = (CurseRecipe) level.getRecipeManager().byKey(new ResourceLocation(variables.get("recipe").asString())).filter(recipe -> recipe.getType().equals(BWRecipeTypes.CURSE_RECIPE_TYPE.get())).orElseThrow(IllegalArgumentException::new);
	}

	@Override
	public IVariable process(Level level, String key) {
		if (key.equals("header")) {
			return IVariable.from(Component.translatable("curse." + BWRegistries.CURSE.getKey(recipe.curse).toString().replace(":", ".")));
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
