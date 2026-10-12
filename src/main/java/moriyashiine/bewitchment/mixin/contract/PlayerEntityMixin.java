/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.contract;

import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWContracts;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	@Shadow
	public abstract FoodData getHungerManager();

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@ModifyVariable(method = "giveExperiencePoints", at = @At("HEAD"), argsOnly = true)
	private int modifyAddExperience(int experience) {
		if (BWComponents.CONTRACTS_COMPONENT.get(this).hasContract(BWContracts.PRIDE.get())) {
			experience *= 2;
		}
		return experience;
	}

	@Inject(method = "eat", at = @At("HEAD"))
	private void eatFood(Level world, ItemStack stack, CallbackInfoReturnable<ItemStack> callbackInfo) {
		if (!world.isClientSide) {
			FoodProperties foodComponent = stack.getItem().getFoodProperties();
			if (foodComponent != null && BWComponents.CONTRACTS_COMPONENT.get(this).hasContract(BWContracts.GLUTTONY.get())) {
				getHungerManager().eat(foodComponent.getNutrition(), foodComponent.getSaturationModifier());
			}
		}
	}
}
