/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.BWConfig;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.GhostEntity;
import moriyashiine.bewitchment.common.entity.living.HellhoundEntity;
import moriyashiine.bewitchment.common.entity.living.VampireEntity;
import moriyashiine.bewitchment.common.entity.living.WerewolfEntity;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import java.util.function.Predicate;

public class BWWorldGenerators {
	public static final ResourceKey<ConfiguredFeature<?, ?>> JUNIPER = FeatureUtils.createKey(Bewitchment.MOD_ID + ":juniper");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CYPRESS = FeatureUtils.createKey(Bewitchment.MOD_ID + ":cypress");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ELDER = FeatureUtils.createKey(Bewitchment.MOD_ID + ":elder");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DRAGONS_BLOOD = FeatureUtils.createKey(Bewitchment.MOD_ID + ":dragons_blood");

	public static final ResourceKey<PlacedFeature> TREE_JUNIPER = PlacementUtils.createKey(Bewitchment.MOD_ID + ":tree_juniper");
	public static final ResourceKey<PlacedFeature> TREE_CYPRESS = PlacementUtils.createKey(Bewitchment.MOD_ID + ":tree_cypress");
	public static final ResourceKey<PlacedFeature> TREE_ELDER = PlacementUtils.createKey(Bewitchment.MOD_ID + ":tree_elder");

	public static final ResourceKey<PlacedFeature> ORE_SALT_UPPER = PlacementUtils.createKey(Bewitchment.MOD_ID + ":ore_salt_lower");
	public static final ResourceKey<PlacedFeature> ORE_SALT_LOWER = PlacementUtils.createKey(Bewitchment.MOD_ID + ":ore_salt_upper");
	public static final ResourceKey<PlacedFeature> ORE_SILVER = PlacementUtils.createKey(Bewitchment.MOD_ID + ":ore_silver");
	public static final ResourceKey<PlacedFeature> ORE_SILVER_LOWER = PlacementUtils.createKey(Bewitchment.MOD_ID + ":ore_silver_lower");

