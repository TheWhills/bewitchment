/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import java.util.function.Predicate;

public class PushMobsRitualFunction extends RitualFunction {
	public PushMobsRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public void tick(Level world, BlockPos glyphPos, BlockPos effectivePos, boolean catFamiliar) {
		if (!world.isClientSide) {
			int radius = catFamiliar ? 24 : 8;
			if (world.getGameTime() % 5 == 0) {
				for (Monster hostileEntity : world.getEntitiesOfClass(Monster.class, new AABB(effectivePos).inflate(radius, 0, radius), LivingEntity::isAlive)) {
					double distanceX = effectivePos.getX() - hostileEntity.getX();
					double distanceZ = effectivePos.getZ() - hostileEntity.getZ();
					double max = Mth.absMax(distanceX, distanceZ);
					if (max >= 0) {
						max = Mth.sqrt((float) max);
						distanceX /= max;
						distanceZ /= max;
						distanceX *= Math.min(1, 1 / max);
						distanceZ *= Math.min(1, 1 / max);
						distanceX /= 2;
						distanceZ /= 2;
						hostileEntity.push(-distanceX, 0, -distanceZ);
					}
				}
			}
		}
	}
}
