/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.contract;

import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWContracts;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BreedGoal.class)
public abstract class AnimalMateGoalMixin extends Goal {
	@Shadow
	@Final
	protected Animal animal;

	@Shadow
	protected Animal mate;

	@Inject(method = "breed", at = @At("HEAD"))
	private void breed(CallbackInfo callbackInfo) {
		ServerPlayer player = animal.getLoveCause();
		if (player != null && BWComponents.CONTRACTS_COMPONENT.get(player).hasContract(BWContracts.LUST)) {
			animal.spawnChildFromBreeding(player.serverLevel(), mate);
			animal.spawnChildFromBreeding(player.serverLevel(), mate);
		}
	}
}
