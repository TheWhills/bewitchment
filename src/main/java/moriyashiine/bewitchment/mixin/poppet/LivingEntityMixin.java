/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.poppet;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.event.ReviveEvents;
import moriyashiine.bewitchment.api.item.PoppetItem;
import moriyashiine.bewitchment.api.misc.PoppetData;
import moriyashiine.bewitchment.client.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.BWConfig;
import moriyashiine.bewitchment.common.entity.living.VampireEntity;
import moriyashiine.bewitchment.common.entity.living.WerewolfEntity;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract boolean addStatusEffect(MobEffectInstance effect);

	@Shadow
	public abstract boolean clearStatusEffects();

	@Shadow
	public abstract void setHealth(float health);

	@Shadow
	protected abstract float getSoundVolume();

	@Shadow
	public abstract float getSoundPitch();

	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Redirect(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isSubmergedIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
	private boolean bewitchment$voodooDrownEffect(LivingEntity obj, TagKey<Fluid> tagKey) {
		if (BWComponents.ADDITIONAL_WATER_DATA_COMPONENT.get(this).isSubmerged()) {
			return true;
		}
		return obj.isEyeInFluid(tagKey);
	}

	@ModifyVariable(method = "applyArmorToDamage", at = @At("HEAD"), argsOnly = true)
	private float modifyDamage(float amount, DamageSource source) {
		if (!level().isClientSide) {
			if (amount > 0 && (Object) this instanceof Player player && !BewitchmentAPI.isVampire(this, true)) {
				PoppetData poppetData = BewitchmentAPI.getPoppetFromInventory(level(), BWObjects.VAMPIRIC_POPPET, null, Stream.concat(player.getInventory().items.stream(), player.getInventory().offhand.stream()).collect(Collectors.toList()));
				if (!poppetData.stack().isEmpty()) {
					LivingEntity owner = BewitchmentAPI.getTaglockOwner(level(), poppetData.stack());
					if (!BewitchmentAPI.isVampire(owner, true) && !getUUID().equals(owner.getUUID()) && owner.hurt(BWDamageSources.create(owner.level(), BWDamageSources.VAMPIRE), amount)) {
						boolean sync = false;
						if (poppetData.stack().hurt((int) (amount * (BewitchmentAPI.getFamiliar(player) == EntityType.WOLF && random.nextBoolean() ? 0.5f : 1)), random, null) && poppetData.stack().getDamageValue() >= poppetData.stack().getMaxDamage()) {
							poppetData.stack().shrink(1);
							sync = true;
						}
						poppetData.update(level(), sync);
						return 0;
					}
				}
			}
			if (PoppetItem.PROTECTION_POPPET_SOURCES.test(source)) {
				PoppetData poppetData = BewitchmentAPI.getPoppet(level(), BWObjects.PROTECTION_POPPET, this);
				if (!poppetData.stack().isEmpty()) {
					boolean sync = false;
					if (poppetData.stack().hurt((int) (amount * ((Object) this instanceof Player player && BewitchmentAPI.getFamiliar(player) == EntityType.WOLF && random.nextBoolean() ? 0.5f : 1)), random, null) && poppetData.stack().getDamageValue() >= poppetData.stack().getMaxDamage()) {
						poppetData.stack().shrink(1);
						sync = true;
					}
					poppetData.update(level(), sync);
					return 0;
				}
			}
			if (source.getEntity() instanceof LivingEntity livingAttacker && BewitchmentAPI.isWeakToSilver(livingAttacker)) {
				PoppetData poppetData = BewitchmentAPI.getPoppet(level(), BWObjects.JUDGMENT_POPPET, this);
				if (!poppetData.stack().isEmpty()) {
					boolean sync = false;
					if (poppetData.stack().hurt((Object) this instanceof Player && BewitchmentAPI.getFamiliar((Player) (Object) this) == EntityType.WOLF && random.nextBoolean() ? 0 : 1, random, null) && poppetData.stack().getDamageValue() >= poppetData.stack().getMaxDamage()) {
						poppetData.stack().shrink(1);
						sync = true;
					}
					poppetData.update(level(), sync);
					amount /= 4;
				}
			}
			if (source.getDirectEntity() instanceof LivingEntity livingSource) {
				PoppetData poppetData = BewitchmentAPI.getPoppet(level(), BWObjects.FATIGUE_POPPET, this);
				boolean sync = false;
				if (!poppetData.stack().isEmpty() && livingSource.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 1)) && poppetData.stack().hurt((Object) this instanceof Player player && BewitchmentAPI.getFamiliar(player) == EntityType.WOLF && random.nextBoolean() ? 0 : 1, random, null) && poppetData.stack().getDamageValue() >= poppetData.stack().getMaxDamage()) {
					poppetData.stack().shrink(1);
					sync = true;
				}
				poppetData.update(level(), sync);
			}
		}
		return amount;
	}

	@Inject(method = "tryUseTotem", at = @At("RETURN"), cancellable = true)
	private void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!level().isClientSide) {
			if (!callbackInfo.getReturnValueZ()) {
				boolean isPlayer = (Object) this instanceof Player;
				PoppetData poppetData = BewitchmentAPI.getPoppet(level(), BWObjects.DEATH_PROTECTION_POPPET, this);
				if (!poppetData.stack().isEmpty() && !(isPlayer && ReviveEvents.CANCEL_REVIVE.invoker().shouldCancel((Player) (Object) this, source, poppetData.stack()))) {
					if (isPlayer) {
						ReviveEvents.ON_REVIVE.invoker().onRevive((Player) (Object) this, source, poppetData.stack());
					}
					boolean sync = false;
					if (poppetData.stack().hurt((Object) this instanceof Player player && BewitchmentAPI.getFamiliar(player) == EntityType.WOLF && random.nextBoolean() ? 0 : 1, random, null) && poppetData.stack().getDamageValue() >= poppetData.stack().getMaxDamage()) {
						poppetData.stack().shrink(1);
						sync = true;
					}
					poppetData.update(level(), sync);
					setHealth(1);
					clearStatusEffects();
					addStatusEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
					addStatusEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
					addStatusEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
					callbackInfo.setReturnValue(true);
				}
			}
			if (callbackInfo.getReturnValueZ() && (Object) this instanceof ServerPlayer player && (!BWConfig.enableCurses || BWComponents.CURSES_COMPONENT.get(player).hasCurse(BWCurses.SUSCEPTIBILITY))) {
				BWComponents.TRANSFORMATION_COMPONENT.maybeGet(player).ifPresent(transformationComponent -> {
					if (transformationComponent.getTransformation() == BWTransformations.HUMAN) {
						if (source.getDirectEntity() instanceof VampireEntity || (source.getDirectEntity() instanceof Player playerSource && BewitchmentAPI.isVampire(playerSource, true) && BewitchmentAPI.isPledged(playerSource, BWPledges.LILITH))) {
							transformationComponent.getTransformation().onRemoved(player);
							transformationComponent.setTransformation(BWTransformations.VAMPIRE);
							transformationComponent.getTransformation().onAdded(player);
							PlayerLookup.tracking(this).forEach(trackingPlayer -> SpawnSmokeParticlesPacket.send(trackingPlayer, this));
							SpawnSmokeParticlesPacket.send(player, this);
							level().playSound(null, blockPosition(), BWSoundEvents.ENTITY_GENERIC_CURSE, getSoundSource(), getSoundVolume(), getSoundPitch());
						} else if (source.getDirectEntity() instanceof WerewolfEntity || (source.getDirectEntity() instanceof Player playerSource && BewitchmentAPI.isWerewolf(playerSource, false) && BewitchmentAPI.isPledged(playerSource, BWPledges.HERNE))) {
							transformationComponent.getTransformation().onRemoved(player);
							transformationComponent.setTransformation(BWTransformations.WEREWOLF);
							transformationComponent.getTransformation().onAdded(player);
							int variant = -1;
							if (source.getDirectEntity() instanceof WerewolfEntity) {
								variant = source.getDirectEntity().getEntityData().get(BWHostileEntity.VARIANT);
							} else if (source.getDirectEntity() instanceof Player playerSource) {
								variant = BWComponents.ADDITIONAL_WEREWOLF_DATA_COMPONENT.get(playerSource).getVariant();
							}
							if (variant > -1) {
								BWComponents.ADDITIONAL_WEREWOLF_DATA_COMPONENT.get(player).setVariant(variant);
							}
							PlayerLookup.tracking(this).forEach(trackingPlayer -> SpawnSmokeParticlesPacket.send(trackingPlayer, this));
							SpawnSmokeParticlesPacket.send(player, this);
							level().playSound(null, blockPosition(), BWSoundEvents.ENTITY_GENERIC_CURSE, getSoundSource(), getSoundVolume(), getSoundPitch());
						}
					}
				});
			}
		}
	}
}
