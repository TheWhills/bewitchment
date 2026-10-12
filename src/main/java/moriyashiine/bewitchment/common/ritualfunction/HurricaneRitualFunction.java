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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.phys.AABB;
import java.util.function.Predicate;

public class HurricaneRitualFunction extends RitualFunction {
	public HurricaneRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public void start(ServerLevel world, BlockPos glyphPos, BlockPos effectivePos, Container inventory, boolean catFamiliar) {
		for (BlockPos foundPos : BWUtil.getBlockPoses(effectivePos, catFamiliar ? 24 : 8)) {
			if (world.getWorldBorder().isWithinBounds(foundPos)) {
				Block block = world.getBlockState(foundPos).getBlock();
				if (block instanceof BushBlock || block instanceof LeavesBlock) {
					world.destroyBlock(foundPos, true);
				}
			}
		}
		for (Entity entity : world.getEntitiesOfClass(Entity.class, new AABB(effectivePos).inflate(catFamiliar ? 24 : 8), Entity::isAlive)) {
			entity.push(0, 4, 0);
			if (entity instanceof Player) {
				entity.hurtMarked = true;
			}
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
