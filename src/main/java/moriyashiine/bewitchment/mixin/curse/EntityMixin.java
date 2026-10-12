/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.curse;

import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ConstantConditions")
@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	private Level world;

	@Inject(method = "setSecondsOnFire", at = @At("HEAD"), cancellable = true)
	private void setOnFireFor(int seconds, CallbackInfo callbackInfo) {
		if (!world.isClientSide && (Object) this instanceof Mob mob && BWComponents.FAKE_MOB_COMPONENT.get(mob).getTarget() != null) {
			callbackInfo.cancel();
		}
	}
}
