/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class InvigoratingStatusEffect extends MobEffect {
	public InvigoratingStatusEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean isInstantenous() {
		return true;
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		if (entity instanceof Player player) {
			BewitchmentAPI.fillMagic(player, 20 * (amplifier + 1), false);
		}
	}
}
