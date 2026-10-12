/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import java.util.function.Predicate;

public class SpawnLightningRitualFunction extends RitualFunction {
	public SpawnLightningRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public void start(ServerLevel world, BlockPos glyphPos, BlockPos effectivePos, Container inventory, boolean catFamiliar) {
		summonLightning(world, effectivePos);
		if (catFamiliar) {
			for (int i = 0; i < Mth.nextInt(world.random, 2, 4); i++) {
				summonLightning(world, effectivePos.offset(Mth.nextInt(world.random, -4, 4), 0, Mth.nextInt(world.random, -4, 4)));
			}
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}

	private void summonLightning(Level world, BlockPos pos) {
		LightningBolt entity = EntityType.LIGHTNING_BOLT.create(world);
		if (entity != null) {
			entity.absMoveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, world.random.nextFloat() * 360, 0);
			world.addFreshEntity(entity);
		}
	}
}
