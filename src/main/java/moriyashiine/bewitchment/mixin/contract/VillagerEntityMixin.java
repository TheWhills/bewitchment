/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.contract;

import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWContracts;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public abstract class VillagerEntityMixin extends AbstractVillager {
	public VillagerEntityMixin(EntityType<? extends AbstractVillager> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "die", at = @At("HEAD"))
	private void onDeath(CallbackInfo callbackInfo) {
		if (!level().isClientSide && lastHurtByPlayer != null) {
			if (BWComponents.CONTRACTS_COMPONENT.get(lastHurtByPlayer).hasContract(BWContracts.ENVY) && !getOffers().isEmpty()) {
				for (MerchantOffer offer : getOffers()) {
					if (!offer.isOutOfStock()) {
						Containers.dropItemStack(level(), getX() + 0.5, getY() + 0.5, getZ() + 0.5, offer.getResult());
					}
				}
			}
		}
	}
}
