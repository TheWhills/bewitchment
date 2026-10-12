/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.poppet;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.misc.PoppetData;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Shadow
	public abstract void setDamage(int damage);

	@Shadow
	public abstract int getDamage();

	@Shadow
	public abstract int getMaxDamage();

	@Inject(method = "hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"), cancellable = true)
	private <T extends LivingEntity> void damage(int amount, T entity, Consumer<T> breakCallback, CallbackInfo callbackInfo) {
		if (getDamage() == getMaxDamage()) {
			PoppetData poppetData = BewitchmentAPI.getPoppet(entity.level(), BWObjects.MENDING_POPPET.get(), entity);
			if (!poppetData.stack().isEmpty()) {
				boolean sync = false;
				if (poppetData.stack().hurt(entity instanceof Player player && BewitchmentAPI.getFamiliar(player) == EntityType.WOLF && entity.getRandom().nextBoolean() ? 0 : 1, entity.getRandom(), null) && poppetData.stack().getDamageValue() == poppetData.stack().getMaxDamage()) {
					poppetData.stack().shrink(1);
					sync = true;
				}
				poppetData.update(entity.level(), sync);
				setDamage(0);
				callbackInfo.cancel();
			}
		}
	}
}
