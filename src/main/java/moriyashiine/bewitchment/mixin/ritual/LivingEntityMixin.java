/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.ritual;

import moriyashiine.bewitchment.common.block.entity.GlyphBlockEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWRitualFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@ModifyVariable(method = "getDamageAfterArmorAbsorb", at = @At("HEAD"), argsOnly = true)
	private float modifyDamage(float amount, DamageSource source) {
		if (!level().isClientSide && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && level().getWorldBorder().isWithinBounds(blockPosition())) {
			for (BlockPos foundPos : BWUtil.getBlockPoses(blockPosition(), 16)) {
				if (level().getWorldBorder().isWithinBounds(foundPos) && level().getBlockEntity(foundPos) instanceof GlyphBlockEntity glyph && glyph.ritualFunction == BWRitualFunctions.PREVENT_DAMAGE) {
					return 0;
				}
			}
		}
		return amount;
	}
}
