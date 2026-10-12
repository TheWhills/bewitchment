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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(ThrownPotion.class)
public abstract class PotionEntityMixin extends ThrowableItemProjectile {
	public PotionEntityMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
		super(entityType, world);
	}

	// Redirects (call-site targets) instead of local-variable capture: production Minecraft has no
	// local variable table, so local-based injectors that work in dev fail to apply there.
	@Redirect(method = "makeAreaOfEffectCloud", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/AreaEffectCloud;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)V"))
	private void modifyCloudEffect(AreaEffectCloud cloud, MobEffectInstance effect, ItemStack stack, Potion potion) {
		if (stack.hasTag() && stack.getTag().getBoolean("BewitchmentBrew")) {
			effect = new MobEffectInstance(effect.getEffect(), effect.getDuration() / 4, effect.getAmplifier(), effect.isAmbient(), effect.isVisible(), effect.showIcon());
		}
		cloud.addEffect(effect);
	}

	@Inject(method = "applySplash", at = @At("HEAD"))
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

	@Redirect(method = "makeAreaOfEffectCloud", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/AreaEffectCloud;setPotion(Lnet/minecraft/world/item/alchemy/Potion;)V"))
	private void applyLingeringPotion(AreaEffectCloud cloud, Potion cloudPotion, ItemStack stack, Potion potion) {
		cloud.setPotion(cloudPotion);
		if (stack.hasTag() && stack.getTag().contains("PolymorphUUID")) {
			BWComponents.POLYMORPH_COMPONENT.maybeGet(cloud).ifPresent(polymorphComponent -> {
				polymorphComponent.setUuid(stack.getTag().getUUID("PolymorphUUID"));
				polymorphComponent.setName(stack.getTag().getString("PolymorphName"));
			});
		}
	}
}
