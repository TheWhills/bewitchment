/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.registry;

import net.minecraft.world.entity.player.Player;

public class Contract {
	public void tick(Player player) {
	}

	public static class Instance {
		public final Contract contract;
		public int duration, cost;

		public Instance(Contract contract, int duration, int cost) {
			this.contract = contract;
			this.duration = duration;
			this.cost = cost;
		}
	}
}
