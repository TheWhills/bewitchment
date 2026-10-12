/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.packet;

import moriyashiine.bewitchment.common.entity.DemonMerchant;
import moriyashiine.bewitchment.common.entity.living.DemonEntity;
import moriyashiine.bewitchment.common.packet.BWNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

/** Server to client: demon trade offers for an open demon trading screen. */
public record SyncDemonTradesPacket(int syncId, List<DemonEntity.DemonTradeOffer> offers, int traderId, boolean discount) {
	public static void send(ServerPlayer player, DemonMerchant merchant, int syncId) {
		BWNetwork.sendToPlayer(player, new SyncDemonTradesPacket(syncId, merchant.getOffers(), merchant.getDemonTrader().getId(), merchant.isDiscount()));
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(syncId);
		DemonEntity.DemonTradeOffer.toPacket(offers, buf);
		buf.writeInt(traderId);
		buf.writeBoolean(discount);
	}

	public static SyncDemonTradesPacket decode(FriendlyByteBuf buf) {
		int syncId = buf.readInt();
		List<DemonEntity.DemonTradeOffer> offers = DemonEntity.DemonTradeOffer.fromPacket(buf);
		return new SyncDemonTradesPacket(syncId, offers, buf.readInt(), buf.readBoolean());
	}

	public void handle(Supplier<NetworkEvent.Context> context) {
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlers.syncDemonTrades(this));
	}
}
