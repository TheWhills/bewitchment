/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.*;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class BWRegistries {
	public static final ResourceKey<Registry<RitualFunction>> RITUAL_FUNCTION_KEY = ResourceKey.createRegistryKey(Bewitchment.id("ritual_function"));
	public static final Registry<RitualFunction> RITUAL_FUNCTION = FabricRegistryBuilder.createSimple(RITUAL_FUNCTION_KEY).buildAndRegister();

	public static final ResourceKey<Registry<Sigil>> SIGIL_KEY = ResourceKey.createRegistryKey(Bewitchment.id("sigil"));
	public static final Registry<Sigil> SIGIL = FabricRegistryBuilder.createSimple(SIGIL_KEY).buildAndRegister();

	public static final ResourceKey<Registry<Fortune>> FORTUNE_KEY = ResourceKey.createRegistryKey(Bewitchment.id("fortune"));
	public static final Registry<Fortune> FORTUNE = FabricRegistryBuilder.createSimple(FORTUNE_KEY).buildAndRegister();

	public static final ResourceKey<Registry<Transformation>> TRANSFORMATION_KEY = ResourceKey.createRegistryKey(Bewitchment.id("transformation"));
	public static final Registry<Transformation> TRANSFORMATION = FabricRegistryBuilder.createSimple(TRANSFORMATION_KEY).buildAndRegister();

	public static final ResourceKey<Registry<Contract>> CONTRACT_KEY = ResourceKey.createRegistryKey(Bewitchment.id("contract"));
	public static final Registry<Contract> CONTRACT = FabricRegistryBuilder.createSimple(CONTRACT_KEY).buildAndRegister();

	public static final ResourceKey<Registry<Curse>> CURSE_KEY = ResourceKey.createRegistryKey(Bewitchment.id("curse"));
	public static final Registry<Curse> CURSE = FabricRegistryBuilder.createSimple(CURSE_KEY).buildAndRegister();
}
