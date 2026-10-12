/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.fortune;

import moriyashiine.bewitchment.api.registry.Fortune;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;

public class MerchantFortune extends Fortune {
	public MerchantFortune(boolean positive) {
		super(positive);
	}

	@Override
	public boolean finish(ServerLevel world, Player target) {
		WanderingTrader entity = EntityType.WANDERING_TRADER.create(world);
		if (entity != null) {
			for (int i = 0; i < 8; i++) {
				BlockPos pos = target.blockPosition().offset(Mth.nextInt(world.random, -3, 3), 0, Mth.nextInt(world.random, -3, 3));
				if (!world.getBlockState(pos).blocksMotion()) {
					entity.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.EVENT, null, null);
					entity.absMoveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, world.random.nextFloat() * 360, 0);
					world.addFreshEntity(entity);
					break;
				}
			}
		}
		return super.finish(world, target);
	}
}
