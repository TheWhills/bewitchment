/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.contract.SlothContract;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;

public class BWContracts {
	public static final DeferredRegister<Contract> CONTRACTS = DeferredRegister.create(BWRegistries.CONTRACT_KEY, Bewitchment.MOD_ID);

	public static final RegistryObject<Contract> LUST = create("lust", () -> new Contract());
	public static final RegistryObject<Contract> GLUTTONY = create("gluttony", () -> new Contract());
	public static final RegistryObject<Contract> GREED = create("greed", () -> new Contract());
	public static final RegistryObject<Contract> SLOTH = create("sloth", () -> new SlothContract());
	public static final RegistryObject<Contract> WRATH = create("wrath", () -> new Contract());
	public static final RegistryObject<Contract> ENVY = create("envy", () -> new Contract());
	public static final RegistryObject<Contract> PRIDE = create("pride", () -> new Contract());

	public static final RegistryObject<Contract> FAMINE = create("famine", () -> new Contract());
	public static final RegistryObject<Contract> PESTILENCE = create("pestilence", () -> new Contract());
	public static final RegistryObject<Contract> WAR = create("war", () -> new Contract());
	public static final RegistryObject<Contract> DEATH = create("death", () -> new Contract());

	private static <T extends Contract> RegistryObject<T> create(String name, Supplier<T> value) {
		return CONTRACTS.register(name, value);
	}
}
