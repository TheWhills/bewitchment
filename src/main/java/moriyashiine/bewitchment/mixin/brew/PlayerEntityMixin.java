/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.brew;

import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	@Shadow
	public abstract FoodData getFoodData();

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "eat", at = @At("HEAD"))
	private void eatFood(Level world, ItemStack stack, CallbackInfoReturnable<ItemStack> callbackInfo) {
		if (!world.isClientSide && hasEffect(BWStatusEffects.NOURISHING.get())) {
			getFoodData().eat(getEffect(BWStatusEffects.NOURISHING.get()).getAmplifier() + 2, 0.5f);
		}
	}
}
