/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class ActiveTargetGoalMixin<T extends LivingEntity> extends TargetGoal {
	@Shadow
	protected LivingEntity target;

	public ActiveTargetGoalMixin(Mob mob, boolean checkVisibility) {
		super(mob, checkVisibility);
	}

	@Inject(method = "findTarget", at = @At("TAIL"))
	private void findClosestTarget(CallbackInfo callbackInfo) {
		if (target instanceof Player player && BWComponents.FULL_INVISIBILITY_COMPONENT.get(player).isFullInvisible()) {
			target = null;
		} else if (BWComponents.MINION_COMPONENT.get(mob).getMaster() == null && mob.isInvertedHealAndHarm() && target != null && BWUtil.getArmorPieces(target, stack -> stack.getItem() == BWObjects.HARBINGER.get()) > 0) {
			target = null;
		}
	}
}
