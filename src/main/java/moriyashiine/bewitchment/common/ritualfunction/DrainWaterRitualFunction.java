/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.misc.BWUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Predicate;

public class DrainWaterRitualFunction extends RitualFunction {
	public DrainWaterRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public void start(ServerLevel world, BlockPos glyphPos, BlockPos effectivePos, Container inventory, boolean catFamiliar) {
		for (BlockPos foundPos : BWUtil.getBlockPoses(effectivePos, catFamiliar ? 24 : 8)) {
			if (world.getWorldBorder().isWithinBounds(foundPos) && world.getFluidState(foundPos).is(FluidTags.WATER)) {
				BlockState state = world.getBlockState(foundPos);
				if (state.getBlock() instanceof BucketPickup fluidDrainable) {
					fluidDrainable.pickupBlock(world, foundPos, state);
				}
			}
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
