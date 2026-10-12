/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.common.misc.BWUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Villager.class)
public abstract class VillagerEntityMixin extends AbstractVillager {
	@Shadow
	protected abstract void sayNo();

	public VillagerEntityMixin(EntityType<? extends AbstractVillager> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "interactMob", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/entity/passive/VillagerEntity;getOffers()Lnet/minecraft/village/TradeOfferList;"), cancellable = true)
	private void interactMob(Player player, InteractionHand hand, CallbackInfoReturnable<MerchantOffers> callbackInfo) {
		if (!level().isClientSide && BWUtil.rejectTrades(this)) {
			sayNo();
			callbackInfo.setReturnValue(BWUtil.EMPTY_TRADES);
		}
	}
}
