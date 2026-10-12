/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.recipe.*;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BWRecipeTypes {
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Bewitchment.MOD_ID);
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Bewitchment.MOD_ID);

	public static final RegistryObject<RecipeSerializer<AthameStrippingRecipe>> ATHAME_STRIPPING_RECIPE_SERIALIZER = create("athame_stripping", () -> new AthameStrippingRecipe.Serializer());
	public static final RegistryObject<RecipeType<AthameStrippingRecipe>> ATHAME_STRIPPING_RECIPE_TYPE = create("athame_stripping");

	public static final RegistryObject<RecipeSerializer<AthameDropRecipe>> ATHAME_DROP_RECIPE_SERIALIZER = create("athame_drop", () -> new AthameDropRecipe.Serializer());
	public static final RegistryObject<RecipeType<AthameDropRecipe>> ATHAME_DROP_RECIPE_TYPE = create("athame_drop");

	public static final RegistryObject<RecipeSerializer<RitualRecipe>> RITUAL_RECIPE_SERIALIZER = create("ritual_recipe", () -> new RitualRecipe.Serializer());
	public static final RegistryObject<RecipeType<RitualRecipe>> RITUAL_RECIPE_TYPE = create("ritual_recipe");

	public static final RegistryObject<RecipeSerializer<OilRecipe>> OIL_RECIPE_SERIALIZER = create("oil_recipe", () -> new OilRecipe.Serializer());
	public static final RegistryObject<RecipeType<OilRecipe>> OIL_RECIPE_TYPE = create("oil_recipe");

	public static final RegistryObject<RecipeSerializer<CauldronBrewingRecipe>> CAULDRON_BREWING_RECIPE_SERIALIZER = create("cauldron_brewing_recipe", () -> new CauldronBrewingRecipe.Serializer());
	public static final RegistryObject<RecipeType<CauldronBrewingRecipe>> CAULDRON_BREWING_RECIPE_TYPE = create("cauldron_brewing_recipe");

	public static final RegistryObject<RecipeSerializer<IncenseRecipe>> INCENSE_RECIPE_SERIALIZER = create("incense_recipe", () -> new IncenseRecipe.Serializer());
	public static final RegistryObject<RecipeType<IncenseRecipe>> INCENSE_RECIPE_TYPE = create("incense_recipe");

	public static final RegistryObject<RecipeSerializer<CurseRecipe>> CURSE_RECIPE_SERIALIZER = create("curse_recipe", () -> new CurseRecipe.Serializer());
	public static final RegistryObject<RecipeType<CurseRecipe>> CURSE_RECIPE_TYPE = create("curse_recipe");

	public static final RegistryObject<RecipeSerializer<TaglockCraftingRecipe>> TAGLOCK_CRAFTING_SERIALIZER = create("taglock_crafting", () -> new SimpleCraftingRecipeSerializer<>((id, category) -> new TaglockCraftingRecipe(id)));
	public static final RegistryObject<RecipeSerializer<ScepterCraftingRecipe>> SCEPTER_CRAFTING_SERIALIZER = create("scepter_crafting", () -> new SimpleCraftingRecipeSerializer<>((id, category) -> new ScepterCraftingRecipe(id)));
	public static final RegistryObject<RecipeSerializer<PricklyBeltCraftingRecipe>> PRICKLY_BELT_CRAFTING_SERIALIZER = create("prickly_belt_crafting", () -> new SimpleCraftingRecipeSerializer<>((id, category) -> new PricklyBeltCraftingRecipe(id)));

	private static <T extends Recipe<?>> RegistryObject<RecipeSerializer<T>> create(String name, Supplier<RecipeSerializer<T>> serializer) {
		return RECIPE_SERIALIZERS.register(name, serializer);
	}

	private static <T extends Recipe<?>> RegistryObject<RecipeType<T>> create(String name) {
		return RECIPE_TYPES.register(name, () -> new RecipeType<>() {
			@Override
			public String toString() {
				return name;
			}
		});
	}
}
