/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	public abstract UUID getUuid();

	@Shadow
	private Level world;

	@Inject(method = "isInvulnerableTo", at = @At("RETURN"), cancellable = true)
	private void isInvulnerableTo(DamageSource source, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValueZ() && !world.isClientSide && (Object) this instanceof Mob mob) {
			if (source.getEntity() instanceof LivingEntity living) {
				if (living.getUUID().equals(BWComponents.MINION_COMPONENT.get(mob).getMaster())) {
					callbackInfo.setReturnValue(true);
				} else if (living instanceof Mob mobAttacker && getUuid().equals(BWComponents.MINION_COMPONENT.get(mobAttacker).getMaster())) {
					callbackInfo.setReturnValue(true);
				}
			}
		}
	}
}
