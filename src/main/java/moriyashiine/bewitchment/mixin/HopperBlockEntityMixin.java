/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.block.entity.BrazierBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.IntStream;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {
	@Inject(method = "getSlots", at = @At("HEAD"), cancellable = true)
	private static void getAvailableSlots(Container inventory, Direction side, CallbackInfoReturnable<IntStream> callbackInfo) {
		if (inventory instanceof BrazierBlockEntity) {
			callbackInfo.setReturnValue(IntStream.empty());
		}
	}
}
