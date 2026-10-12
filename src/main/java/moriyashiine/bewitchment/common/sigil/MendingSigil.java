/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.sigil;

import moriyashiine.bewitchment.api.registry.Sigil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PressurePlateBlock;

public class MendingSigil extends Sigil {
	public MendingSigil(boolean active, int uses) {
		super(active, uses);
	}

	@Override
	public InteractionResult use(Level world, BlockPos pos, LivingEntity user, InteractionHand hand) {
		InteractionResult result = super.use(world, pos, user, hand);
		boolean flag = true;
		if (world.getBlockState(pos).getBlock() instanceof PressurePlateBlock) {
			flag = user.tickCount % 20 == 0;
		}
		if (flag) {
			MobEffectInstance regeneration = new MobEffectInstance(MobEffects.REGENERATION, 200);
			if (user.canBeAffected(regeneration)) {
				if (!world.isClientSide) {
					user.addEffect(regeneration);
				}
				result = InteractionResult.SUCCESS;
			}
			MobEffectInstance resistance = new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200);
			if (user.canBeAffected(resistance)) {
				if (!world.isClientSide) {
					user.addEffect(resistance);
				}
				result = InteractionResult.SUCCESS;
			}
		}
		return result;
	}
}
