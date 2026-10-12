/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.curse;

import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWCurses;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PanicGoal.class)
public abstract class EscapeDangerGoalMixin extends Goal {
	@Shadow
	@Final
	protected PathfinderMob mob;

	@Inject(method = "canStart", at = @At("RETURN"), cancellable = true)
	private void canStart(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValueZ() && !mob.level().isClientSide && mob.tickCount % 20 == 0 && !mob.level().getEntitiesOfClass(LivingEntity.class, new AABB(mob.blockPosition()).inflate(8), living -> mob.hasLineOfSight(living) && living.isAlive() && BWComponents.CURSES_COMPONENT.get(living).hasCurse(BWCurses.APATHY)).isEmpty()) {
			callbackInfo.setReturnValue(true);
		}
	}
}
