/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
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
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class OilRecipe implements Recipe<Container> {
	private final ResourceLocation identifier;
	public final NonNullList<Ingredient> input;
	private final ItemStack output;
	public final int color;

	public OilRecipe(ResourceLocation identifier, NonNullList<Ingredient> input, ItemStack output, int color) {
		this.identifier = identifier;
		this.input = input;
		this.output = output;
		this.color = color;
	}

	@Override
	public boolean matches(Container inv, Level world) {
		return RitualRecipe.matches(inv, input);
	}

	@Override
	public ItemStack assemble(Container inventory, RegistryAccess registryManager) {
		return output;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryManager) {
		return output;
	}

	@Override
	public ResourceLocation getId() {
		return identifier;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return BWRecipeTypes.OIL_RECIPE_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return BWRecipeTypes.OIL_RECIPE_TYPE.get();
	}

	public static class Serializer implements RecipeSerializer<OilRecipe> {
		@Override
		public OilRecipe fromJson(ResourceLocation id, JsonObject json) {
			NonNullList<Ingredient> ingredients = RitualRecipe.Serializer.getIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
			if (ingredients.isEmpty()) {
				throw new JsonParseException("No ingredients for oil recipe");
			} else if (ingredients.size() > 4) {
				throw new JsonParseException("Too many ingredients for oil recipe");
			}
			return new OilRecipe(id, ingredients, ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result")), GsonHelper.getAsInt(json, "color"));
		}

		@Override
		public OilRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			NonNullList<Ingredient> defaultedList = NonNullList.withSize(buf.readVarInt(), Ingredient.EMPTY);
			defaultedList.replaceAll(ignored -> Ingredient.fromNetwork(buf));
			return new OilRecipe(id, defaultedList, buf.readItem(), buf.readInt());
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, OilRecipe recipe) {
			buf.writeVarInt(recipe.input.size());
			for (Ingredient ingredient : recipe.input) {
				ingredient.toNetwork(buf);
			}
			buf.writeItem(recipe.getResultItem(null));
			buf.writeInt(recipe.color);
		}
	}
}
