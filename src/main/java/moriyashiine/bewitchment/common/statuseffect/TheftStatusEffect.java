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
import java.util.List;

public class TheftStatusEffect extends MobEffect {
	public TheftStatusEffect(MobEffectCategory statusEffectCategory, int color) {
		super(statusEffectCategory, color);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		if (!entity.level().isClientSide && entity.tickCount % 20 == 0) {
			entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(3 * (amplifier + 1)), foundEntity -> foundEntity != entity).forEach(livingEntity -> {
				List<MobEffectInstance> statusEffects = livingEntity.getActiveEffects().stream().filter(instance -> instance.getEffect().getCategory() == MobEffectCategory.BENEFICIAL && !instance.isAmbient()).toList();
				for (MobEffectInstance statusEffect : statusEffects) {
					entity.addEffect(new MobEffectInstance(statusEffect.getEffect(), statusEffect.getDuration() / 2, statusEffect.getAmplifier()));
					livingEntity.removeEffect(statusEffect.getEffect());
				}
			});
		}
	}

	@Override
	public void addAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
		super.addAttributeModifiers(entity, attributes, amplifier);
		entity.removeEffect(BWStatusEffects.SYNCHRONIZED);
	}
}
