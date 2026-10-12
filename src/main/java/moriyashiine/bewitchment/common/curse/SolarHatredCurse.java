/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.curse;

import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class SolarHatredCurse extends Curse {
	public SolarHatredCurse(Type type) {
		super(type);
	}

	@Override
	public void tick(LivingEntity target) {
		if (target instanceof Player player && BWComponents.RESPAWN_TIMER_COMPONENT.get(player).getRespawnTimer() > 0) {
			return;
		}
		if (target.tickCount % 400 == 0 && target.level().isDay() && target.level().canSeeSky(target.blockPosition())) {
			target.setSecondsOnFire(8);
		}
	}
}
