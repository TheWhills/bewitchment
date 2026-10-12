/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.fortune;

import moriyashiine.bewitchment.api.registry.Fortune;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ClumsinessFortune extends Fortune {
	public ClumsinessFortune(boolean positive) {
		super(positive);
	}

	@Override
	public boolean finish(ServerLevel world, Player target) {
		boolean drop = target.drop(target.getMainHandItem(), false, true) != null;
		if (drop) {
			target.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
			return true;
		}
		return false;
	}
}
