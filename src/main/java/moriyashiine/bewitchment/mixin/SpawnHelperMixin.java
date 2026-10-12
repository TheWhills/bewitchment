/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.world.BWWorldState;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NaturalSpawner.class)
public abstract class SpawnHelperMixin {
	@Inject(method = "isValidPositionForMob", at = @At("RETURN"), cancellable = true)
	private static void isValidSpawn(ServerLevel world, Mob entity, double squaredDistance, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValueZ() && entity instanceof Enemy) {
			BWWorldState worldState = BWWorldState.get(world);
			for (Long longPos : worldState.glowingBrambles) {
				if (new AABB(entity.blockPosition()).inflate(16).intersects(new AABB(BlockPos.of(longPos)))) {
					callbackInfo.setReturnValue(false);
				}
			}
		}
	}
}
