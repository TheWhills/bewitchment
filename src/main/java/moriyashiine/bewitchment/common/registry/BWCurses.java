/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.curse.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;

public class BWCurses {
	public static final DeferredRegister<Curse> CURSES = DeferredRegister.create(BWRegistries.CURSE_KEY, Bewitchment.MOD_ID);

	public static final RegistryObject<Curse> SOLAR_HATRED = create("solar_hatred", () -> new SolarHatredCurse(Curse.Type.LESSER));
	public static final RegistryObject<Curse> MISFORTUNE = create("misfortune", () -> new MisfortuneCurse(Curse.Type.LESSER));
	public static final RegistryObject<Curse> WEAK_LUNGS = create("weak_lungs", () -> new WeakLungsCurse(Curse.Type.LESSER));
	public static final RegistryObject<Curse> OUTRAGE = create("outrage", () -> new Curse(Curse.Type.LESSER));
	public static final RegistryObject<Curse> FORESTS_WRATH = create("forests_wrath", () -> new Curse(Curse.Type.LESSER));
	public static final RegistryObject<Curse> LIGHTNING_ROD = create("lightning_rod", () -> new Curse(Curse.Type.LESSER));
	public static final RegistryObject<Curse> UNLUCKY = create("unlucky", () -> new Curse(Curse.Type.LESSER));
	public static final RegistryObject<Curse> COMPROMISED = create("compromised", () -> new Curse(Curse.Type.LESSER));
	public static final RegistryObject<Curse> ARMY_OF_WORMS = create("army_of_worms", () -> new Curse(Curse.Type.LESSER));
	public static final RegistryObject<Curse> ARACHNOPHOBIA = create("arachnophobia", () -> new Curse(Curse.Type.LESSER));

	public static final RegistryObject<Curse> INSANITY = create("insanity", () -> new InsanityCurse(Curse.Type.GREATER));
	public static final RegistryObject<Curse> SUSCEPTIBILITY = create("susceptibility", () -> new SusceptibilityCurse(Curse.Type.GREATER));
	public static final RegistryObject<Curse> APATHY = create("apathy", () -> new ApathyCurse(Curse.Type.GREATER));

	private static <T extends Curse> RegistryObject<T> create(String name, Supplier<T> value) {
		return CURSES.register(name, value);
	}
}
