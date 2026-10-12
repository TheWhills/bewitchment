/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.familiar;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creeper.class)
public class CreeperEntityMixin extends Monster {
	protected CreeperEntityMixin(EntityType<? extends Monster> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "registerGoals", at = @At("TAIL"))
	private void initGoals(CallbackInfo callbackInfo) {
		goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 6, 1, 1.2, entity -> entity instanceof Player player && BewitchmentAPI.getFamiliar(player) == EntityType.CAT));
	}
}
