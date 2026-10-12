/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.curse;

import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWCurses;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin {
	@Inject(method = "spawnEntity", at = @At("HEAD"))
	private void spawnEntity(Entity entity, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (entity instanceof LightningBolt) {
			LivingEntity closest = null;
			for (LivingEntity foundLiving : entity.level().getEntitiesOfClass(LivingEntity.class, new AABB(entity.blockPosition()).inflate(256), currentLiving -> currentLiving.isAlive() && BWComponents.CURSES_COMPONENT.get(currentLiving).hasCurse(BWCurses.LIGHTNING_ROD))) {
				if (closest == null || foundLiving.distanceTo(entity) < closest.distanceTo(entity)) {
					closest = foundLiving;
				}
			}
			if (closest != null) {
				entity.absMoveTo(closest.getX(), closest.getY(), closest.getZ(), entity.level().random.nextFloat() * 360, 0);
			}
		}
	}
}
