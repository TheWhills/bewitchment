/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.familiar;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	@Unique
	private static final AttributeModifier WOLF_FAMILIAR_ARMOR_MODIFIER = new AttributeModifier(UUID.fromString("1b2866e6-ca04-43e4-b643-1142c0791e6d"), "Familiar modifier", 6, AttributeModifier.Operation.ADDITION);
	@Unique
	private static final AttributeModifier WOLF_FAMILIAR_ARMOR_TOUGHNESS_MODIFIER = new AttributeModifier(UUID.fromString("ec7f7a2e-d5c5-40c4-9338-c2808946f7c4"), "Familiar modifier", 6, AttributeModifier.Operation.ADDITION);

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo callbackInfo) {
		if (!level().isClientSide) {
			boolean shouldHave = BewitchmentAPI.getFamiliar((Player) (Object) this) == EntityType.WOLF;
			AttributeInstance armorAttribute = getAttribute(Attributes.ARMOR);
			AttributeInstance armorToughnessAttribute = getAttribute(Attributes.ARMOR_TOUGHNESS);
			if (shouldHave && !armorAttribute.hasModifier(WOLF_FAMILIAR_ARMOR_MODIFIER)) {
				armorAttribute.addPermanentModifier(WOLF_FAMILIAR_ARMOR_MODIFIER);
				armorToughnessAttribute.addPermanentModifier(WOLF_FAMILIAR_ARMOR_TOUGHNESS_MODIFIER);
			} else if (!shouldHave && armorAttribute.hasModifier(WOLF_FAMILIAR_ARMOR_MODIFIER)) {
				armorAttribute.removeModifier(WOLF_FAMILIAR_ARMOR_MODIFIER);
				armorToughnessAttribute.removeModifier(WOLF_FAMILIAR_ARMOR_TOUGHNESS_MODIFIER);
			}
		}
	}
}
