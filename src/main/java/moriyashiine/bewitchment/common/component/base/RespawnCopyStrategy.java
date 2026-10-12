/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.base;

/** What happens to a player's component when the player entity is recreated (death or leaving the End). */
public enum RespawnCopyStrategy {
	/** Always carried over. */
	ALWAYS_COPY,
	/** Carried over when nothing would be lost anyway: returning from the End, or keepInventory is on. */
	LOSSLESS_ONLY,
	/** Never carried over; the new player starts fresh. */
	NEVER_COPY;

	public boolean shouldCopy(boolean wasDeath, boolean keepInventory) {
		return switch (this) {
			case ALWAYS_COPY -> true;
			case LOSSLESS_ONLY -> !wasDeath || keepInventory;
			case NEVER_COPY -> false;
		};
	}
}
