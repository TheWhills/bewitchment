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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class AthameStrippingRecipe implements Recipe<Container> {
	private final ResourceLocation identifier;
	public final Block log, strippedLog;
	private final ItemStack output;

	public AthameStrippingRecipe(ResourceLocation identifier, Block log, Block strippedLog, ItemStack output) {
		this.identifier = identifier;
		this.log = log;
		this.strippedLog = strippedLog;
		this.output = output;
	}

	@Override
	public boolean matches(Container inv, Level world) {
		return false;
	}

	@Override
	public ItemStack assemble(Container inventory, RegistryAccess registryManager) {
		return output;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return false;
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
		return BWRecipeTypes.ATHAME_STRIPPING_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return BWRecipeTypes.ATHAME_STRIPPING_RECIPE_TYPE;
	}

	public static class Serializer implements RecipeSerializer<AthameStrippingRecipe> {
		@Override
		public AthameStrippingRecipe fromJson(ResourceLocation id, JsonObject json) {
			return new AthameStrippingRecipe(id, BuiltInRegistries.BLOCK.get(new ResourceLocation(GsonHelper.getAsString(json, "log"))), BuiltInRegistries.BLOCK.get(new ResourceLocation(GsonHelper.getAsString(json, "stripped_log"))), ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result")));
		}

		@Override
		public AthameStrippingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			return new AthameStrippingRecipe(id, BuiltInRegistries.BLOCK.get(new ResourceLocation(buf.readUtf())), BuiltInRegistries.BLOCK.get(new ResourceLocation(buf.readUtf())), buf.readItem());
		}

		@Override
		public void write(FriendlyByteBuf buf, AthameStrippingRecipe recipe) {
			buf.writeUtf(BuiltInRegistries.BLOCK.getKey(recipe.log).toString());
			buf.writeUtf(BuiltInRegistries.BLOCK.getKey(recipe.strippedLog).toString());
			buf.writeItem(recipe.getResultItem(null));
		}
	}
}
