/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.statuseffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class SinkingStatusEffect extends MobEffect {
	public SinkingStatusEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		Vec3 velocity = entity.getDeltaMovement();
		float amount = 0.05f * (amplifier + 1);
		if (!entity.onGround() && velocity.y() < 0) {
			entity.setDeltaMovement(velocity.multiply(1, 1 + amount, 1));
		}
		if (entity.isInWater() || entity.isFallFlying()) {
			entity.setDeltaMovement(velocity.add(0, -amount / 2, 0));
		}
	}
}
