/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageEnchantment.class)
public abstract class DamageEnchantmentMixin {
	@Shadow
	@Final
	public int type;

	@Shadow
	public abstract float getDamageBonus(int level, MobType group);

	@Inject(method = "getDamageBonus", at = @At("HEAD"), cancellable = true)
	private void getDamageBonus(int level, MobType group, CallbackInfoReturnable<Float> callbackInfo) {
		if (type == 1 && group == BewitchmentAPI.DEMON) {
			callbackInfo.setReturnValue(getDamageBonus(level, MobType.UNDEAD));
		}
	}
}
