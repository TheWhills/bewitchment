/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import java.util.LinkedHashMap;
import java.util.Map;

public class BWParticleTypes {
	private static final Map<ParticleType<?>, ResourceLocation> PARTICLE_TYPES = new LinkedHashMap<>();

	public static final ParticleType<SimpleParticleType> CAULDRON_BUBBLE = create("cauldron_bubble", FabricParticleTypes.simple());
	public static final ParticleType<SimpleParticleType> INCENSE_SMOKE = create("incense_smoke", FabricParticleTypes.simple());

	private static <T extends ParticleOptions> ParticleType<T> create(String name, ParticleType<T> type) {
		PARTICLE_TYPES.put(type, Bewitchment.id(name));
		return type;
	}

	public static void init() {
		PARTICLE_TYPES.keySet().forEach(particleType -> Registry.register(BuiltInRegistries.PARTICLE_TYPE, PARTICLE_TYPES.get(particleType), particleType));
	}
}
