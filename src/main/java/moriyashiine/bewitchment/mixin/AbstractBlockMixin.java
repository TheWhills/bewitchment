/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.block.entity.interfaces.Lockable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public abstract class AbstractBlockMixin {
	@Inject(method = "calcBlockBreakingDelta", at = @At("RETURN"), cancellable = true)
	private void calcBlockBreakingDelta(BlockState state, Player player, BlockGetter world, BlockPos pos, CallbackInfoReturnable<Float> callbackInfo) {
		if (callbackInfo.getReturnValueF() > 0 && world.getBlockEntity(state.getBlock() instanceof DoorBlock && state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos) instanceof Lockable lockable && lockable.getLocked() && !lockable.test(player)) {
			callbackInfo.setReturnValue(0f);
		}
	}
}
