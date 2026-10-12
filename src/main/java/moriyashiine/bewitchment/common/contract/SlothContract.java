/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.contract;

import moriyashiine.bewitchment.api.registry.Contract;
import net.minecraft.world.entity.player.Player;

public class SlothContract extends Contract {
	@Override
	public void tick(Player target) {
		if (target.tickCount % 10 == 0 && target.onGround() && !target.isSprinting()) {
			target.heal(1);
		}
	}
}
