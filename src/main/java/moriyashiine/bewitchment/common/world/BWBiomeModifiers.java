/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.world;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.bewitchment.common.BWConfig;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

/**
 * Biome modifier types whose effect depends on Bewitchment's config, read when the modifiers
 * are applied (server start). The modifiers themselves are data files under
 * data/bewitchment/forge/biome_modifier. Replaces Fabric's BiomeModifications calls.
 */
public final class BWBiomeModifiers {
	private static final Logger LOGGER = LogUtils.getLogger();

	public static final DeferredRegister<Codec<? extends BiomeModifier>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Bewitchment.MOD_ID);

	public static final RegistryObject<Codec<AddFeatures>> ADD_FEATURES = SERIALIZERS.register("add_features", () -> RecordCodecBuilder.create(instance -> instance.group(
			Biome.LIST_CODEC.fieldOf("biomes").forGetter(AddFeatures::biomes),
			PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(AddFeatures::features),
			GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(AddFeatures::step),
			Codec.STRING.optionalFieldOf("enabled_by", "always").forGetter(AddFeatures::enabledBy)
	).apply(instance, AddFeatures::new)));

	public static final RegistryObject<Codec<AddSpawn>> ADD_SPAWN = SERIALIZERS.register("add_spawn", () -> RecordCodecBuilder.create(instance -> instance.group(
			Biome.LIST_CODEC.fieldOf("biomes").forGetter(AddSpawn::biomes),
			ForgeRegistries.ENTITY_TYPES.getCodec().fieldOf("entity").forGetter(AddSpawn::entity),
			Codec.STRING.fieldOf("config").forGetter(AddSpawn::config),
			Codec.BOOL.optionalFieldOf("only_where_category_spawns", false).forGetter(AddSpawn::onlyWhereCategorySpawns)
	).apply(instance, AddSpawn::new)));

	private BWBiomeModifiers() {
	}

	/** Adds placed features; {@code enabled_by} is "always", "salt" or "silver" (the ore toggles). */
	public record AddFeatures(HolderSet<Biome> biomes, HolderSet<PlacedFeature> features, GenerationStep.Decoration step, String enabledBy) implements BiomeModifier {
		@Override
		public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
			if (phase != Phase.ADD || !biomes.contains(biome) || !enabled()) {
				return;
			}
			features.forEach(feature -> builder.getGenerationSettings().addFeature(step, feature));
		}

		private boolean enabled() {
			return switch (enabledBy) {
				case "salt" -> BWConfig.generateSalt;
				case "silver" -> BWConfig.generateSilver;
				default -> true;
			};
		}

		@Override
		public Codec<? extends BiomeModifier> codec() {
			return ADD_FEATURES.get();
		}
	}

	/**
	 * Adds a spawn whose weight and group size come from the config entry named by {@code config}
	 * (owl, raven, snake, toad, ghost, vampire, werewolf, hellhound); weight 0 or min group 0 disables it.
	 * With {@code only_where_category_spawns}, only biomes that already spawn the mob's category qualify.
	 */
	public record AddSpawn(HolderSet<Biome> biomes, EntityType<?> entity, String config, boolean onlyWhereCategorySpawns) implements BiomeModifier {
		@Override
		public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
			if (phase != Phase.ADD || !biomes.contains(biome)) {
				return;
			}
			if (onlyWhereCategorySpawns && biome.value().getMobSettings().getMobs(entity.getCategory()).isEmpty()) {
				return;
			}
			int[] settings = settings();
			int weight = settings[0], min = settings[1], max = settings[2];
			if (weight < 0 || min < 0 || max < 0 || min > max) {
				LOGGER.error("Invalid Bewitchment spawn config for {}: weight {}, group {}-{}; spawn not added", config, weight, min, max);
				return;
			}
			if (weight == 0 || min == 0) {
				return;
			}
			builder.getMobSpawnSettings().addSpawn(entity.getCategory(), new MobSpawnSettings.SpawnerData(entity, weight, min, max));
		}

		private int[] settings() {
			return switch (config) {
				case "owl" -> new int[]{BWConfig.owlWeight, BWConfig.owlMinGroupCount, BWConfig.owlMaxGroupCount};
				case "raven" -> new int[]{BWConfig.ravenWeight, BWConfig.ravenMinGroupCount, BWConfig.ravenMaxGroupCount};
				case "snake" -> new int[]{BWConfig.snakeWeight, BWConfig.snakeMinGroupCount, BWConfig.snakeMaxGroupCount};
				case "toad" -> new int[]{BWConfig.toadWeight, BWConfig.toadMinGroupCount, BWConfig.toadMaxGroupCount};
				case "ghost" -> new int[]{BWConfig.ghostWeight, BWConfig.ghostMinGroupCount, BWConfig.ghostMaxGroupCount};
				case "vampire" -> new int[]{BWConfig.vampireWeight, BWConfig.vampireMinGroupCount, BWConfig.vampireMaxGroupCount};
				case "werewolf" -> new int[]{BWConfig.werewolfWeight, BWConfig.werewolfMinGroupCount, BWConfig.werewolfMaxGroupCount};
				case "hellhound" -> new int[]{BWConfig.hellhoundWeight, BWConfig.hellhoundMinGroupCount, BWConfig.hellhoundMaxGroupCount};
				default -> new int[]{-1, -1, -1};
			};
		}

		@Override
		public Codec<? extends BiomeModifier> codec() {
			return ADD_SPAWN.get();
		}
	}
}
