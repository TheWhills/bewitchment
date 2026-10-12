/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.transformation;

import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Villager.class)
public abstract class VillagerEntityMixin {
	@Inject(method = "interactMob", at = @At("HEAD"))
	private void interactMob(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> callbackInfo) {
		BWComponents.WEREWOLF_VILLAGER_COMPONENT.get(this).setDespawnTimer(-1);
	}
}
