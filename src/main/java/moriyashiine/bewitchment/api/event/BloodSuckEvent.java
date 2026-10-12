/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

/** Fired on {@code MinecraftForge.EVENT_BUS} when a vampire player drinks from an entity. (Fabric API: {@code BloodSuckEvents}.) */
public abstract class BloodSuckEvent extends PlayerEvent {
	private final LivingEntity target;

	protected BloodSuckEvent(Player player, LivingEntity target) {
		super(player);
		this.target = target;
	}

	public LivingEntity getTarget() {
		return target;
	}

	/** Before drinking: listeners may raise (never lower) how much blood the player receives. */
	public static class Amount extends BloodSuckEvent {
		private int bloodToGive;

		public Amount(Player player, LivingEntity target, int bloodToGive) {
			super(player, target);
			this.bloodToGive = bloodToGive;
		}

		public int getBloodToGive() {
			return bloodToGive;
		}

		/** Keeps the larger of the current and requested amount, as the Fabric event did. */
		public void setBloodToGive(int bloodToGive) {
			this.bloodToGive = Math.max(this.bloodToGive, bloodToGive);
		}
	}

	/** After drinking. */
	public static class Post extends BloodSuckEvent {
		private final int bloodGiven;

		public Post(Player player, LivingEntity target, int bloodGiven) {
			super(player, target);
			this.bloodGiven = bloodGiven;
		}

		public int getBloodGiven() {
			return bloodGiven;
		}
	}
}
