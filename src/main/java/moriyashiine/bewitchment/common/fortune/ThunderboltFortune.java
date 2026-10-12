/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.fortune;

import moriyashiine.bewitchment.api.registry.Fortune;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;

public class ThunderboltFortune extends Fortune {
	public ThunderboltFortune(boolean positive) {
		super(positive);
	}

	@Override
	public boolean finish(ServerLevel world, Player target) {
		LightningBolt entity = EntityType.LIGHTNING_BOLT.create(world);
		if (entity != null) {
			entity.absMoveTo(target.getX(), target.getY(), target.getZ(), world.random.nextFloat() * 360, 0);
			world.addFreshEntity(entity);
		}
		return super.finish(world, target);
	}
}
