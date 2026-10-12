/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Bewitchment's settings, stored in config/bewitchment-common.toml (Forge config; was MidnightLib,
 * whose Forge build crashes dedicated servers). The static fields keep their names and defaults so
 * the rest of the mod reads them unchanged; they are refreshed whenever Forge loads or reloads the file.
 */
public final class BWConfig {
	public static List<String> disabledPoppets = new ArrayList<>();
	public static boolean enableCurses = true;
	public static boolean enablePolymorph = true;
	public static int altarDistributionRadius = 24;
	public static boolean generateSalt = true;
	public static boolean generateSilver = true;
	public static int owlWeight = 10;
	public static int owlMinGroupCount = 1;
	public static int owlMaxGroupCount = 2;
	public static int ravenWeight = 10;
	public static int ravenMinGroupCount = 1;
	public static int ravenMaxGroupCount = 3;
	public static int snakeWeight = 6;
	public static int snakeMinGroupCount = 1;
	public static int snakeMaxGroupCount = 2;
	public static int toadWeight = 10;
	public static int toadMinGroupCount = 1;
	public static int toadMaxGroupCount = 3;
	public static int ghostWeight = 20;
	public static int ghostMinGroupCount = 1;
	public static int ghostMaxGroupCount = 1;
	public static int vampireWeight = 20;
	public static int vampireMinGroupCount = 1;
	public static int vampireMaxGroupCount = 1;
	public static int werewolfWeight = 20;
	public static int werewolfMinGroupCount = 1;
	public static int werewolfMaxGroupCount = 1;
	public static int hellhoundWeight = 6;
	public static int hellhoundMinGroupCount = 1;
	public static int hellhoundMaxGroupCount = 1;

	private static final ForgeConfigSpec.ConfigValue<List<? extends String>> V_disabledPoppets;
	private static final ForgeConfigSpec.BooleanValue V_enableCurses;
	private static final ForgeConfigSpec.BooleanValue V_enablePolymorph;
	private static final ForgeConfigSpec.IntValue V_altarDistributionRadius;
	private static final ForgeConfigSpec.BooleanValue V_generateSalt;
	private static final ForgeConfigSpec.BooleanValue V_generateSilver;
	private static final ForgeConfigSpec.IntValue V_owlWeight;
	private static final ForgeConfigSpec.IntValue V_owlMinGroupCount;
	private static final ForgeConfigSpec.IntValue V_owlMaxGroupCount;
	private static final ForgeConfigSpec.IntValue V_ravenWeight;
	private static final ForgeConfigSpec.IntValue V_ravenMinGroupCount;
	private static final ForgeConfigSpec.IntValue V_ravenMaxGroupCount;
	private static final ForgeConfigSpec.IntValue V_snakeWeight;
	private static final ForgeConfigSpec.IntValue V_snakeMinGroupCount;
	private static final ForgeConfigSpec.IntValue V_snakeMaxGroupCount;
	private static final ForgeConfigSpec.IntValue V_toadWeight;
	private static final ForgeConfigSpec.IntValue V_toadMinGroupCount;
	private static final ForgeConfigSpec.IntValue V_toadMaxGroupCount;
	private static final ForgeConfigSpec.IntValue V_ghostWeight;
	private static final ForgeConfigSpec.IntValue V_ghostMinGroupCount;
	private static final ForgeConfigSpec.IntValue V_ghostMaxGroupCount;
	private static final ForgeConfigSpec.IntValue V_vampireWeight;
	private static final ForgeConfigSpec.IntValue V_vampireMinGroupCount;
	private static final ForgeConfigSpec.IntValue V_vampireMaxGroupCount;
	private static final ForgeConfigSpec.IntValue V_werewolfWeight;
	private static final ForgeConfigSpec.IntValue V_werewolfMinGroupCount;
	private static final ForgeConfigSpec.IntValue V_werewolfMaxGroupCount;
	private static final ForgeConfigSpec.IntValue V_hellhoundWeight;
	private static final ForgeConfigSpec.IntValue V_hellhoundMinGroupCount;
	private static final ForgeConfigSpec.IntValue V_hellhoundMaxGroupCount;

	private static final ForgeConfigSpec SPEC;

