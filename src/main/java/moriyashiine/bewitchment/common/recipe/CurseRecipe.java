/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.registry.BWObjects;
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

public class CurseRecipe implements Recipe<Container> {
	private final ResourceLocation identifier;
	public final NonNullList<Ingredient> input;
	public final Curse curse;
	public final int cost;

	public CurseRecipe(ResourceLocation identifier, NonNullList<Ingredient> input, Curse curse, int cost) {
		this.identifier = identifier;
		this.input = input;
		this.curse = curse;
		this.cost = cost;
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
		return BWRecipeTypes.CURSE_RECIPE_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return BWRecipeTypes.CURSE_RECIPE_TYPE.get();
	}

	@SuppressWarnings("ConstantConditions")
	public static class Serializer implements RecipeSerializer<CurseRecipe> {
		private static final ItemStack TAGLOCK = new ItemStack(BWObjects.TAGLOCK.get());

		@Override
		public CurseRecipe fromJson(ResourceLocation id, JsonObject json) {
			NonNullList<Ingredient> ingredients = RitualRecipe.Serializer.getIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
			if (ingredients.isEmpty()) {
				throw new JsonParseException("No ingredients for curse recipe");
			} else if (ingredients.size() > 4) {
				throw new JsonParseException("Too many ingredients for curse recipe");
			}
			boolean foundTaglock = false;
			for (Ingredient ingredient : ingredients) {
				if (ingredient.test(TAGLOCK)) {
					foundTaglock = true;
					break;
				}
			}
			if (!foundTaglock) {
				throw new JsonParseException("Taglock not found in curse recipe");
			}
			return new CurseRecipe(id, ingredients, BWRegistries.CURSE.get(new ResourceLocation(GsonHelper.getAsString(json, "curse"))), GsonHelper.getAsInt(json, "cost"));
		}

		@Override
		public CurseRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			NonNullList<Ingredient> defaultedList = NonNullList.withSize(buf.readVarInt(), Ingredient.EMPTY);
			defaultedList.replaceAll(ignored -> Ingredient.fromNetwork(buf));
			return new CurseRecipe(id, defaultedList, BWRegistries.CURSE.get(new ResourceLocation(buf.readUtf())), buf.readInt());
		}

		@Override
		public void write(FriendlyByteBuf buf, CurseRecipe recipe) {
			buf.writeVarInt(recipe.input.size());
			for (Ingredient ingredient : recipe.input) {
				ingredient.toNetwork(buf);
			}
			buf.writeUtf(BWRegistries.CURSE.getKey(recipe.curse).toString());
			buf.writeInt(recipe.cost);
		}
	}
}
