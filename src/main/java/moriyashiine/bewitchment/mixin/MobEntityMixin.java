/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.entity.living.GhostEntity;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobEntityMixin extends LivingEntity {
	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@ModifyVariable(method = "setTarget", at = @At("HEAD"), argsOnly = true)
	private LivingEntity modifyTarget(LivingEntity target) {
		if (!level().isClientSide && target != null) {
			if (target instanceof GhostEntity) {
				return null;
			}
			if (target instanceof Mob mob && getUUID().equals(BWComponents.MINION_COMPONENT.get(mob).getMaster())) {
				return null;
			}
			if (isInvertedHealAndHarm()) {
				for (BlockPos foundPos : BWUtil.getBlockPoses(target.blockPosition(), 2)) {
					if (level().getWorldBorder().isWithinBounds(foundPos) && level().getBlockState(foundPos).is(BWTags.UNDEAD_MASK)) {
						return null;
					}
				}
			}
		}
		return target;
	}

	@Inject(method = "checkAndHandleImportantInteractions", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/player/Player;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"), cancellable = true)
	private void interactWithItem(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		if (player.getItemInHand(hand).getItem() instanceof TaglockItem) {
			cir.setReturnValue(TaglockItem.useTaglock(player, this, hand, true, false));
		}
	}
}
