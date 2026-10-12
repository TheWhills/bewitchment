/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class IncenseRecipe implements Recipe<Container> {
	private final ResourceLocation identifier;
	public final NonNullList<Ingredient> input;
	public final MobEffect effect;
	public final int amplifier;

	public IncenseRecipe(ResourceLocation identifier, NonNullList<Ingredient> input, MobEffect effect, int amplifier) {
		this.identifier = identifier;
		this.input = input;
		this.effect = effect;
		this.amplifier = amplifier;
	}

	@Override
	public boolean matches(Container inv, Level world) {
		return RitualRecipe.matches(inv, input);
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
		return BWRecipeTypes.INCENSE_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return BWRecipeTypes.INCENSE_RECIPE_TYPE;
	}

	@SuppressWarnings("ConstantConditions")
	public static class Serializer implements RecipeSerializer<IncenseRecipe> {
		@Override
		public IncenseRecipe fromJson(ResourceLocation id, JsonObject json) {
			NonNullList<Ingredient> ingredients = RitualRecipe.Serializer.getIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
			if (ingredients.isEmpty()) {
				throw new JsonParseException("No ingredients for incense recipe");
			} else if (ingredients.size() > 4) {
				throw new JsonParseException("Too many ingredients for incense recipe");
			}
			return new IncenseRecipe(id, ingredients, BuiltInRegistries.MOB_EFFECT.get(new ResourceLocation(GsonHelper.getAsString(json, "effect"))), GsonHelper.getAsInt(json, "amplifier", 0));
		}

		@Override
		public IncenseRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			NonNullList<Ingredient> defaultedList = NonNullList.withSize(buf.readVarInt(), Ingredient.EMPTY);
			defaultedList.replaceAll(ignored -> Ingredient.fromNetwork(buf));
			return new IncenseRecipe(id, defaultedList, BuiltInRegistries.MOB_EFFECT.get(new ResourceLocation(buf.readUtf())), buf.readInt());
		}

		@Override
		public void write(FriendlyByteBuf buf, IncenseRecipe recipe) {
			buf.writeVarInt(recipe.input.size());
			for (Ingredient ingredient : recipe.input) {
				ingredient.toNetwork(buf);
			}
			buf.writeUtf(BuiltInRegistries.MOB_EFFECT.getKey(recipe.effect).toString());
			buf.writeInt(recipe.amplifier);
		}
	}
}