	public static void init() {
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.SAVANNA), GenerationStep.Decoration.VEGETAL_DECORATION, TREE_JUNIPER);
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.TAIGA).or(BiomeSelectors.tag(ConventionalBiomeTags.SWAMP)), GenerationStep.Decoration.VEGETAL_DECORATION, TREE_CYPRESS);
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.FOREST), GenerationStep.Decoration.VEGETAL_DECORATION, TREE_ELDER);
		if (BWConfig.generateSalt) {
			BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, ORE_SALT_UPPER);
			BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, ORE_SALT_LOWER);
		}
		if (BWConfig.generateSilver) {
			BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, ORE_SILVER);
			BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, ORE_SILVER_LOWER);
		}
		if (registerEntitySpawn(BWEntityTypes.OWL, BiomeSelectors.foundInOverworld().and(BiomeSelectors.tag(ConventionalBiomeTags.TAIGA).or(BiomeSelectors.tag(ConventionalBiomeTags.FOREST))), BWConfig.owlWeight, BWConfig.owlMinGroupCount, BWConfig.owlMaxGroupCount)) {
			SpawnPlacements.register(BWEntityTypes.OWL, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		}
		if (registerEntitySpawn(BWEntityTypes.RAVEN, BiomeSelectors.foundInOverworld().and(BiomeSelectors.tag(ConventionalBiomeTags.PLAINS).or(BiomeSelectors.tag(ConventionalBiomeTags.FOREST))), BWConfig.ravenWeight, BWConfig.ravenMinGroupCount, BWConfig.ravenMaxGroupCount)) {
			SpawnPlacements.register(BWEntityTypes.RAVEN, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		}
		if (registerEntitySpawn(BWEntityTypes.SNAKE, BiomeSelectors.foundInOverworld().and(BiomeSelectors.tag(ConventionalBiomeTags.PLAINS).or(BiomeSelectors.tag(ConventionalBiomeTags.SAVANNA).or(BiomeSelectors.tag(ConventionalBiomeTags.DESERT)))), BWConfig.snakeWeight, BWConfig.snakeMinGroupCount, BWConfig.snakeMaxGroupCount)) {
			SpawnPlacements.register(BWEntityTypes.SNAKE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		}
		if (registerEntitySpawn(BWEntityTypes.TOAD, BiomeSelectors.foundInOverworld().and(BiomeSelectors.tag(ConventionalBiomeTags.JUNGLE).or(BiomeSelectors.tag(ConventionalBiomeTags.SWAMP))), BWConfig.toadWeight, BWConfig.toadMinGroupCount, BWConfig.toadMaxGroupCount)) {
			SpawnPlacements.register(BWEntityTypes.TOAD, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
		}
		if (registerEntitySpawn(BWEntityTypes.GHOST, BiomeSelectors.foundInOverworld().and(context -> !context.getBiome().getMobSettings().getMobs(BWEntityTypes.GHOST.getCategory()).isEmpty()), BWConfig.ghostWeight, BWConfig.ghostMinGroupCount, BWConfig.ghostMaxGroupCount)) {
			SpawnPlacements.register(BWEntityTypes.GHOST, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GhostEntity::canSpawn);
		}
		if (registerEntitySpawn(BWEntityTypes.VAMPIRE, BiomeSelectors.foundInOverworld().and(context -> !context.getBiome().getMobSettings().getMobs(BWEntityTypes.VAMPIRE.getCategory()).isEmpty()).and(BiomeSelectors.tag(ConventionalBiomeTags.TAIGA).or(BiomeSelectors.tag(ConventionalBiomeTags.PLAINS))), BWConfig.vampireWeight, BWConfig.vampireMinGroupCount, BWConfig.vampireMaxGroupCount)) {
			SpawnPlacements.register(BWEntityTypes.VAMPIRE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, VampireEntity::canSpawn);
		}
		if (registerEntitySpawn(BWEntityTypes.WEREWOLF, BiomeSelectors.foundInOverworld().and(context -> !context.getBiome().getMobSettings().getMobs(BWEntityTypes.WEREWOLF.getCategory()).isEmpty()).and(BiomeSelectors.tag(ConventionalBiomeTags.TAIGA).or(BiomeSelectors.tag(ConventionalBiomeTags.FOREST))), BWConfig.werewolfWeight, BWConfig.werewolfMinGroupCount, BWConfig.werewolfMaxGroupCount)) {
			SpawnPlacements.register(BWEntityTypes.WEREWOLF, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WerewolfEntity::canSpawn);
		}
		if (registerEntitySpawn(BWEntityTypes.HELLHOUND, BiomeSelectors.foundInTheNether(), BWConfig.hellhoundWeight, BWConfig.hellhoundMinGroupCount, BWConfig.hellhoundMaxGroupCount)) {
			SpawnPlacements.register(BWEntityTypes.HELLHOUND, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HellhoundEntity::canSpawn);
		}
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			ResourceLocation seeds = Bewitchment.id("inject/seeds");
			ResourceLocation nether_fortress = Bewitchment.id("inject/nether_fortress");
			if (Blocks.GRASS.getLootTable().equals(id) || Blocks.FERN.getLootTable().equals(id) || Blocks.TALL_GRASS.getLootTable().equals(id) || Blocks.LARGE_FERN.getLootTable().equals(id)) {
				tableBuilder.pool(LootPool.lootPool().add(LootTableReference.lootTableReference(seeds).setWeight(1)).build());
			}
			if (BuiltInLootTables.NETHER_BRIDGE.equals(id)) {
				tableBuilder.pool(LootPool.lootPool().add(LootTableReference.lootTableReference(nether_fortress).setWeight(1)).build());
			}
		});
	}

	private static boolean registerEntitySpawn(EntityType<?> type, Predicate<BiomeSelectionContext> predicate, int weight, int minGroupSize, int maxGroupSize) {
		if (weight < 0) {
			throw new UnsupportedOperationException("Could not register entity type " + type.getDescriptionId() + ": weight " + weight + " cannot be negative.");
		} else if (minGroupSize < 0) {
			throw new UnsupportedOperationException("Could not register entity type " + type.getDescriptionId() + ": minGroupSize " + minGroupSize + " cannot be negative.");
		} else if (maxGroupSize < 0) {
			throw new UnsupportedOperationException("Could not register entity type " + type.getDescriptionId() + ": maxGroupSize " + maxGroupSize + " cannot be negative.");
		} else if (minGroupSize > maxGroupSize) {
			throw new UnsupportedOperationException("Could not register entity type " + type.getDescriptionId() + ": minGroupSize " + minGroupSize + " cannot be greater than maxGroupSize " + maxGroupSize + ".");
		} else if (weight == 0 || minGroupSize == 0) {
			return false;
		}
		BiomeModifications.addSpawn(predicate, type.getCategory(), type, weight, minGroupSize, maxGroupSize);
		return true;
	}
}
