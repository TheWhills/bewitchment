/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class SynchronizedStatusEffect extends MobEffect {
	public SynchronizedStatusEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		if (!entity.level().isClientSide && entity.tickCount % 20 == 0) {
			entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(3 * (amplifier + 1)), foundEntity -> foundEntity != entity).forEach(livingEntity -> entity.getActiveEffects().forEach(effect -> {
				if (effect.getEffect() != this) {
					livingEntity.addEffect(new MobEffectInstance(effect.getEffect(), effect.getDuration() / 2, effect.getAmplifier()));
				}
			}));
		}
	}

	@Override
	public void addAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
		super.addAttributeModifiers(entity, attributes, amplifier);
		entity.removeEffect(BWStatusEffects.THEFT);
	}
}
