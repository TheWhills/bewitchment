/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class Sigil {
	public final boolean active;
	public final int uses;

	public Sigil(boolean active, int uses) {
		this.active = active;
		this.uses = uses;
	}

	public int tick(Level world, BlockPos pos) {
		return 0;
	}

	public InteractionResult use(Level world, BlockPos pos, LivingEntity user, InteractionHand hand) {
		return InteractionResult.PASS;
	}
}
