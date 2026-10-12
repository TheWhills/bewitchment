/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.misc;

import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;

/**
 * Bat-form flight for vampires (replaces PlayerAbilityLib's ability source on Fabric).
 * Never removes flight that creative or spectator mode grants, and re-allows flight every
 * tick while in bat form so a game-mode change cannot strand a flying vampire.
 */
public final class VampireFlight {
	private VampireFlight() {
	}

	public static void grant(Player player) {
		Abilities abilities = player.getAbilities();
		if (!abilities.mayfly || !abilities.flying) {
			abilities.mayfly = true;
			abilities.flying = true;
			player.onUpdateAbilities();
		}
	}

	public static void revoke(Player player) {
		if (player.isCreative() || player.isSpectator()) {
			return;
		}
		Abilities abilities = player.getAbilities();
		if (abilities.mayfly || abilities.flying) {
			abilities.mayfly = false;
			abilities.flying = false;
			player.onUpdateAbilities();
		}
	}

	/** Called every server tick while the player is in bat form. */
	public static void maintain(Player player) {
		Abilities abilities = player.getAbilities();
		if (!abilities.mayfly) {
			abilities.mayfly = true;
			player.onUpdateAbilities();
		}
	}
}
