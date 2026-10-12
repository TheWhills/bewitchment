/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.ritualfunction.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;

public class BWRitualFunctions {
	public static final DeferredRegister<RitualFunction> RITUAL_FUNCTIONS = DeferredRegister.create(BWRegistries.RITUAL_FUNCTION_KEY, Bewitchment.MOD_ID);

	public static final RegistryObject<RitualFunction> TURN_TO_DAY = create("turn_to_day", () -> new TurnToDayRitualFunction(ParticleTypes.FLAME, null));
	public static final RegistryObject<RitualFunction> CLEANSE = create("cleanse", () -> new CleanseRitualFunction(ParticleTypes.ITEM_SNOWBALL, null));
	public static final RegistryObject<RitualFunction> PREVENT_DAMAGE = create("prevent_damage", () -> new RitualFunction(ParticleTypes.CRIT, null));
	public static final RegistryObject<RitualFunction> TURN_TO_NIGHT = create("turn_to_night", () -> new TurnToNightRitualFunction(ParticleTypes.ENCHANTED_HIT, null));
	public static final RegistryObject<RitualFunction> DESTROY_LIGHTS = create("destroy_lights", () -> new DestroyLightsRitualFunction(ParticleTypes.SMOKE, null));
	public static final RegistryObject<RitualFunction> CLEAR_ENCHANTMENTS = create("clear_enchantments", () -> new ClearEnchantmentsRitualFunction(ParticleTypes.ENCHANTED_HIT, null));
	public static final RegistryObject<RitualFunction> START_RAIN = create("start_rain", () -> new StartRainRitualFunction(ParticleTypes.DRIPPING_WATER, null));
	public static final RegistryObject<RitualFunction> MAKE_ENTITIES_WET = create("make_entities_wet", () -> new MakeEntitiesWetRitualFunction(ParticleTypes.SPLASH, null));
	public static final RegistryObject<RitualFunction> ENCHANT = create("enchant", () -> new EnchantRitualFunction(ParticleTypes.ENCHANT, null));
	public static final RegistryObject<RitualFunction> STOP_RAIN = create("stop_rain", () -> new StopRainRitualFunction(ParticleTypes.FLAME, null));
	public static final RegistryObject<RitualFunction> SMELT_ITEMS = create("smelt_items", () -> new SmeltItemsRitualFunction(ParticleTypes.FLAME, null));
	public static final RegistryObject<RitualFunction> DRAIN_WATER = create("drain_water", () -> new DrainWaterRitualFunction(ParticleTypes.SMOKE, null));
	public static final RegistryObject<RitualFunction> TELEPORT_ENTITIES = create("teleport_entities", () -> new TeleportEntitiesRitualFunction(ParticleTypes.PORTAL, null));
	public static final RegistryObject<RitualFunction> GROW = create("grow", () -> new GrowRitualFunction(ParticleTypes.HAPPY_VILLAGER, null));
	public static final RegistryObject<RitualFunction> SEARCH_BLOCKS = create("search_blocks", () -> new SearchBlocksRitualFunction(ParticleTypes.MYCELIUM, null));
	public static final RegistryObject<RitualFunction> SPAWN_LIGHTNING = create("spawn_lightning", () -> new SpawnLightningRitualFunction(ParticleTypes.CLOUD, null));
	public static final RegistryObject<RitualFunction> PUSH_MOBS = create("push_mobs", () -> new PushMobsRitualFunction(ParticleTypes.CLOUD, null));
	public static final RegistryObject<RitualFunction> HURRICANE = create("hurricane", () -> new HurricaneRitualFunction(ParticleTypes.CLOUD, null));

	public static final RegistryObject<RitualFunction> BIND_FAMILIAR = create("bind_familiar", () -> new BindFamiliarRitualFunction(ParticleTypes.PORTAL, null));
	public static final RegistryObject<RitualFunction> UNBIND_FAMILIAR = create("unbind_familiar", () -> new UnbindFamiliarRitualFunction(ParticleTypes.PORTAL, null));
	public static final RegistryObject<RitualFunction> SUMMON_WITHER = create("summon_wither", () -> new SummonWitherRitualFunction(ParticleTypes.FLAME, null));
	public static final RegistryObject<RitualFunction> SUMMON_DEMON = create("summon_demon", () -> new SummonDemonRitualFunction(ParticleTypes.FLAME, livingEntity -> livingEntity.getType().is(BWTags.HAS_BLOOD)));
	public static final RegistryObject<RitualFunction> SUMMON_LEONARD = create("summon_leonard", () -> new SummonLeonardRitualFunction(ParticleTypes.FLAME, livingEntity -> livingEntity instanceof Sheep));
	public static final RegistryObject<RitualFunction> SUMMON_BAPHOMET = create("summon_baphomet", () -> new SummonBaphometRitualFunction(ParticleTypes.FLAME, livingEntity -> livingEntity instanceof Sheep));

	public static final RegistryObject<RitualFunction> WEDNESDAY = create("wednesday", () -> new WednesdayRitualFunction(ParticleTypes.ITEM_SLIME, null));

	private static <T extends RitualFunction> RegistryObject<T> create(String name, Supplier<T> value) {
		return RITUAL_FUNCTIONS.register(name, value);
	}
}
