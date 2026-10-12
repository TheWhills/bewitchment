/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class BWTags {
	public static final TagKey<Biome> SPAWNS_HELLHOUNDS = TagKey.create(Registries.BIOME, Bewitchment.id("spawns_hellhounds"));

	public static final TagKey<EntityType<?>> VULNERABLE_TO_SILVER = TagKey.create(Registries.ENTITY_TYPE, Bewitchment.id("vulnerable_to_silver"));
	public static final TagKey<EntityType<?>> IMMUNE_TO_SILVER = TagKey.create(Registries.ENTITY_TYPE, Bewitchment.id("immune_to_silver"));
	public static final TagKey<EntityType<?>> HAS_BLOOD = TagKey.create(Registries.ENTITY_TYPE, Bewitchment.id("has_blood"));
	public static final TagKey<EntityType<?>> TAGLOCK_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, Bewitchment.id("taglock_blacklist"));
	public static final TagKey<EntityType<?>> ENCOUNTER_FORTUNE = TagKey.create(Registries.ENTITY_TYPE, Bewitchment.id("encounter_fortune"));
	public static final TagKey<EntityType<?>> INSANITY_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, Bewitchment.id("insanity_blacklist"));

	public static final TagKey<Block> ORES = TagKey.create(Registries.BLOCK, new ResourceLocation("c", "ores"));
	public static final TagKey<Item> SKULLS = TagKey.create(Registries.ITEM, new ResourceLocation("c", "skulls"));
	public static final TagKey<Block> GIVES_ALTAR_POWER = TagKey.create(Registries.BLOCK, Bewitchment.id("gives_altar_power"));
	public static final TagKey<Block> HEATS_CAULDRON = TagKey.create(Registries.BLOCK, Bewitchment.id("heats_cauldron"));
	public static final TagKey<Block> NATURAL_TERRAIN = TagKey.create(Registries.BLOCK, Bewitchment.id("natural_terrain"));
	public static final TagKey<Block> UNDEAD_MASK = TagKey.create(Registries.BLOCK, Bewitchment.id("undead_mask"));

	public static final TagKey<Item> SILVER_INGOTS = TagKey.create(Registries.ITEM, new ResourceLocation("c", "silver_ingots"));
	public static final TagKey<Item> SILVER_NUGGETS = TagKey.create(Registries.ITEM, new ResourceLocation("c", "silver_nuggets"));
	public static final TagKey<Item> BARKS = TagKey.create(Registries.ITEM, Bewitchment.id("barks"));
	public static final TagKey<Item> WITCHBERRY_FOODS = TagKey.create(Registries.ITEM, Bewitchment.id("witchberry_foods"));

	public static final TagKey<Item> SWORDS = TagKey.create(Registries.ITEM, Bewitchment.id("swords"));
	public static final TagKey<Item> PENTACLES = TagKey.create(Registries.ITEM, Bewitchment.id("pentacles"));
	public static final TagKey<Item> WANDS = TagKey.create(Registries.ITEM, Bewitchment.id("wands"));

	public static final TagKey<Item> WEAK_SWORDS = TagKey.create(Registries.ITEM, Bewitchment.id("weak_swords"));
	public static final TagKey<Item> AVERAGE_SWORDS = TagKey.create(Registries.ITEM, Bewitchment.id("average_swords"));
	public static final TagKey<Item> STRONG_SWORDS = TagKey.create(Registries.ITEM, Bewitchment.id("strong_swords"));

	public static final TagKey<Item> WEAK_PENTACLES = TagKey.create(Registries.ITEM, Bewitchment.id("weak_pentacles"));
	public static final TagKey<Item> AVERAGE_PENTACLES = TagKey.create(Registries.ITEM, Bewitchment.id("average_pentacles"));
	public static final TagKey<Item> STRONG_PENTACLES = TagKey.create(Registries.ITEM, Bewitchment.id("strong_pentacles"));

	public static final TagKey<Item> WEAK_WANDS = TagKey.create(Registries.ITEM, Bewitchment.id("weak_wands"));
	public static final TagKey<Item> AVERAGE_WANDS = TagKey.create(Registries.ITEM, Bewitchment.id("average_wands"));
	public static final TagKey<Item> STRONG_WANDS = TagKey.create(Registries.ITEM, Bewitchment.id("strong_wands"));

	public static final TagKey<Item> SUN_GLASSES = TagKey.create(Registries.ITEM, Bewitchment.id("sun_glasses"));
}
