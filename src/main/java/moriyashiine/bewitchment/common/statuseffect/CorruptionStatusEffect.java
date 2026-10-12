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
	public static final Map<MobEffect, MobEffect> INVERSE_EFFECTS = new HashMap<>();

	static {
		INVERSE_EFFECTS.put(MobEffects.DAMAGE_BOOST, MobEffects.WEAKNESS);
		INVERSE_EFFECTS.put(MobEffects.REGENERATION, MobEffects.POISON);
		INVERSE_EFFECTS.put(MobEffects.NIGHT_VISION, MobEffects.BLINDNESS);
		INVERSE_EFFECTS.put(MobEffects.DIG_SPEED, MobEffects.DIG_SLOWDOWN);
		INVERSE_EFFECTS.put(MobEffects.MOVEMENT_SPEED, MobEffects.MOVEMENT_SLOWDOWN);
		INVERSE_EFFECTS.put(MobEffects.JUMP, BWStatusEffects.SINKING.get());
		INVERSE_EFFECTS.put(MobEffects.FIRE_RESISTANCE, BWStatusEffects.IGNITION.get());
		INVERSE_EFFECTS.put(MobEffects.WATER_BREATHING, BWStatusEffects.GILLS.get());
		INVERSE_EFFECTS.put(MobEffects.SLOW_FALLING, MobEffects.LEVITATION);
		INVERSE_EFFECTS.put(BWStatusEffects.HARDENING.get(), BWStatusEffects.CORROSION.get());
		INVERSE_EFFECTS.put(BWStatusEffects.ENCHANTED.get(), BWStatusEffects.INHIBITED.get());
		INVERSE_EFFECTS.put(BWStatusEffects.NOURISHING.get(), MobEffects.HUNGER);
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
					MobEffect inverse = INVERSE_EFFECTS.get(effect);
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
