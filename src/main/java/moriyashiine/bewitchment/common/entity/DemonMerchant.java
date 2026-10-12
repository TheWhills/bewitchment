/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.entity.living.BaphometEntity;
import moriyashiine.bewitchment.common.entity.living.DemonEntity;
import moriyashiine.bewitchment.common.registry.BWPledges;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface DemonMerchant {
	List<DemonEntity.DemonTradeOffer> getOffers();

	LivingEntity getDemonTrader();

	default void trade(DemonEntity.DemonTradeOffer offer) {
		if (!getDemonTrader().level().isClientSide) {
			offer.apply(this);
		}
	}

	void onSell(DemonEntity.DemonTradeOffer offer);

	void setCurrentCustomer(Player customer);

	@Nullable Player getCurrentCustomer();

	default boolean isDiscount() {
		return this instanceof BaphometEntity && getCurrentCustomer() != null && BewitchmentAPI.isPledged(getCurrentCustomer(), BWPledges.BAPHOMET);
	}

	default void setOffersClientside(List<DemonEntity.DemonTradeOffer> offers) {
	}

	default void setDemonTraderClientside(LivingEntity trader) {
	}

	default void setDiscountClientside(boolean discount) {
	}
}
