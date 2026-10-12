/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.sigil;

import moriyashiine.bewitchment.api.registry.Sigil;
import moriyashiine.bewitchment.common.entity.living.GhostEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PressurePlateBlock;

public class SentinelSigil extends Sigil {
	public SentinelSigil(boolean active, int uses) {
		super(active, uses);
	}

	@Override
	public InteractionResult use(Level world, BlockPos pos, LivingEntity user, InteractionHand hand) {
		boolean flag = true;
		if (world.getBlockState(pos).getBlock() instanceof PressurePlateBlock) {
			flag = user.tickCount % 20 == 0;
		}
		if (flag) {
			MobEffectInstance effect = GhostEntity.getEffect(world.random.nextInt(4), 300);
			if (user.canBeAffected(effect)) {
				if (!world.isClientSide) {
					user.addEffect(effect);
				}
				return InteractionResult.SUCCESS;
			}
		}
		return super.use(world, pos, user, hand);
	}
}
