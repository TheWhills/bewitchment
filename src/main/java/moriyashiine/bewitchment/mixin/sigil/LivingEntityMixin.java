/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.sigil;

import moriyashiine.bewitchment.common.block.entity.interfaces.SigilHolder;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWSigils;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
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

	@ModifyVariable(method = "getDamageAfterArmorAbsorb", at = @At("HEAD"), argsOnly = true)
	private float modifyDamage(float amount, DamageSource source) {
		if (!level().isClientSide && source.is(DamageTypeTags.IS_FALL)) {
			BWWorldState worldState = BWWorldState.get(level());
			BlockPos sigilPos = BWUtil.getClosestBlockPos(blockPosition(), 16, currentPos -> worldState.potentialSigils.contains(currentPos.asLong()) && level().getBlockEntity(currentPos) instanceof SigilHolder sigilHolder && sigilHolder.getSigil() == BWSigils.HEAVY.get());
			if (sigilPos != null) {
				BlockEntity blockEntity = level().getBlockEntity(sigilPos);
				SigilHolder sigilHolder = (SigilHolder) blockEntity;
				if (sigilHolder.test(this)) {
					sigilHolder.setUses(sigilHolder.getUses() - 1);
					blockEntity.setChanged();
					amount *= 3;
				}
			}
		}
		return amount;
	}

	@Inject(method = "canBeAffected", at = @At("RETURN"), cancellable = true)
	private void canHaveStatusEffect(MobEffectInstance effect, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (callbackInfo.getReturnValueZ() && !level().isClientSide && !effect.isAmbient() && effect.getEffect().getCategory() != MobEffectCategory.HARMFUL) {
			BWWorldState worldState = BWWorldState.get(level());
			BlockPos sigilPos = BWUtil.getClosestBlockPos(blockPosition(), 16, currentPos -> worldState.potentialSigils.contains(currentPos.asLong()) && level().getBlockEntity(currentPos) instanceof SigilHolder sigilHolder && sigilHolder.getSigil() == BWSigils.RUIN.get());
			if (sigilPos != null) {
				BlockEntity blockEntity = level().getBlockEntity(sigilPos);
				SigilHolder sigilHolder = (SigilHolder) blockEntity;
				if (sigilHolder.test(this)) {
					sigilHolder.setUses(sigilHolder.getUses() - 1);
					blockEntity.setChanged();
					callbackInfo.setReturnValue(false);
				}
			}
		}
	}
}
