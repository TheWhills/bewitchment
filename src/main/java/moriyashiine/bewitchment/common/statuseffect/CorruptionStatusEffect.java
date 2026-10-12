/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ConstantConditions")
public class CorruptionStatusEffect extends MobEffect {
	/** Built on first use: several entries are Bewitchment effects that do not exist while effects are still registering. */
	private static Map<MobEffect, MobEffect> inverseEffects;

	public static Map<MobEffect, MobEffect> getInverseEffects() {
		if (inverseEffects == null) {
			Map<MobEffect, MobEffect> map = new HashMap<>();
			map.put(MobEffects.DAMAGE_BOOST, MobEffects.WEAKNESS);
			map.put(MobEffects.REGENERATION, MobEffects.POISON);
			map.put(MobEffects.NIGHT_VISION, MobEffects.BLINDNESS);
			map.put(MobEffects.DIG_SPEED, MobEffects.DIG_SLOWDOWN);
			map.put(MobEffects.MOVEMENT_SPEED, MobEffects.MOVEMENT_SLOWDOWN);
			map.put(MobEffects.JUMP, BWStatusEffects.SINKING.get());
			map.put(MobEffects.FIRE_RESISTANCE, BWStatusEffects.IGNITION.get());
			map.put(MobEffects.WATER_BREATHING, BWStatusEffects.GILLS.get());
			map.put(MobEffects.SLOW_FALLING, MobEffects.LEVITATION);
			map.put(BWStatusEffects.HARDENING.get(), BWStatusEffects.CORROSION.get());
			map.put(BWStatusEffects.ENCHANTED.get(), BWStatusEffects.INHIBITED.get());
			map.put(BWStatusEffects.NOURISHING.get(), MobEffects.HUNGER);
			inverseEffects = map;
		}
		return inverseEffects;
	}

	public CorruptionStatusEffect(MobEffectCategory category, int color) {
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
		if (!entity.level().isClientSide) {
			BuiltInRegistries.MOB_EFFECT.stream().forEach(effect -> {
				if (effect.getCategory() == MobEffectCategory.BENEFICIAL && entity.hasEffect(effect) && !entity.getEffect(effect).isAmbient()) {
					MobEffect inverse = getInverseEffects().get(effect);
					MobEffectInstance inverseEffect = null;
					if (inverse != null) {
						MobEffectInstance goodEffect = entity.getEffect(effect);
						inverseEffect = new MobEffectInstance(inverse, goodEffect.getDuration(), goodEffect.getAmplifier(), goodEffect.isAmbient(), goodEffect.isVisible(), goodEffect.showIcon());
					}
					entity.removeEffect(effect);
					if (inverseEffect != null) {
						entity.addEffect(inverseEffect);
					}
				}
			});
		}
	}
}
