/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.recipe;

import com.google.gson.JsonObject;
import moriyashiine.bewitchment.common.registry.BWRecipeTypes;
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

public class CauldronBrewingRecipe implements Recipe<Container> {
	private final ResourceLocation identifier;
	public final Ingredient input;
	public final MobEffect output;
	public final int time;

	public CauldronBrewingRecipe(ResourceLocation identifier, Ingredient input, MobEffect output, int time) {
		this.identifier = identifier;
		this.input = input;
		this.output = output;
		this.time = time;
	}

	@Override
	public boolean matches(Container inv, Level world) {
		return false;
	}

	@Override
	public ItemStack assemble(Container inventory, RegistryAccess registryManager) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return false;
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
		return BWRecipeTypes.CAULDRON_BREWING_RECIPE_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return BWRecipeTypes.CAULDRON_BREWING_RECIPE_TYPE.get();
	}

	@SuppressWarnings("ConstantConditions")
	public static class Serializer implements RecipeSerializer<CauldronBrewingRecipe> {
		@Override
		public CauldronBrewingRecipe fromJson(ResourceLocation id, JsonObject json) {
			return new CauldronBrewingRecipe(id, Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient")), BuiltInRegistries.MOB_EFFECT.get(new ResourceLocation(GsonHelper.getAsString(json, "effect"))), GsonHelper.getAsInt(json, "time"));
		}

		@Override
		public CauldronBrewingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			return new CauldronBrewingRecipe(id, Ingredient.fromNetwork(buf), BuiltInRegistries.MOB_EFFECT.get(new ResourceLocation(buf.readUtf())), buf.readInt());
		}

		@Override
		public void write(FriendlyByteBuf buf, CauldronBrewingRecipe recipe) {
			recipe.input.toNetwork(buf);
			buf.writeUtf(BuiltInRegistries.MOB_EFFECT.getKey(recipe.output).toString());
			buf.writeInt(recipe.time);
		}
	}
}
