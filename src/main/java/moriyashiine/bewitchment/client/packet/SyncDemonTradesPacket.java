/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.client.screen.DemonScreenHandler;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.DemonMerchant;
import moriyashiine.bewitchment.common.entity.living.DemonEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import java.util.List;

public class SyncDemonTradesPacket {
	public static final ResourceLocation ID = Bewitchment.id("sync_demon_trades");

	public static void send(ServerPlayer player, DemonMerchant merchant, int syncId) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(syncId);
		DemonEntity.DemonTradeOffer.toPacket(merchant.getOffers(), buf);
		buf.writeInt(merchant.getDemonTrader().getId());
		buf.writeBoolean(merchant.isDiscount());
		ServerPlayNetworking.send(player, ID, buf);
	}

	public static class Receiver implements ClientPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
			int syncId = buf.readInt();
			List<DemonEntity.DemonTradeOffer> offers = DemonEntity.DemonTradeOffer.fromPacket(buf);
			int traderId = buf.readInt();
			boolean discount = buf.readBoolean();
			client.execute(() -> {
				if (client.player != null) {
					AbstractContainerMenu screenHandler = client.player.containerMenu;
					if (syncId == screenHandler.containerId && screenHandler instanceof DemonScreenHandler) {
						((DemonScreenHandler) screenHandler).demonMerchant.setCurrentCustomer(client.player);
						((DemonScreenHandler) screenHandler).demonMerchant.setOffersClientside(offers);
						((DemonScreenHandler) screenHandler).demonMerchant.setDemonTraderClientside((LivingEntity) client.level.getEntity(traderId));
						((DemonScreenHandler) screenHandler).demonMerchant.setDiscountClientside(discount);
					}
				}
			});
		}
	}
}
