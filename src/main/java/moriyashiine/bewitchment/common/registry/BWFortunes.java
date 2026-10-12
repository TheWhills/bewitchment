/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.Fortune;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.fortune.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;

public class BWFortunes {
	public static final DeferredRegister<Fortune> FORTUNES = DeferredRegister.create(BWRegistries.FORTUNE_KEY, Bewitchment.MOD_ID);

	public static final RegistryObject<Fortune> POWER = create("power", () -> new PowerFortune(true));
	public static final RegistryObject<Fortune> WISDOM = create("wisdom", () -> new WisdomFortune(true));
	public static final RegistryObject<Fortune> COURAGE = create("courage", () -> new CourageFortune(true));
	public static final RegistryObject<Fortune> TREASURE = create("treasure", () -> new Fortune(true));
	public static final RegistryObject<Fortune> HAWKEYE = create("hawkeye", () -> new Fortune(true));
	public static final RegistryObject<Fortune> MERCHANT = create("merchant", () -> new MerchantFortune(true));

	public static final RegistryObject<Fortune> ILLNESS = create("illness", () -> new IllnessFortune(false));
	public static final RegistryObject<Fortune> INFESTED = create("infested", () -> new Fortune(false));
	public static final RegistryObject<Fortune> THUNDERBOLT = create("thunderbolt", () -> new ThunderboltFortune(false));
	public static final RegistryObject<Fortune> CLUMSINESS = create("clumsiness", () -> new ClumsinessFortune(false));
	public static final RegistryObject<Fortune> ENCOUNTER = create("encounter", () -> new EncounterFortune(false));
	public static final RegistryObject<Fortune> EXHAUSTED = create("exhausted", () -> new ExhaustedFortune(false));

	private static <T extends Fortune> RegistryObject<T> create(String name, Supplier<T> value) {
		return FORTUNES.register(name, value);
	}
}
