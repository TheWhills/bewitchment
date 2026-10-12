/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.curse;

import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWCurses;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public class CropBlockMixin {
	@Inject(method = "isBonemealSuccess", at = @At("RETURN"), cancellable = true)
	private void canGrow(Level world, RandomSource random, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValueZ() && !world.isClientSide) {
			for (int i = 0; i < world.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(16), living -> living.isAlive() && BWComponents.CURSES_COMPONENT.get(living).hasCurse(BWCurses.ARMY_OF_WORMS)).size(); i++) {
				if (random.nextFloat() < 2 / 3f) {
					callbackInfo.setReturnValue(false);
				}
			}
		}
	}

	@Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
	private void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random, CallbackInfo callbackInfo) {
		for (int i = 0; i < world.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(16), living -> living.isAlive() && BWComponents.CURSES_COMPONENT.get(living).hasCurse(BWCurses.ARMY_OF_WORMS)).size(); i++) {
			if (random.nextFloat() < 2 / 3f) {
				callbackInfo.cancel();
			}
		}
	}
}
