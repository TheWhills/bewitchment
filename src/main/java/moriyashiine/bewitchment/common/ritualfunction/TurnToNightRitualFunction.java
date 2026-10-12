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

public class TurnToNightRitualFunction extends RitualFunction {
	public TurnToNightRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public String getInvalidMessage() {
		return "ritual.precondition.day";
	}

	@Override
	public boolean isValid(ServerLevel world, BlockPos pos, Container inventory) {
		return world.isDay();
	}

	@Override
	public void start(ServerLevel world, BlockPos glyphPos, BlockPos effectivePos, Container inventory, boolean catFamiliar) {
		while (world.getDayTime() % 24000 < 13000) {
			world.setDayTime(world.getDayTime() + 1);
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
