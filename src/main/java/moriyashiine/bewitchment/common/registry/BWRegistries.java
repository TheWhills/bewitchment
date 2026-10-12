/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.*;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Bewitchment's own registries (ritual functions, sigils, fortunes, transformations,
 * contracts, curses). Each is created as a Forge registry; the entries themselves are
 * registered by the matching BW* content class through its own DeferredRegister.
 */
public class BWRegistries {
	private static final List<DeferredRegister<?>> REGISTRY_MAKERS = new ArrayList<>();

	public static final ResourceKey<Registry<RitualFunction>> RITUAL_FUNCTION_KEY = ResourceKey.createRegistryKey(Bewitchment.id("ritual_function"));
	public static final BWRegistry<RitualFunction> RITUAL_FUNCTION = create(RITUAL_FUNCTION_KEY);

	public static final ResourceKey<Registry<Sigil>> SIGIL_KEY = ResourceKey.createRegistryKey(Bewitchment.id("sigil"));
	public static final BWRegistry<Sigil> SIGIL = create(SIGIL_KEY);

	public static final ResourceKey<Registry<Fortune>> FORTUNE_KEY = ResourceKey.createRegistryKey(Bewitchment.id("fortune"));
	public static final BWRegistry<Fortune> FORTUNE = create(FORTUNE_KEY);

	public static final ResourceKey<Registry<Transformation>> TRANSFORMATION_KEY = ResourceKey.createRegistryKey(Bewitchment.id("transformation"));
	public static final BWRegistry<Transformation> TRANSFORMATION = create(TRANSFORMATION_KEY);

	public static final ResourceKey<Registry<Contract>> CONTRACT_KEY = ResourceKey.createRegistryKey(Bewitchment.id("contract"));
	public static final BWRegistry<Contract> CONTRACT = create(CONTRACT_KEY);

	public static final ResourceKey<Registry<Curse>> CURSE_KEY = ResourceKey.createRegistryKey(Bewitchment.id("curse"));
	public static final BWRegistry<Curse> CURSE = create(CURSE_KEY);

	private static <T> BWRegistry<T> create(ResourceKey<Registry<T>> key) {
		DeferredRegister<T> maker = DeferredRegister.create(key, Bewitchment.MOD_ID);
		REGISTRY_MAKERS.add(maker);
		return new BWRegistry<>(key, maker.makeRegistry(RegistryBuilder::new));
	}

	static void register(IEventBus modBus) {
		REGISTRY_MAKERS.forEach(maker -> maker.register(modBus));
	}
}
