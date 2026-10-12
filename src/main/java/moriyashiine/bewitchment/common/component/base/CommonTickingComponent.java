/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.base;

/** Ticked on both sides with the same {@link #tick()} method. */
public interface CommonTickingComponent extends ServerTickingComponent, ClientTickingComponent {
	void tick();

	@Override
	default void serverTick() {
		tick();
	}

	@Override
	default void clientTick() {
		tick();
	}
}
