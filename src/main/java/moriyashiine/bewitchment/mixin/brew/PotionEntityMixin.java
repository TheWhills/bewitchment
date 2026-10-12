/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.brew;

import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(ThrownPotion.class)
public abstract class PotionEntityMixin extends ThrowableItemProjectile {
	public PotionEntityMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
		super(entityType, world);
	}

	@ModifyVariable(method = "applyLingeringPotion", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;iterator()Ljava/util/Iterator;"))
	private Iterator<MobEffectInstance> modifyIterator(Iterator<MobEffectInstance> iterator, ItemStack stack) {
		if (stack.getTag().getBoolean("BewitchmentBrew")) {
			List<MobEffectInstance> statusEffects = new ArrayList<>();
			iterator.forEachRemaining(effect -> statusEffects.add(new MobEffectInstance(effect.getEffect(), effect.getDuration() / 4, effect.getAmplifier(), effect.isAmbient(), effect.isVisible(), effect.showIcon())));
			return statusEffects.iterator();
		}
		return iterator;
	}

	@Inject(method = "applySplashPotion", at = @At("HEAD"))
	private void applySplashPotion(List<MobEffectInstance> statusEffects, @Nullable Entity entity, CallbackInfo callbackInfo) {
		if (getItemRaw().hasTag() && getItemRaw().getTag().contains("PolymorphUUID")) {
			UUID uuid = getItemRaw().getTag().getUUID("PolymorphUUID");
			String name = getItemRaw().getTag().getString("PolymorphName");
			for (LivingEntity livingEntity : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(4, 2, 4))) {
				BWComponents.POLYMORPH_COMPONENT.maybeGet(livingEntity).ifPresent(polymorphComponent -> {
					polymorphComponent.setUuid(uuid);
					polymorphComponent.setName(name);
				});
			}
		}
	}

	@Inject(method = "applyLingeringPotion", at = @At(value = "INVOKE", target = "net/minecraft/entity/AreaEffectCloudEntity.setPotion(Lnet/minecraft/potion/Potion;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void applyLingeringPotion(ItemStack stack, Potion potion, CallbackInfo callbackInfo, AreaEffectCloud cloud) {
		if (stack.hasTag() && stack.getTag().contains("PolymorphUUID")) {
			BWComponents.POLYMORPH_COMPONENT.maybeGet(cloud).ifPresent(polymorphComponent -> {
				polymorphComponent.setUuid(stack.getTag().getUUID("PolymorphUUID"));
				polymorphComponent.setName(stack.getTag().getString("PolymorphName"));
			});
		}
	}
}
