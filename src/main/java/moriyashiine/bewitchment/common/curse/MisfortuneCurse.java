/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.curse;

import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class MisfortuneCurse extends Curse {
	public MisfortuneCurse(Type type) {
		super(type);
	}

	@Override
	public void tick(LivingEntity target) {
		if (target.tickCount % 20 == 0 && target.getRandom().nextFloat() < 1 / 100f) {
			target.addEffect(getEffect(target.getRandom().nextInt(8)));
		}
	}

	private static MobEffectInstance getEffect(int value) {
		return new MobEffectInstance(value == 1 ? MobEffects.POISON : value == 2 ? MobEffects.WEAKNESS : value == 3 ? MobEffects.MOVEMENT_SLOWDOWN : value == 4 ? MobEffects.BLINDNESS : value == 5 ? MobEffects.CONFUSION : value == 6 ? MobEffects.DIG_SLOWDOWN : value == 7 ? BWStatusEffects.CORROSION.get() : BWStatusEffects.SINKING.get(), 400);
	}
}
