/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class WebbedStatusEffect extends MobEffect {
	public WebbedStatusEffect(MobEffectCategory category, int color) {
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
			for (BlockPos foundPos : BWUtil.getBlockPoses(entity.blockPosition(), Math.min(3, amplifier + 1))) {
				if (entity.level().getWorldBorder().isWithinBounds(foundPos) && entity.level().getBlockState(foundPos).isAir()) {
					entity.level().setBlockAndUpdate(foundPos, BWObjects.TEMPORARY_COBWEB.get().defaultBlockState());
				}
			}
		}
	}
}
