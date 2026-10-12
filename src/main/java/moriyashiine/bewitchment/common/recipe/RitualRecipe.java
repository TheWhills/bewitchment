/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import java.util.ArrayList;
import java.util.List;

public class RitualRecipe implements Recipe<Container> {
	private final ResourceLocation identifier;
	public final NonNullList<Ingredient> input;
	public final String inner, outer;
	public final RitualFunction ritualFunction;
	public final int cost, runningTime;

	public RitualRecipe(ResourceLocation identifier, NonNullList<Ingredient> input, String inner, String outer, RitualFunction ritualFunction, int cost, int runningTime) {
		this.identifier = identifier;
		this.input = input;
		this.inner = inner;
		this.outer = outer;
		this.ritualFunction = ritualFunction;
		this.cost = cost;
		this.runningTime = runningTime;
	}

	@Override
	public boolean matches(Container inv, Level world) {
		return matches(inv, input);
	}

	@Override
	public ItemStack assemble(Container inventory, RegistryAccess registryManager) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryManager) {
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation getId() {
		return identifier;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return BWRecipeTypes.RITUAL_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return BWRecipeTypes.RITUAL_RECIPE_TYPE;
	}

	public static boolean matches(Container inv, NonNullList<Ingredient> input) {
		List<ItemStack> checklist = new ArrayList<>();
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				checklist.add(stack);
			}
		}
		if (input.size() != checklist.size()) {
			return false;
		}
		for (Ingredient ingredient : input) {
			boolean found = false;
			for (ItemStack stack : checklist) {
				if (ingredient.test(stack)) {
					found = true;
					checklist.remove(stack);
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("ConstantConditions")
	public static class Serializer implements RecipeSerializer<RitualRecipe> {
		@Override
		public RitualRecipe fromJson(ResourceLocation id, JsonObject json) {
			NonNullList<Ingredient> ingredients = getIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
			if (ingredients.isEmpty()) {
				throw new JsonParseException("No ingredients for ritual recipe");
			} else if (ingredients.size() > 6) {
				throw new JsonParseException("Too many ingredients for ritual recipe");
			}
			String inner = GsonHelper.getAsString(json, "inner");
			if (inner.isEmpty()) {
				throw new JsonParseException("Inner circle is empty");
			}
			return new RitualRecipe(id, ingredients, inner, GsonHelper.getAsString(json, "outer", ""), BWRegistries.RITUAL_FUNCTION.get(new ResourceLocation(GsonHelper.getAsString(json, "ritual_function"))), GsonHelper.getAsInt(json, "cost"), GsonHelper.getAsInt(json, "running_time", 0));
		}

		@Override
		public RitualRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			NonNullList<Ingredient> defaultedList = NonNullList.withSize(buf.readVarInt(), Ingredient.EMPTY);
			defaultedList.replaceAll(ignored -> Ingredient.fromNetwork(buf));
			return new RitualRecipe(id, defaultedList, buf.readUtf(), buf.readUtf(), BWRegistries.RITUAL_FUNCTION.get(new ResourceLocation(buf.readUtf())), buf.readInt(), buf.readInt());
		}

		@Override
		public void write(FriendlyByteBuf buf, RitualRecipe recipe) {
			buf.writeVarInt(recipe.input.size());
			for (Ingredient ingredient : recipe.input) {
				ingredient.toNetwork(buf);
			}
			buf.writeUtf(recipe.inner);
			buf.writeUtf(recipe.outer);
			buf.writeUtf(BWRegistries.RITUAL_FUNCTION.getKey(recipe.ritualFunction).toString());
			buf.writeInt(recipe.cost);
			buf.writeInt(recipe.runningTime);
		}

		public static NonNullList<Ingredient> getIngredients(JsonArray json) {
			NonNullList<Ingredient> ingredients = NonNullList.create();
			for (int i = 0; i < json.size(); i++) {
				Ingredient ingredient = Ingredient.fromJson(json.get(i));
				if (!ingredient.isEmpty()) {
					ingredients.add(ingredient);
				}
			}
			return ingredients;
		}
	}
}
