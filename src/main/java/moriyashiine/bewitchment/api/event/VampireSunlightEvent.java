/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * Fired on {@code MinecraftForge.EVENT_BUS} when a vampire player is about to burn in sunlight.
 * Cancel to stop the burning. (Fabric API: {@code AllowVampireBurn}.)
 */
@Cancelable
public class VampireSunlightEvent extends PlayerEvent {
	public VampireSunlightEvent(Player player) {
		super(player);
	}
}
