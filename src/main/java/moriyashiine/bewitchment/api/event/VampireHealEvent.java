/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * Fired on {@code MinecraftForge.EVENT_BUS} before a vampire player regenerates from blood.
 * Cancel to stop the heal. (Fabric API: {@code AllowVampireHeal}.)
 */
@Cancelable
public class VampireHealEvent extends PlayerEvent {
	private final boolean pledgedToLilith;

	public VampireHealEvent(Player player, boolean pledgedToLilith) {
		super(player);
		this.pledgedToLilith = pledgedToLilith;
	}

	public boolean isPledgedToLilith() {
		return pledgedToLilith;
	}
}
