/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.entity.living.BaphometEntity;
import moriyashiine.bewitchment.common.entity.living.LilithEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHurtingProjectile.class)
public abstract class ExplosiveProjectileEntityMixin extends Entity {
	public ExplosiveProjectileEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "canHit(Lnet/minecraft/entity/Entity;)Z", at = @At("RETURN"), cancellable = true)
	private void canHit(Entity entity, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValueZ() && !level().isClientSide) {
			Entity owner = ((Projectile) (Object) this).getOwner();
			if (owner instanceof BaphometEntity && entity instanceof LargeFireball) {
				callbackInfo.setReturnValue(false);
			} else if (owner instanceof LilithEntity && entity instanceof WitherSkull) {
				callbackInfo.setReturnValue(false);
			}
		}
	}
}
