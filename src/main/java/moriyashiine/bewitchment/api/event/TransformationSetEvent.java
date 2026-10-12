/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.event;

import moriyashiine.bewitchment.api.registry.Transformation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

/** Fired on {@code MinecraftForge.EVENT_BUS} after a player's transformation is set. (Fabric API: {@code OnTransformationSet}.) */
public class TransformationSetEvent extends PlayerEvent {
	private final Transformation transformation;

	public TransformationSetEvent(Player player, Transformation transformation) {
		super(player);
		this.transformation = transformation;
	}

	public Transformation getTransformation() {
		return transformation;
	}
}
