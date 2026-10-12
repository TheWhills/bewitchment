/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.screen;

import moriyashiine.bewitchment.client.packet.SyncContractsPacket;
import moriyashiine.bewitchment.client.packet.SyncDemonTradesPacket;
import moriyashiine.bewitchment.common.entity.DemonMerchant;
import moriyashiine.bewitchment.common.entity.living.DemonEntity;
import moriyashiine.bewitchment.common.registry.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DemonScreenHandler extends AbstractContainerMenu {
	public final DemonMerchant demonMerchant;
	public final Container demonInventory = new SimpleContainer(getOfferCount());

	public DemonScreenHandler(int syncId, DemonMerchant merchant) {
		this(BWScreenHandlerTypes.DEMON_SCREEN_HANDLER, syncId, merchant);
	}

	protected DemonScreenHandler(MenuType<?> type, int syncId, DemonMerchant merchant) {
		super(type, syncId);
		this.demonMerchant = merchant;
		addSlot(new DemonTradeSlot(demonInventory, 0, 41, 96));
		addSlot(new DemonTradeSlot(demonInventory, 1, 80, 104));
		addSlot(new DemonTradeSlot(demonInventory, 2, 119, 96));
	}

	public int getOfferCount() {
		return 3;
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		demonMerchant.setCurrentCustomer(null);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void clicked(int slotIndex, int button, ClickType actionType, Player player) {
		if (slotIndex == -999) {
			return;
		}
		Slot slot = slots.get(slotIndex);
		if (slot instanceof DemonTradeSlot tradeSlot) {
			DemonEntity.DemonTradeOffer offer = tradeSlot.getOffer();
			BWComponents.CONTRACTS_COMPONENT.maybeGet(player).ifPresent(contractsComponent -> {
				if (!contractsComponent.hasContract(offer.getContract())) {
					demonMerchant.onSell(offer);
					demonMerchant.trade(offer);
					if (player instanceof ServerPlayer serverPlayer) {
						SyncContractsPacket.send(serverPlayer);
						SyncDemonTradesPacket.send(serverPlayer, demonMerchant, containerId);
						if (player.getMaxHealth() - offer.getCost(demonMerchant) <= 0) {
							contractsComponent.getContracts().clear();
							player.hurt(BWDamageSources.create(demonMerchant.getDemonTrader().level(), BWDamageSources.DEATH), Float.MAX_VALUE);
						}
					}
				}
			});
			return;
		}
		super.clicked(slotIndex, button, actionType, player);
	}

	@Override
	public void slotsChanged(Container inventory) {
		super.slotsChanged(inventory);
	}

	@Override
	public boolean stillValid(Player player) {
		return !player.isSpectator();
	}

	@SuppressWarnings("ConstantConditions")
	public class DemonTradeSlot extends Slot {
		private final int contractIndex;

		public DemonTradeSlot(Container inventory, int index, int x, int y) {
			super(inventory, index, x, y);
			contractIndex = index;
		}

		@Override
		public boolean isActive() {
			return getOffer() != null && super.isActive();
		}

		@Override
		public ItemStack getItem() {
			DemonEntity.DemonTradeOffer offer = getOffer();
			if (offer != null) {
				ItemStack contract = new ItemStack(BWObjects.DEMONIC_CONTRACT);
				contract.getOrCreateTag().putString("Contract", BWRegistries.CONTRACT.getKey(getOffer().getContract()).toString());
				contract.getOrCreateTag().putInt("Duration", getOffer().getDuration());
				return contract;
			}
			return ItemStack.EMPTY;
		}

		public DemonEntity.DemonTradeOffer getOffer() {
			return demonMerchant.getOffers().size() > contractIndex ? demonMerchant.getOffers().get(contractIndex) : null;
		}
	}

	public static class DemonMerchantImpl implements DemonMerchant {
		private List<DemonEntity.DemonTradeOffer> offers = new ArrayList<>();
		private Player costumer;
		private LivingEntity trader;
		private boolean discount;

		@Override
		public List<DemonEntity.DemonTradeOffer> getOffers() {
			return offers;
		}

		@Override
		public void trade(DemonEntity.DemonTradeOffer offer) {
		}

		@Override
		public LivingEntity getDemonTrader() {
			return trader;
		}

		@Override
		public void onSell(DemonEntity.DemonTradeOffer offer) {
		}

		@Override
		public void setCurrentCustomer(Player customer) {
			costumer = customer;
		}

		@Override
		public @Nullable Player getCurrentCustomer() {
			return costumer;
		}

		@Override
		public boolean isDiscount() {
			return discount;
		}

		@Override
		public void setOffersClientside(List<DemonEntity.DemonTradeOffer> offers) {
			this.offers = offers;
		}

		@Override
		public void setDemonTraderClientside(LivingEntity trader) {
			this.trader = trader;
		}

		@Override
		public void setDiscountClientside(boolean discount) {
			this.discount = discount;
		}
	}
}
