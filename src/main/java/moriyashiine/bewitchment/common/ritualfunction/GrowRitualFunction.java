/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.misc.BWUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import java.util.function.Predicate;

public class GrowRitualFunction extends RitualFunction {
	public GrowRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public void tick(Level world, BlockPos glyphPos, BlockPos effectivePos, boolean catFamiliar) {
		int radius = catFamiliar ? 9 : 3;
		if (world instanceof ServerLevel serverWorld) {
			if (world.getGameTime() % 20 == 0) {
				for (AgeableMob passiveEntity : world.getEntitiesOfClass(AgeableMob.class, new AABB(effectivePos).inflate(radius, 0, radius), AgeableMob::isBaby)) {
					if (world.random.nextFloat() < 1 / 4f) {
						passiveEntity.ageUp(world.random.nextInt(), true);
					}
				}
				for (BlockPos foundPos : BWUtil.getBlockPoses(effectivePos, radius)) {
					if (world.getWorldBorder().isWithinBounds(foundPos)) {
						BlockState state = world.getBlockState(foundPos);
						if (!(state.getBlock() instanceof GrassBlock) && state.getBlock() instanceof BonemealableBlock fertilizable && fertilizable.isValidBonemealTarget(world, foundPos, state, false) && fertilizable.isBonemealSuccess(world, world.random, foundPos, state)) {
							fertilizable.performBonemeal(serverWorld, world.random, foundPos, state);
						}
					}
				}
			}
		} else {
			world.addParticle(ParticleTypes.HAPPY_VILLAGER, effectivePos.getX() + Mth.nextDouble(world.random, -radius, radius), effectivePos.getY() + 0.5, effectivePos.getZ() + Mth.nextDouble(world.random, -radius, radius), 0, 0, 0);
		}
	}
}
