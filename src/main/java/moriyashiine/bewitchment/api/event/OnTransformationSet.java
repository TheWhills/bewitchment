/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.event;

import moriyashiine.bewitchment.api.registry.Transformation;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.world.entity.player.Player;

import static net.fabricmc.fabric.api.event.EventFactory.createArrayBacked;

public interface OnTransformationSet {
	Event<OnTransformationSet> EVENT = createArrayBacked(OnTransformationSet.class, listeners -> (player, transformation) -> {
		for (OnTransformationSet listener : listeners) {
			listener.onTransformationSet(player, transformation);
		}
	});

	void onTransformationSet(Player player, Transformation transformation);
}
