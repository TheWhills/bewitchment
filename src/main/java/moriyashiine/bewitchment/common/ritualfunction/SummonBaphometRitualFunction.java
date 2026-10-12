/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.entity.living.BaphometEntity;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import java.util.function.Predicate;

public class SummonBaphometRitualFunction extends RitualFunction {
	public SummonBaphometRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public void start(ServerLevel world, BlockPos glyphPos, BlockPos effectivePos, Container inventory, boolean catFamiliar) {
		BaphometEntity entity = BWEntityTypes.BAPHOMET.create(world);
		if (entity != null) {
			entity.finalizeSpawn(world, world.getCurrentDifficultyAt(glyphPos), MobSpawnType.EVENT, null, null);
			entity.absMoveTo(effectivePos.getX() + 0.5, effectivePos.getY(), effectivePos.getZ() + 0.5, world.random.nextFloat() * 360, 0);
			world.addFreshEntity(entity);
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
