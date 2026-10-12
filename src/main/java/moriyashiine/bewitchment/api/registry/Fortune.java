/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.registry;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class Fortune {
	public final boolean positive;

	public Fortune(boolean positive) {
		this.positive = positive;
	}

	public boolean tick(ServerLevel world, Player target) {
		return false;
	}

	public boolean finish(ServerLevel world, Player target) {
		return true;
	}

	public static class Instance {
		public final Fortune fortune;
		public int duration;

		public Instance(Fortune fortune, int duration) {

			this.fortune = fortune;
			this.duration = duration;
		}
	}
}
