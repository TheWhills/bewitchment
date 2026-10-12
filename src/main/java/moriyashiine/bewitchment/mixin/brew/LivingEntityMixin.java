/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.brew;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.client.packet.SpawnExplosionParticlesPacket;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	@Nullable
	public abstract MobEffectInstance getStatusEffect(MobEffect effect);

	@Shadow
	public abstract boolean hasStatusEffect(MobEffect effect);

	@Shadow
	public abstract boolean removeStatusEffect(MobEffect type);

	@Shadow
	public abstract void heal(float amount);

	@Shadow
	public int hurtTime;

	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
	private DamageSource modifyDamage0(DamageSource source) {
		if (!level().isClientSide) {
			Entity attacker = source.getDirectEntity();
			if (attacker instanceof LivingEntity livingAttacker && livingAttacker.hasEffect(BWStatusEffects.ENCHANTED)) {
				return level().damageSources().indirectMagic(livingAttacker, livingAttacker);
			}
		}
		return source;
	}

	@ModifyVariable(method = "applyArmorToDamage", at = @At("HEAD"), argsOnly = true)
	private float modifyDamage1(float amount, DamageSource source) {
		if (!level().isClientSide) {
			if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && (hasStatusEffect(BWStatusEffects.ETHEREAL) || (source.getEntity() instanceof LivingEntity livingAttacker && livingAttacker.hasEffect(BWStatusEffects.ETHEREAL)))) {
				return 0;
			}
			if (source.getDirectEntity() instanceof LivingEntity livingSource && livingSource.hasEffect(BWStatusEffects.ENCHANTED)) {
				amount /= 4;
				amount += livingSource.getEffect(BWStatusEffects.ENCHANTED).getAmplifier();
			}
			if (hasStatusEffect(BWStatusEffects.MAGIC_SPONGE) && source.is(DamageTypeTags.WITCH_RESISTANT_TO)) {
				float magicAmount = (0.3f + (0.1f * getStatusEffect(BWStatusEffects.MAGIC_SPONGE).getAmplifier()));
				amount *= (1 - magicAmount);
				if ((Object) this instanceof Player player) {
					BewitchmentAPI.fillMagic(player, (int) (amount * magicAmount), false);
				}
			}
		}
		return amount;
	}

	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!level().isClientSide) {
			Entity directSource = source.getDirectEntity();
			if (hasStatusEffect(BWStatusEffects.DEFLECTION) && directSource != null && directSource.getType().is(EntityTypeTags.ARROWS)) {
				int amplifier = getStatusEffect(BWStatusEffects.DEFLECTION).getAmplifier() + 1;
				Vec3 velocity = directSource.getDeltaMovement();
				directSource.setDeltaMovement(velocity.x() * 2 * amplifier, velocity.y() * 2 * amplifier, velocity.z() * 2 * amplifier);
				callbackInfo.setReturnValue(false);
			} else if (amount > 0 && hurtTime == 0) {
				if (!hasStatusEffect(MobEffects.DAMAGE_BOOST) && !hasStatusEffect(MobEffects.REGENERATION) && !hasStatusEffect(MobEffects.DAMAGE_RESISTANCE) && directSource instanceof LivingEntity livingSource && livingSource.hasEffect(BWStatusEffects.LEECHING)) {
					livingSource.heal(amount * (livingSource.getEffect(BWStatusEffects.LEECHING).getAmplifier() + 1) / 8);
				}
				if (directSource != null && hasStatusEffect(BWStatusEffects.THORNS) && source.is(DamageTypes.THORNS)) {
					directSource.hurt(level().damageSources().thorns(directSource), 2 * (getStatusEffect(BWStatusEffects.THORNS).getAmplifier() + 1));
				}
				if (hasStatusEffect(BWStatusEffects.VOLATILITY) && !source.is(DamageTypeTags.IS_EXPLOSION)) {
					for (LivingEntity entity : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(3), foundEntity -> foundEntity.isAlive() && !foundEntity.getUUID().equals(getUUID()))) {
						entity.hurt(level().damageSources().explosion(((LivingEntity) (Object) this), ((LivingEntity) (Object) this)), 4 * (getStatusEffect(BWStatusEffects.VOLATILITY).getAmplifier() + 1));
					}
					level().playSound(null, blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 1, 1);
					PlayerLookup.tracking(this).forEach(trackingPlayer -> SpawnExplosionParticlesPacket.send(trackingPlayer, this));
					if (((Object) this) instanceof ServerPlayer player) {
						SpawnExplosionParticlesPacket.send(player, this);
					}
					removeStatusEffect(BWStatusEffects.VOLATILITY);
				}
			}
		}
	}

	@Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At(value = "HEAD"))
	private void addStatusEffect(MobEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
		if (effect.getEffect() == BWStatusEffects.POLYMORPH && source instanceof AreaEffectCloud) {
			BWComponents.POLYMORPH_COMPONENT.maybeGet(source).ifPresent(sourcePolymorphComponent -> {
				if (sourcePolymorphComponent.getUuid() != null) {
					BWComponents.POLYMORPH_COMPONENT.maybeGet(LivingEntity.class.cast(this)).ifPresent(entityPolymorphComponent -> {
						entityPolymorphComponent.setUuid(sourcePolymorphComponent.getUuid());
						entityPolymorphComponent.setName(sourcePolymorphComponent.getName());
					});
				}
			});
		}
	}

	@Inject(method = "canBreatheInWater", at = @At("RETURN"), cancellable = true)
	private void canBreatheInWater(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValueZ() && !level().isClientSide && hasStatusEffect(BWStatusEffects.GILLS)) {
			callbackInfo.setReturnValue(true);
		}
	}

	@Inject(method = "isClimbing", at = @At("RETURN"), cancellable = true)
	private void isClimbing(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValueZ() && hasStatusEffect(BWStatusEffects.CLIMBING) && horizontalCollision) {
			callbackInfo.setReturnValue(true);
		}
	}
}
