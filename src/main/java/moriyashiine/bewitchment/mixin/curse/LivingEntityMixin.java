/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.curse;

import moriyashiine.bewitchment.api.component.CursesComponent;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWCurses;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@ModifyVariable(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"), argsOnly = true)
	private MobEffectInstance modifyStatusEffect(MobEffectInstance effect) {
		if (!level().isClientSide && !effect.isAmbient() && effect.getEffect().getCategory() == MobEffectCategory.HARMFUL && BWComponents.CURSES_COMPONENT.get(this).hasCurse(BWCurses.COMPROMISED)) {
			return new MobEffectInstance(effect.getEffect(), effect.getDuration(), effect.getAmplifier() + 1, false, effect.isVisible(), effect.showIcon());
		}
		return effect;
	}

	@ModifyVariable(method = "applyArmorToDamage", at = @At("HEAD"), argsOnly = true)
	private float modifyDamage(float amount, DamageSource source) {
		if (!level().isClientSide) {
			CursesComponent cursesComponent = BWComponents.CURSES_COMPONENT.get(this);
			if (cursesComponent.hasCurse(BWCurses.FORESTS_WRATH) && (source.is(DamageTypeTags.IS_FIRE) || ((source.getDirectEntity() instanceof LivingEntity livingSource && livingSource.getMainHandItem().getItem() instanceof AxeItem)))) {
				amount *= 2;
			}
			if (cursesComponent.hasCurse(BWCurses.SUSCEPTIBILITY) && source.is(DamageTypeTags.WITCH_RESISTANT_TO)) {
				amount *= 2;
			}
		}
		return amount;
	}

	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!level().isClientSide && (Object) this instanceof Mob mob && BWComponents.FAKE_MOB_COMPONENT.get(mob).getTarget() != null) {
			callbackInfo.setReturnValue(false);
		}
	}

	@Inject(method = "canHaveStatusEffect", at = @At("RETURN"), cancellable = true)
	private void canHaveStatusEffect(MobEffectInstance effect, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValueZ() && !level().isClientSide && !effect.isAmbient() && effect.getEffect().getCategory() == MobEffectCategory.BENEFICIAL && BWComponents.CURSES_COMPONENT.get(this).hasCurse(BWCurses.UNLUCKY) && random.nextBoolean()) {
			callbackInfo.setReturnValue(false);
		}
	}
}
