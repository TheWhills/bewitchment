/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.statuseffect;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

@SuppressWarnings("ConstantConditions")
public class PurityStatusEffect extends MobEffect {
	public PurityStatusEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		if (!entity.level().isClientSide && entity.tickCount % 20 == 0) {
			BuiltInRegistries.MOB_EFFECT.stream().forEach(effect -> {
				if (effect.getCategory() == MobEffectCategory.HARMFUL && entity.hasEffect(effect)) {
					MobEffectInstance currentPurity = entity.getEffect(this);
					if (currentPurity != null) {
						entity.removeEffect(this);
						if (currentPurity.getAmplifier() > 0) {
							entity.addEffect(new MobEffectInstance(this, currentPurity.getDuration(), currentPurity.getAmplifier() - 1, currentPurity.isAmbient(), currentPurity.isVisible(), currentPurity.showIcon()));
						}
						MobEffectInstance currentNegative = entity.getEffect(effect);
						entity.removeEffect(effect);
						if (currentNegative.getAmplifier() > 0) {
							entity.addEffect(new MobEffectInstance(effect, currentNegative.getDuration(), currentNegative.getAmplifier() - 1, currentNegative.isAmbient(), currentNegative.isVisible(), currentNegative.showIcon()));
						}
					}
				}
			});
		}
	}
}
