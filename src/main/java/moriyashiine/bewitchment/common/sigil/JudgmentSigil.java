/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.sigil;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.registry.Sigil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class JudgmentSigil extends Sigil {
	public JudgmentSigil(boolean active, int uses) {
		super(active, uses);
	}

	@Override
	public InteractionResult use(Level world, BlockPos pos, LivingEntity user, InteractionHand hand) {
		if (BewitchmentAPI.isWeakToSilver(user) && user.hurt(world.damageSources().magic(), 8)) {
			return InteractionResult.SUCCESS;
		}
		return super.use(world, pos, user, hand);
	}
}
