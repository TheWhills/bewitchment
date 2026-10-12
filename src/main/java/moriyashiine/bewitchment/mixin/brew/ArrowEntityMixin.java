/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.brew;

import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("ConstantConditions")
@Mixin(Arrow.class)
public abstract class ArrowEntityMixin extends Entity {
	public ArrowEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@ModifyVariable(method = "setEffectsFromItem", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/alchemy/PotionUtils;getCustomEffects(Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;"))
	private Collection<MobEffectInstance> modifyGetCustomPotionEffects(Collection<MobEffectInstance> collection, ItemStack stack) {
		if (stack.getTag().getBoolean("BewitchmentBrew")) {
			List<MobEffectInstance> statusEffects = new ArrayList<>(collection);
			for (int i = collection.size() - 1; i >= 0; i--) {
				statusEffects.set(i, new MobEffectInstance(statusEffects.get(i).getEffect(), statusEffects.get(i).getDuration() / 8, statusEffects.get(i).getAmplifier(), statusEffects.get(i).isAmbient(), statusEffects.get(i).isVisible(), statusEffects.get(i).showIcon()));
			}
			return statusEffects;
		}
		return collection;
	}

	@Inject(method = "getPickupItem", at = @At(value = "RETURN", ordinal = 1), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void asItemStack(CallbackInfoReturnable<ItemStack> callbackInfo, ItemStack stack) {
		BWComponents.POLYMORPH_COMPONENT.maybeGet(this).ifPresent(polymorphComponent -> {
			if (polymorphComponent.getUuid() != null) {
				stack.getOrCreateTag().putUUID("PolymorphUUID", polymorphComponent.getUuid());
				stack.getOrCreateTag().putString("PolymorphName", polymorphComponent.getName());
			}
		});
	}

	@Inject(method = "doPostHurtEffects", at = @At("HEAD"))
	private void onHit(LivingEntity target, CallbackInfo callbackInfo) {
		BWComponents.POLYMORPH_COMPONENT.maybeGet(this).ifPresent(thisPolymorphComponent -> {
			if (thisPolymorphComponent.getUuid() != null) {
				BWComponents.POLYMORPH_COMPONENT.maybeGet(target).ifPresent(targetPolymorphComponent -> {
					targetPolymorphComponent.setUuid(thisPolymorphComponent.getUuid());
					targetPolymorphComponent.setName(thisPolymorphComponent.getName());
				});
			}
		});
	}

	@SuppressWarnings("ConstantConditions")
	@Inject(method = "setEffectsFromItem", at = @At("TAIL"))
	private void initFromStack(ItemStack stack, CallbackInfo callbackInfo) {
		BWComponents.POLYMORPH_COMPONENT.maybeGet(this).ifPresent(polymorphComponent -> {
			if (stack.hasTag() && stack.getTag().contains("PolymorphUUID")) {
				polymorphComponent.setUuid(stack.getTag().getUUID("PolymorphUUID"));
				polymorphComponent.setName(stack.getTag().getString("PolymorphName"));
			}
		});
	}
}
