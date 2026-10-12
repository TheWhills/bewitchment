/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.curse;

import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWCurses;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderMan.class)
public class EndermanEntityMixin extends Monster {
	protected EndermanEntityMixin(EntityType<? extends Monster> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "isPlayerStaring", at = @At("RETURN"), cancellable = true)
	private void isPlayerStaring(Player player, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValueZ() && !player.isCreative() && getTarget() == null && BWComponents.CURSES_COMPONENT.get(player).hasCurse(BWCurses.OUTRAGE) && distanceTo(player) <= getAttributeValue(Attributes.FOLLOW_RANGE) / 4 && hasLineOfSight(player)) {
			callbackInfo.setReturnValue(true);
		}
	}
}
