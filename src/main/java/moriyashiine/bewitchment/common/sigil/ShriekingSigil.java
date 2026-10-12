/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.sigil;

import moriyashiine.bewitchment.api.registry.Sigil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PressurePlateBlock;

public class ShriekingSigil extends Sigil {
	public ShriekingSigil(boolean active, int uses) {
		super(active, uses);
	}

	@Override
	public InteractionResult use(Level world, BlockPos pos, LivingEntity user, InteractionHand hand) {
		boolean flag = true;
		if (world.getBlockState(pos).getBlock() instanceof PressurePlateBlock) {
			flag = user.tickCount % 20 == 0;
		}
		if (flag) {
			if (!world.isClientSide) {
				world.playSound(null, pos, SoundEvents.GHAST_HURT, SoundSource.BLOCKS, 1, 1);
			}
			return InteractionResult.SUCCESS;
		}
		return super.use(world, pos, user, hand);
	}
}
