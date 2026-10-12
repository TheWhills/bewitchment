/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.living.GhostEntity;
import moriyashiine.bewitchment.common.entity.living.HellhoundEntity;
import moriyashiine.bewitchment.common.entity.living.VampireEntity;
import moriyashiine.bewitchment.common.entity.living.WerewolfEntity;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;

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

	/**
	 * Where Bewitchment mobs may spawn. Biome placement itself (trees, ores, spawns) is data-driven:
	 * see data/bewitchment/forge/biome_modifier and BWBiomeModifiers; loot injection is in
	 * data/bewitchment/loot_modifiers.
	 */
	static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
		event.register(BWEntityTypes.OWL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
		event.register(BWEntityTypes.RAVEN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
		event.register(BWEntityTypes.SNAKE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
		event.register(BWEntityTypes.TOAD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
		event.register(BWEntityTypes.GHOST.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GhostEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
		event.register(BWEntityTypes.VAMPIRE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, VampireEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
		event.register(BWEntityTypes.WEREWOLF.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WerewolfEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
		event.register(BWEntityTypes.HELLHOUND.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HellhoundEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
	}
}
