/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.misc.BWUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import java.util.function.Predicate;

public class SearchBlocksRitualFunction extends RitualFunction {
	public SearchBlocksRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public void start(ServerLevel world, BlockPos glyphPos, BlockPos effectivePos, Container inventory, boolean catFamiliar) {
		Player closestPlayer = world.getNearestPlayer(glyphPos.getX() + 0.5, glyphPos.getY() + 0.5, glyphPos.getZ() + 0.5, 8, false);
		if (closestPlayer != null) {
			Block block = world.getBlockState(glyphPos.below(2)).getBlock();
			int blocks = 0;
			for (BlockPos foundPos : BWUtil.getBlockPoses(effectivePos, catFamiliar ? 48 : 16)) {
				if (world.getWorldBorder().isWithinBounds(foundPos) && world.getBlockState(foundPos).getBlock() == block) {
					blocks++;
				}
			}
			closestPlayer.displayClientMessage(Component.translatable(Bewitchment.MOD_ID + ".message.found_block" + (blocks == 1 ? "" : "s"), blocks, block.getName()), true);
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
