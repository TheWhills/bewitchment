/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;
import net.minecraftforge.registries.ForgeRegistries;

public class BWParticleTypes {
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Bewitchment.MOD_ID);

	public static final RegistryObject<ParticleType<SimpleParticleType>> CAULDRON_BUBBLE = create("cauldron_bubble", () -> new SimpleParticleType(false));
	public static final RegistryObject<ParticleType<SimpleParticleType>> INCENSE_SMOKE = create("incense_smoke", () -> new SimpleParticleType(false));

	private static <T extends ParticleOptions> RegistryObject<ParticleType<T>> create(String name, Supplier<ParticleType<T>> type) {
		return PARTICLE_TYPES.register(name, type);
	}
}
