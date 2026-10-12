/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import java.util.function.Predicate;

public class SummonWitherRitualFunction extends RitualFunction {
	public SummonWitherRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public void start(ServerLevel world, BlockPos glyphPos, BlockPos effectivePos, Container inventory, boolean catFamiliar) {
		WitherBoss entity = EntityType.WITHER.create(world);
		if (entity != null) {
			entity.finalizeSpawn(world, world.getCurrentDifficultyAt(effectivePos), MobSpawnType.EVENT, null, null);
			entity.absMoveTo(effectivePos.getX() + 0.5, effectivePos.getY(), effectivePos.getZ() + 0.5, world.random.nextFloat() * 360, 0);
			world.addFreshEntity(entity);
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
