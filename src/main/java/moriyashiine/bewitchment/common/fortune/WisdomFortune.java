/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.fortune;

import moriyashiine.bewitchment.api.registry.Fortune;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class WisdomFortune extends Fortune {
	public WisdomFortune(boolean positive) {
		super(positive);
	}

	@Override
	public boolean finish(ServerLevel world, Player target) {
		target.giveExperiencePoints(551);
		return super.finish(world, target);
	}
}
