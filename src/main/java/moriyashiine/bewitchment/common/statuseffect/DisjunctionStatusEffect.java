/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.common.misc.BWUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class DisjunctionStatusEffect extends MobEffect {
	public DisjunctionStatusEffect(MobEffectCategory category, int color) {
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
			BWUtil.attemptTeleport(entity, entity.blockPosition(), 8 * (amplifier + 1), true);
		}
	}
}
