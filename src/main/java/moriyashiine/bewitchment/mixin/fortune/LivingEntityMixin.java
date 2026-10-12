/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.fortune;

import moriyashiine.bewitchment.api.component.FortuneComponent;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWFortunes;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@ModifyVariable(method = "getDamageAfterArmorAbsorb", at = @At("HEAD"), argsOnly = true)
	private float modifyDamage(float amount, DamageSource source) {
		if (!level().isClientSide && amount > 0 && source.is(DamageTypeTags.IS_PROJECTILE) && source.getEntity() instanceof Player player) {
			FortuneComponent fortuneComponent = BWComponents.FORTUNE_COMPONENT.get(player);
			if (fortuneComponent.getFortune() != null && fortuneComponent.getFortune().fortune == BWFortunes.HAWKEYE) {
				fortuneComponent.getFortune().duration = 0;
				amount *= 3;
			}
		}
		return amount;
	}
}
