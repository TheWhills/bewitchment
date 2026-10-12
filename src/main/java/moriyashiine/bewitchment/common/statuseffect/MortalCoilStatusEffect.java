/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.common.registry.BWDamageSources;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

@SuppressWarnings("ConstantConditions")
public class MortalCoilStatusEffect extends MobEffect {
	public MortalCoilStatusEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		if (entity.getEffect(this).getDuration() == 1) {
			entity.hurt(BWDamageSources.create(entity.level(), BWDamageSources.DEATH), Float.MAX_VALUE);
		}
	}
}
