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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class AthameDropRecipe implements Recipe<Container> {
	private final ResourceLocation identifier;
	public final EntityType<?> entity_type;
	private final ItemStack output;
	public final float chance;

	public AthameDropRecipe(ResourceLocation id, EntityType<?> entity_type, ItemStack output, float chance) {
		this.identifier = id;
		this.entity_type = entity_type;
		this.output = output;
		this.chance = chance;
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
		return BWRecipeTypes.ATHAME_DROP_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return BWRecipeTypes.ATHAME_DROP_RECIPE_TYPE;
	}

	public static class Serializer implements RecipeSerializer<AthameDropRecipe> {
		@Override
		public AthameDropRecipe fromJson(ResourceLocation id, JsonObject json) {
			return new AthameDropRecipe(id, BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(GsonHelper.getAsString(json, "entity_type"))), ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result")), GsonHelper.getAsFloat(json, "chance"));
		}

		@Override
		public AthameDropRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			return new AthameDropRecipe(id, BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(buf.readUtf())), buf.readItem(), buf.readFloat());
		}

		@Override
		public void write(FriendlyByteBuf buf, AthameDropRecipe recipe) {
			buf.writeUtf(BuiltInRegistries.ENTITY_TYPE.getKey(recipe.entity_type).toString());
			buf.writeItem(recipe.getResultItem(null));
			buf.writeFloat(recipe.chance);
		}
	}
}
