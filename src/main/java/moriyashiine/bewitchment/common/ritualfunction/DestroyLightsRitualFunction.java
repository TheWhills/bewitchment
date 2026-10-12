/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.misc.BWUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;

public class DestroyLightsRitualFunction extends RitualFunction {
	public DestroyLightsRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public void start(ServerLevel world, BlockPos glyphPos, BlockPos effectivePos, Container inventory, boolean catFamiliar) {
		for (BlockPos foundPos : BWUtil.getBlockPoses(effectivePos, catFamiliar ? 24 : 8)) {
			if (world.getWorldBorder().isWithinBounds(foundPos) && world.getBlockState(foundPos).getLightEmission() > 0 && world.getBlockState(foundPos).getDestroySpeed(world, foundPos) == 0) {
				world.destroyBlock(foundPos, true);
			}
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
