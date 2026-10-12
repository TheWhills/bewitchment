/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.fortune;

import moriyashiine.bewitchment.api.registry.Fortune;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class ExhaustedFortune extends Fortune {
	public ExhaustedFortune(boolean positive) {
		super(positive);
	}

	@Override
	public boolean finish(ServerLevel world, Player target) {
		return BWComponents.MAGIC_COMPONENT.get(target).drainMagic(50, false);
	}
}
