/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.transformation;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@SuppressWarnings("ConstantConditions")
@Mixin(Entity.class)
public abstract class EntityMixin {
	@ModifyVariable(method = "setPose", at = @At("HEAD"), argsOnly = true)
	private Pose modifySetPose(Pose pose) {
		if (((Object) this) instanceof Player player && (BewitchmentAPI.isVampire(player, false) || BewitchmentAPI.isWerewolf(player, false)) && (pose == Pose.FALL_FLYING || pose == Pose.SWIMMING)) {
			return Pose.STANDING;
		}
		return pose;
	}
}
