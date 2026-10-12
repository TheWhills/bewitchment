/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.mixin.brew.LivingEntityAccessor;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class GillsStatusEffect extends MobEffect {
	public GillsStatusEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		boolean damage = false;
		if (!entity.isUnderWater() && !entity.level().isRainingAt(entity.blockPosition().above())) {
			entity.setAirSupply(((LivingEntityAccessor) entity).bw_getNextAirUnderwater(entity.getAirSupply() - ((LivingEntityAccessor) entity).bw_getNextAirOnLand(0)));
			if (entity.getAirSupply() == -20) {
				damage = true;
			}
		} else if (entity.getAirSupply() < entity.getMaxAirSupply()) {
			entity.setAirSupply(((LivingEntityAccessor) entity).bw_getNextAirOnLand(entity.getAirSupply()));
			return;
		}
		if (!damage) {
			damage = entity.getAirSupply() < -20;
		}
		if (damage) {
			entity.hurt(entity.level().damageSources().generic(), 2);
			entity.setAirSupply(0);
		}
	}
}
