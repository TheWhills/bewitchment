/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.curse;

import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class SusceptibilityCurse extends Curse {
	public SusceptibilityCurse(Type type) {
		super(type);
	}

	@Override
	public void tick(LivingEntity target) {
		target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 19, 1, true, false));
		target.addEffect(new MobEffectInstance(BWStatusEffects.SINKING.get(), 19, 0, true, false));
	}
}
