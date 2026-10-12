/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Fired on {@code MinecraftForge.EVENT_BUS} when an entity's blood changes. Informational;
 * not cancelable. (Fabric API: {@code BloodSetEvents}.)
 */
public abstract class BloodEvent extends LivingEvent {
	private final int amount;

	protected BloodEvent(LivingEntity entity, int amount) {
		super(entity);
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}

	/** Blood was added, or would be if {@link #isSimulated()}. */
	public static class Fill extends BloodEvent {
		private final boolean simulated;

		public Fill(LivingEntity entity, int amount, boolean simulated) {
			super(entity, amount);
			this.simulated = simulated;
		}

		public boolean isSimulated() {
			return simulated;
		}
	}

	/** Blood was removed, or would be if {@link #isSimulated()}. */
	public static class Drain extends BloodEvent {
		private final boolean simulated;

		public Drain(LivingEntity entity, int amount, boolean simulated) {
			super(entity, amount);
			this.simulated = simulated;
		}

		public boolean isSimulated() {
			return simulated;
		}
	}

	/** Blood was set directly to {@link #getAmount()}. */
	public static class Set extends BloodEvent {
		public Set(LivingEntity entity, int amount) {
			super(entity, amount);
		}
	}
}
