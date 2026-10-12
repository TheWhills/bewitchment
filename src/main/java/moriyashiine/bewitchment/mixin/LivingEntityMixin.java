/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.entity.Pledgeable;
import moriyashiine.bewitchment.common.entity.living.BaphometEntity;
import moriyashiine.bewitchment.common.entity.living.HerneEntity;
import moriyashiine.bewitchment.common.entity.living.LeonardEntity;
import moriyashiine.bewitchment.common.entity.living.LilithEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.*;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract boolean removeEffect(MobEffect type);

	@Shadow
	public abstract Iterable<ItemStack> getArmorSlots();

	@Shadow
	public abstract boolean hurt(DamageSource source, float amount);

	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (!level().isClientSide) {
			LivingEntity living = (LivingEntity) (Object) this;
			int damage = 0;
			if (living.getMainHandItem().getItem() == BWObjects.GARLIC.get() && BewitchmentAPI.isVampire(this, true)) {
				damage++;
			}
			if (living.getOffhandItem().getItem() == BWObjects.GARLIC.get() && BewitchmentAPI.isVampire(this, true)) {
				damage++;
			}
			if (living.getMainHandItem().getItem() == BWObjects.ACONITE.get() && BewitchmentAPI.isWerewolf(this, true)) {
				damage++;
			}
			if (living.getOffhandItem().getItem() == BWObjects.ACONITE.get() && BewitchmentAPI.isWerewolf(this, true)) {
				damage++;
			}
			if (damage > 0) {
				hurt(BWDamageSources.create(level(), BWDamageSources.MAGIC_COPY), damage);
			}
		}
	}

	@ModifyVariable(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), argsOnly = true)
	private MobEffectInstance modifyStatusEffect(MobEffectInstance effect) {
		if (!level().isClientSide && !effect.isAmbient() && !effect.getEffect().isInstantenous() && effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
			float durationMultiplier = 1;
			for (ItemStack stack : getArmorSlots()) {
				durationMultiplier -= EnchantmentHelper.getItemEnchantmentLevel(BWEnchantments.MAGIC_PROTECTION.get(), stack) / 32f;
			}
			if (durationMultiplier < 1) {
				return new MobEffectInstance(effect.getEffect(), (int) (effect.getDuration() * durationMultiplier), effect.getAmplifier(), false, effect.isVisible(), effect.showIcon());
			}
		}
		return effect;
	}

	@ModifyVariable(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getHealth()F"), ordinal = 0, argsOnly = true)
	private float modifyDamage0(float amount, DamageSource source) {
		if (!level().isClientSide) {
			amount = BWDamageSources.handleDamage((LivingEntity) (Object) this, source, amount);
		}
		return amount;
	}

	@ModifyVariable(method = "getDamageAfterArmorAbsorb", at = @At("HEAD"), argsOnly = true)
	private float modifyDamage1(float amount, DamageSource source) {
		if (!level().isClientSide) {
			Entity attacker = source.getEntity();
			Entity directSource = source.getDirectEntity();
			if (directSource instanceof LargeFireball fireball) {
				if (attacker instanceof Player && BWComponents.CADUCEUS_FIREBALL_COMPONENT.get(fireball).isFromCaduceus()) {
					amount *= 1.5f;
				}
				if (attacker instanceof BaphometEntity) {
					amount *= 3;
				}
			}
			if (directSource instanceof WitherSkull && attacker instanceof LilithEntity) {
				amount *= 3;
			}
			if (source.is(DamageTypeTags.WITCH_RESISTANT_TO) && (Object) this instanceof LivingEntity living) {
				int armorPieces = BWUtil.getArmorPieces(living, stack -> {
					if (stack.getItem() instanceof ArmorItem armorItem) {
						ArmorMaterial material = armorItem.getMaterial();
						return material == BWMaterials.HEDGEWITCH_ARMOR || material == BWMaterials.ALCHEMIST_ARMOR || material == BWMaterials.BESMIRCHED_ARMOR || material == BWMaterials.HARBINGER_ARMOR;
					}
					return false;
				});
				if (armorPieces > 0) {
					amount *= (1 - (0.2f * armorPieces));
				}
			}
			if ((getVehicle() != null && getVehicle().getType() == BWEntityTypes.CYPRESS_BROOM.get()) || (source.getEntity() != null && source.getEntity().getVehicle() != null && source.getEntity().getVehicle().getType() == BWEntityTypes.CYPRESS_BROOM.get())) {
				amount *= 0.2f;
			}
		}
		return amount;
	}

	@Inject(method = "hurt", at = @At("HEAD"))
	private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!level().isClientSide) {
			Entity attacker = source.getEntity();
			if (attacker instanceof LeonardEntity) {
				removeEffect(BWStatusEffects.MAGIC_SPONGE.get());
			}
			if (attacker instanceof BaphometEntity) {
				removeEffect(MobEffects.FIRE_RESISTANCE);
			}
			if (attacker instanceof LilithEntity) {
				removeEffect(MobEffects.DAMAGE_BOOST);
			}
			if (attacker instanceof HerneEntity) {
				removeEffect(MobEffects.DAMAGE_RESISTANCE);
			}
			if (attacker instanceof Pledgeable pledgeable) {
				pledgeable.setTimeSinceLastAttack(0);
			}
		}
	}

	@Inject(method = "dropFromLootTable", at = @At("HEAD"), cancellable = true)
	private void dropLoot(DamageSource source, boolean causedByPlayer, CallbackInfo callbackInfo) {
		if ((Object) this instanceof Mob mob && BWComponents.MINION_COMPONENT.get(mob).getMaster() != null) {
			callbackInfo.cancel();
		}
	}

	@Inject(method = "isAffectedByPotions", at = @At("RETURN"), cancellable = true)
	private void isAffectedBySplashPotions(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValueZ() && (Object) this instanceof Mob mob && BWComponents.MINION_COMPONENT.get(mob).getMaster() != null) {
			callbackInfo.setReturnValue(false);
		}
	}
}
