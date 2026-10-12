/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.block.WitchCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WalkNodeEvaluator.class)
public abstract class LandPathNodeMakerMixin extends NodeEvaluator {
	@Inject(method = "isBurningBlock", at = @At("RETURN"), cancellable = true)
	private static void inflictsFireDamage(BlockState blockState, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValueZ() && blockState.getBlock() instanceof WitchCauldronBlock) {
			callbackInfo.setReturnValue(true);
		}
	}
}
