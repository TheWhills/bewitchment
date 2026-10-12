/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.registry.Sigil;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.sigil.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;

public class BWSigils {
	public static final DeferredRegister<Sigil> SIGILS = DeferredRegister.create(BWRegistries.SIGIL_KEY, Bewitchment.MOD_ID);

	public static final RegistryObject<Sigil> MENDING = create("mending", () -> new MendingSigil(true, 32));
	public static final RegistryObject<Sigil> CLEANSING = create("cleansing", () -> new CleansingSigil(true, 32));
	public static final RegistryObject<Sigil> JUDGMENT = create("judgment", () -> new JudgmentSigil(true, 32));
	public static final RegistryObject<Sigil> DECAY = create("decay", () -> new DecaySigil(true, 32));
	public static final RegistryObject<Sigil> SHRIEKING = create("shrieking", () -> new ShriekingSigil(true, 32));
	public static final RegistryObject<Sigil> SENTINEL = create("sentinel", () -> new SentinelSigil(true, 32));

	public static final RegistryObject<Sigil> SLIPPERY = create("slippery", () -> new Sigil(false, 32));
	public static final RegistryObject<Sigil> SHADOWS = create("shadows", () -> new Sigil(false, 32));
	public static final RegistryObject<Sigil> EXTENDING = create("extending", () -> new Sigil(false, 32));
	public static final RegistryObject<Sigil> SMELLY = create("smelly", () -> new SmellySigil(false, 256));
	public static final RegistryObject<Sigil> RUIN = create("ruin", () -> new Sigil(false, 32));
	public static final RegistryObject<Sigil> HEAVY = create("heavy", () -> new Sigil(false, 32));

	private static <T extends Sigil> RegistryObject<T> create(String name, Supplier<T> value) {
		return SIGILS.register(name, value);
	}
}
