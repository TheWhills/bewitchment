/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.contract;

import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWContracts;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract boolean addStatusEffect(MobEffectInstance effect);

	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@ModifyVariable(method = "applyArmorToDamage", at = @At("HEAD"), argsOnly = true)
	private float modifyDamage(float amount, DamageSource source) {
		if (!level().isClientSide) {
			Entity directSource = source.getDirectEntity();
			if ((Object) this instanceof Player player && BWComponents.CONTRACTS_COMPONENT.get(player).hasContract(BWContracts.FAMINE)) {
				amount /= (2 - (player.getFoodData().getFoodLevel() / 20f));
			}
			if (directSource instanceof Player player && BWComponents.CONTRACTS_COMPONENT.get(player).hasContract(BWContracts.WRATH)) {
				amount *= (3 - 2 * (player.getHealth() / player.getMaxHealth()));
			}
			if (directSource instanceof Player player && BWComponents.CONTRACTS_COMPONENT.get(player).hasContract(BWContracts.PESTILENCE)) {
				addStatusEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100));
				addStatusEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100));
				addStatusEffect(new MobEffectInstance(MobEffects.WITHER, 100));
			}
		}
		return amount;
	}
}
