/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.component.BloodComponent;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class BWDamageSources {
	public static final ResourceKey<DamageType> MAGIC_COPY = ResourceKey.create(Registries.DAMAGE_TYPE, Bewitchment.id("magic_copy"));
	public static final ResourceKey<DamageType> WEDNESDAY = ResourceKey.create(Registries.DAMAGE_TYPE, Bewitchment.id("wednesday"));
	public static final ResourceKey<DamageType> DEATH = ResourceKey.create(Registries.DAMAGE_TYPE, Bewitchment.id("death"));
	public static final ResourceKey<DamageType> VAMPIRE = ResourceKey.create(Registries.DAMAGE_TYPE, Bewitchment.id("vampire"));
	public static final ResourceKey<DamageType> SUN = ResourceKey.create(Registries.DAMAGE_TYPE, Bewitchment.id("sun"));

	public static DamageSource create(Level world, ResourceKey<DamageType> key, @Nullable Entity source, @Nullable Entity attacker) {
		return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key), source, attacker);
	}

	public static DamageSource create(Level world, ResourceKey<DamageType> key, @Nullable Entity attacker) {
		return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key), attacker);
	}

	public static DamageSource create(Level world, ResourceKey<DamageType> key) {
		return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key));
	}

	public static float handleDamage(LivingEntity entity, DamageSource source, float amount) {
		if (BewitchmentAPI.isWeakToSilver(entity) && BewitchmentAPI.isSourceFromSilver(source)) {
			amount += 4;
		} else if (BewitchmentAPI.isWerewolf(entity, false) && !isEffective(source, false)) {
			amount *= 2 / 3f;
		} else if (BewitchmentAPI.isVampire(entity, true)) {
			amount = handleVampireDamage(entity, source, amount);
		}
		return amount;
	}

	public static boolean isEffective(DamageSource source, boolean vampire) {
		if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || (vampire && (source.is(MAGIC_COPY) || source.is(SUN)))) {
			return true;
		}
		Entity attacker = source.getDirectEntity();
		if (attacker != null) {
			if (attacker.getType().is(BWTags.TAGLOCK_BLACKLIST) || BewitchmentAPI.isVampire(attacker, true) || BewitchmentAPI.isWerewolf(attacker, true)) {
				return true;
			} else if (attacker instanceof LivingEntity && EnchantmentHelper.getEnchantmentLevel(Enchantments.SMITE, (LivingEntity) attacker) > 0) {
				return true;
			}
		}
		return BewitchmentAPI.isSourceFromSilver(source);
	}

	private static float handleVampireDamage(LivingEntity entity, DamageSource source, float amount) {
		if (!isEffective(source, true)) {
			if (entity.getHealth() - amount < 1) {
				BloodComponent bloodComponent = BWComponents.BLOOD_COMPONENT.get(entity);
				while (entity.getHealth() - amount <= 0 && bloodComponent.getBlood() > 0) {
					amount--;
					bloodComponent.drainBlood(1, false);
				}
			}
		}
		return amount;
	}
}