	static {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.push("general");
		V_disabledPoppets = builder.comment("Poppet item ids (e.g. \"bewitchment:voodoo_poppet\") that cannot be used.").defineListAllowEmpty("disabled_poppets", List.of(), entry -> entry instanceof String);
		V_enableCurses = builder.comment("Whether curses can be applied.").define("enable_curses", true);
		V_enablePolymorph = builder.comment("Whether the polymorph effect can disguise players.").define("enable_polymorph", true);
		V_altarDistributionRadius = builder.comment("Radius in blocks around a witch altar that it powers and refills magic in.").defineInRange("altar_distribution_radius", 24, 1, 128);
		builder.pop();
		builder.push("worldgen");
		V_generateSalt = builder.comment("Generate salt ore. Applied when a world loads.").define("generate_salt", true);
		V_generateSilver = builder.comment("Generate silver ore. Applied when a world loads.").define("generate_silver", true);
		builder.pop();
		builder.comment("Natural spawning per mob: weight (0 disables) and group size. Applied when a world loads.").push("spawns");
		builder.push("owl");
		V_owlWeight = builder.defineInRange("weight", 10, 0, 1000);
		V_owlMinGroupCount = builder.defineInRange("min_group", 1, 0, 1000);
		V_owlMaxGroupCount = builder.defineInRange("max_group", 2, 0, 1000);
		builder.pop();
		builder.push("raven");
		V_ravenWeight = builder.defineInRange("weight", 10, 0, 1000);
		V_ravenMinGroupCount = builder.defineInRange("min_group", 1, 0, 1000);
		V_ravenMaxGroupCount = builder.defineInRange("max_group", 3, 0, 1000);
		builder.pop();
		builder.push("snake");
		V_snakeWeight = builder.defineInRange("weight", 6, 0, 1000);
		V_snakeMinGroupCount = builder.defineInRange("min_group", 1, 0, 1000);
		V_snakeMaxGroupCount = builder.defineInRange("max_group", 2, 0, 1000);
		builder.pop();
		builder.push("toad");
		V_toadWeight = builder.defineInRange("weight", 10, 0, 1000);
		V_toadMinGroupCount = builder.defineInRange("min_group", 1, 0, 1000);
		V_toadMaxGroupCount = builder.defineInRange("max_group", 3, 0, 1000);
		builder.pop();
		builder.push("ghost");
		V_ghostWeight = builder.defineInRange("weight", 20, 0, 1000);
		V_ghostMinGroupCount = builder.defineInRange("min_group", 1, 0, 1000);
		V_ghostMaxGroupCount = builder.defineInRange("max_group", 1, 0, 1000);
		builder.pop();
		builder.push("vampire");
		V_vampireWeight = builder.defineInRange("weight", 20, 0, 1000);
		V_vampireMinGroupCount = builder.defineInRange("min_group", 1, 0, 1000);
		V_vampireMaxGroupCount = builder.defineInRange("max_group", 1, 0, 1000);
		builder.pop();
		builder.push("werewolf");
		V_werewolfWeight = builder.defineInRange("weight", 20, 0, 1000);
		V_werewolfMinGroupCount = builder.defineInRange("min_group", 1, 0, 1000);
		V_werewolfMaxGroupCount = builder.defineInRange("max_group", 1, 0, 1000);
		builder.pop();
		builder.push("hellhound");
		V_hellhoundWeight = builder.defineInRange("weight", 6, 0, 1000);
		V_hellhoundMinGroupCount = builder.defineInRange("min_group", 1, 0, 1000);
		V_hellhoundMaxGroupCount = builder.defineInRange("max_group", 1, 0, 1000);
		builder.pop();
		builder.pop();
		SPEC = builder.build();
	}

	private BWConfig() {
	}

	public static void register(IEventBus modBus) {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC);
		modBus.addListener((ModConfigEvent.Loading event) -> load(event.getConfig()));
		modBus.addListener((ModConfigEvent.Reloading event) -> load(event.getConfig()));
	}

	private static void load(ModConfig config) {
		if (config.getSpec() != SPEC) {
			return;
		}
		disabledPoppets = new ArrayList<>(V_disabledPoppets.get());
		enableCurses = V_enableCurses.get();
		enablePolymorph = V_enablePolymorph.get();
		altarDistributionRadius = V_altarDistributionRadius.get();
		generateSalt = V_generateSalt.get();
		generateSilver = V_generateSilver.get();
		owlWeight = V_owlWeight.get();
		owlMinGroupCount = V_owlMinGroupCount.get();
		owlMaxGroupCount = V_owlMaxGroupCount.get();
		ravenWeight = V_ravenWeight.get();
		ravenMinGroupCount = V_ravenMinGroupCount.get();
		ravenMaxGroupCount = V_ravenMaxGroupCount.get();
		snakeWeight = V_snakeWeight.get();
		snakeMinGroupCount = V_snakeMinGroupCount.get();
		snakeMaxGroupCount = V_snakeMaxGroupCount.get();
		toadWeight = V_toadWeight.get();
		toadMinGroupCount = V_toadMinGroupCount.get();
		toadMaxGroupCount = V_toadMaxGroupCount.get();
		ghostWeight = V_ghostWeight.get();
		ghostMinGroupCount = V_ghostMinGroupCount.get();
		ghostMaxGroupCount = V_ghostMaxGroupCount.get();
		vampireWeight = V_vampireWeight.get();
		vampireMinGroupCount = V_vampireMinGroupCount.get();
		vampireMaxGroupCount = V_vampireMaxGroupCount.get();
		werewolfWeight = V_werewolfWeight.get();
		werewolfMinGroupCount = V_werewolfMinGroupCount.get();
		werewolfMaxGroupCount = V_werewolfMaxGroupCount.get();
		hellhoundWeight = V_hellhoundWeight.get();
		hellhoundMinGroupCount = V_hellhoundMinGroupCount.get();
		hellhoundMaxGroupCount = V_hellhoundMaxGroupCount.get();
	}
}
