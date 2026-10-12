/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.CoffinBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Lets vanilla block entity types accept Bewitchment blocks that reuse them:
 * coffins use the bed block entity, and Bewitchment's signs use the vanilla
 * sign / hanging sign block entities (so the vanilla renderers and editors work).
 */
@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {
	@Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
	private void supports(BlockState state, CallbackInfoReturnable<Boolean> callbackInfo) {
		Object self = this;
		Block block = state.getBlock();
		if (self == BlockEntityType.BED && block instanceof CoffinBlock) {
			callbackInfo.setReturnValue(true);
		} else if (block instanceof SignBlock sign && sign.type().name().startsWith(Bewitchment.MOD_ID + ":")) {
			if ((self == BlockEntityType.SIGN && (block instanceof StandingSignBlock || block instanceof WallSignBlock))
					|| (self == BlockEntityType.HANGING_SIGN && (block instanceof CeilingHangingSignBlock || block instanceof WallHangingSignBlock))) {
				callbackInfo.setReturnValue(true);
			}
		}
	}
}
