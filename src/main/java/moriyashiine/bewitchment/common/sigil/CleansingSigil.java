/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.sigil;

import moriyashiine.bewitchment.api.registry.Sigil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PressurePlateBlock;

public class CleansingSigil extends Sigil {
	public CleansingSigil(boolean active, int uses) {
		super(active, uses);
	}

	@Override
	public InteractionResult use(Level world, BlockPos pos, LivingEntity user, InteractionHand hand) {
		boolean flag = true;
		if (world.getBlockState(pos).getBlock() instanceof PressurePlateBlock) {
			flag = user.tickCount % 20 == 0;
		}
		if (flag) {
			flag = false;
			for (MobEffect effect : BuiltInRegistries.MOB_EFFECT) {
				if (user.hasEffect(effect) && effect.getCategory() == MobEffectCategory.HARMFUL) {
					if (!world.isClientSide) {
						user.removeEffect(effect);
					}
					flag = true;
				}
			}
			if (flag) {
				return InteractionResult.SUCCESS;
			}
		}
		return super.use(world, pos, user, hand);
	}
}
