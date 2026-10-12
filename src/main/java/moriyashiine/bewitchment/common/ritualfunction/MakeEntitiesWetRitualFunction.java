/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import java.util.function.Predicate;

public class MakeEntitiesWetRitualFunction extends RitualFunction {
	public MakeEntitiesWetRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public void start(ServerLevel world, BlockPos glyphPos, BlockPos effectivePos, Container inventory, boolean catFamiliar) {
		int radius = catFamiliar ? 9 : 3;
		world.getEntitiesOfClass(Entity.class, new AABB(effectivePos).inflate(radius), Entity::isAlive).forEach(entity -> BWComponents.ADDITIONAL_WATER_DATA_COMPONENT.get(entity).setWetTimer(6000 * (catFamiliar ? 3 : 1)));
		for (BlockPos foundPos : BWUtil.getBlockPoses(effectivePos, radius)) {
			if (world.getWorldBorder().isWithinBounds(foundPos) && world.getBlockState(foundPos).getBlock() instanceof BaseFireBlock) {
				world.setBlockAndUpdate(foundPos, Blocks.AIR.defaultBlockState());
			}
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
