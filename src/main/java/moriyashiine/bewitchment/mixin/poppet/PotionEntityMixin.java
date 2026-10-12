/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.poppet;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.BWConfig;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ThrownPotion.class)
public abstract class PotionEntityMixin extends ThrowableItemProjectile {
	public PotionEntityMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "applySplash", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void applySplashPotion(List<MobEffectInstance> statusEffects, @Nullable Entity entity, CallbackInfo callbackInfo, AABB box) {
		if (BWConfig.disabledPoppets.contains("bewitchment:voodoo_poppet")) {
			return;
		}
		List<ItemEntity> itemEntities = level().getEntitiesOfClass(ItemEntity.class, box);
		for (ItemEntity itemEntity : itemEntities) {
			if (itemEntity.getItem().getItem() == BWObjects.VOODOO_POPPET) {
				LivingEntity owner = BewitchmentAPI.getTaglockOwner(level(), itemEntity.getItem());
				if (owner != null && owner.isAffectedByPotions()) {
					for (MobEffectInstance effect : statusEffects) {
						if (itemEntity.getItem().hurt(8, random, null) && itemEntity.getItem().getDamageValue() >= itemEntity.getItem().getMaxDamage()) {
							itemEntity.getItem().shrink(1);
						}
						if (!BewitchmentAPI.hasVoodooProtection(owner, 8)) {
							if (effect.getEffect().isInstantenous()) {
								effect.getEffect().applyInstantenousEffect(null, null, owner, effect.getAmplifier(), 0.5);
							} else {
								owner.addEffect(new MobEffectInstance(effect.getEffect(), effect.getDuration() / 2, effect.getAmplifier(), effect.isAmbient(), effect.isVisible()));
							}
						}
					}
				}
			}
		}
	}
}
