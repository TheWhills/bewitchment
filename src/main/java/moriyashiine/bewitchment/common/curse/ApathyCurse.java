/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.curse;

import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ApathyCurse extends Curse {
	public ApathyCurse(Type type) {
		super(type);
	}

	@Override
	public void tick(LivingEntity target) {
		if (target instanceof Player player) {
			BWComponents.MAGIC_COMPONENT.maybeGet(player).ifPresent(magicComponent -> {
				if (magicComponent.getMagic() > 0) {
					magicComponent.setMagic(0);
				}
			});
		}
	}
}
