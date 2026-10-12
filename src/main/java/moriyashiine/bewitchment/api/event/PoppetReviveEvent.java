/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/** Fired on {@code MinecraftForge.EVENT_BUS} when a death protection poppet saves a player. (Fabric API: {@code ReviveEvents}.) */
public abstract class PoppetReviveEvent extends PlayerEvent {
	private final DamageSource source;
	private final ItemStack poppet;

	protected PoppetReviveEvent(Player player, DamageSource source, ItemStack poppet) {
		super(player);
		this.source = source;
		this.poppet = poppet;
	}

	public DamageSource getSource() {
		return source;
	}

	public ItemStack getPoppet() {
		return poppet;
	}

	/** Before the revive. Cancel to let the player die. */
	@Cancelable
	public static class Pre extends PoppetReviveEvent {
		public Pre(Player player, DamageSource source, ItemStack poppet) {
			super(player, source, poppet);
		}
	}

	/** After the player was revived. */
	public static class Post extends PoppetReviveEvent {
		public Post(Player player, DamageSource source, ItemStack poppet) {
			super(player, source, poppet);
		}
	}
}
