/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static net.fabricmc.fabric.api.event.EventFactory.createArrayBacked;

public final class ReviveEvents {
	public static final Event<OnRevive> ON_REVIVE = createArrayBacked(OnRevive.class, listeners -> (player, source, poppet) -> {
		for (OnRevive listener : listeners) {
			listener.onRevive(player, source, poppet);
		}
	});

	public static final Event<CancelRevive> CANCEL_REVIVE = createArrayBacked(CancelRevive.class, listeners -> (player, source, poppet) -> {
		for (CancelRevive listener : listeners) {
			if (listener.shouldCancel(player, source, poppet)) {
				return true;
			}
		}
		return false;
	});

	public interface OnRevive {
		void onRevive(Player player, DamageSource source, ItemStack poppet);
	}

	public interface CancelRevive {
		boolean shouldCancel(Player player, DamageSource source, ItemStack poppet);
	}
}