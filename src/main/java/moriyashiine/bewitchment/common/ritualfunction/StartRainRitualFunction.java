/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;

public class StartRainRitualFunction extends RitualFunction {
	public StartRainRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public String getInvalidMessage() {
		return "ritual.precondition.no_rain";
	}

	@Override
	public boolean isValid(ServerLevel world, BlockPos pos, Container inventory) {
		return !world.isRaining();
	}

	@Override
	public void start(ServerLevel world, BlockPos glyphPos, BlockPos effectivePos, Container inventory, boolean catFamiliar) {
		world.setWeatherParameters(0, world.random.nextInt(6000) + (catFamiliar ? 18000 : 6000), true, catFamiliar);
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
