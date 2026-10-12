/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.trinket;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@SuppressWarnings({"ConstantConditions", "OptionalGetWithoutIsPresent"})
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract boolean hasStatusEffect(MobEffect effect);

	@Shadow
	public abstract boolean addStatusEffect(MobEffectInstance effect);

	@Shadow
	public int hurtTime;

	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "hurt", at = @At("HEAD"))
	private void damageHead(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!level().isClientSide) {
			if (amount > 0 && hurtTime == 0) {
				if ((Object) this instanceof Player player && source.getDirectEntity() instanceof LivingEntity livingSource) {
					List<Tuple<SlotReference, ItemStack>> component = TrinketsApi.getTrinketComponent(player).get().getEquipped(BWObjects.PRICKLY_BELT);
					if (!component.isEmpty()) {
						ItemStack belt = component.get(0).getB();
						if (belt.hasTag() && belt.getTag().getInt("PotionUses") > 0) {
							boolean used = false;
							List<MobEffectInstance> effects = PotionUtils.getMobEffects(belt);
							for (MobEffectInstance effect : effects) {
								if (effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
									if (!livingSource.hasEffect(effect.getEffect()) && BewitchmentAPI.drainMagic(player, 2, true) && livingSource.addEffect(effect)) {
										used = true;
									}
								} else if (!hasStatusEffect(effect.getEffect()) && BewitchmentAPI.drainMagic(player, 2, true) && addStatusEffect(effect)) {
									if (belt.getTag().contains("PolymorphUUID")) {
										BWComponents.POLYMORPH_COMPONENT.maybeGet(this).ifPresent(polymorphComponent -> {
											polymorphComponent.setUuid(belt.getTag().getUUID("PolymorphUUID"));
											polymorphComponent.setName(belt.getTag().getString("PolymorphName"));
										});
									}
									used = true;
								}
							}
							if (used) {
								belt.getTag().putInt("PotionUses", belt.getTag().getInt("PotionUses") - 1);
								if (belt.getTag().getInt("PotionUses") <= 0) {
									belt.getOrCreateTag().put("CustomPotionEffects", new ListTag());
									if (belt.getTag().contains("PolymorphUUID")) {
										belt.getTag().remove("PolymorphUUID");
										belt.getTag().remove("PolymorphName");
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Inject(method = "hurt", at = @At("RETURN"))
	private void damageReturn(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValueZ() && !level().isClientSide && source.getDirectEntity() instanceof Player player && player.getMainHandItem().isEmpty() && TrinketsApi.getTrinketComponent(player).get().isEquipped(BWObjects.ZEPHYR_HARNESS) && BewitchmentAPI.drainMagic(player, 1, false)) {
			push(0, 2 / 3f, 0);
		}
	}
}
