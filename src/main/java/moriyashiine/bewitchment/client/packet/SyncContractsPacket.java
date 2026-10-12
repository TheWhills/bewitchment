/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.packet;

import moriyashiine.bewitchment.common.packet.BWNetwork;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Server to client: the player's full contract list. */
public record SyncContractsPacket(CompoundTag contracts) {
	public static void send(ServerPlayer player) {
		CompoundTag contracts = new CompoundTag();
		contracts.put("Contracts", BWComponents.CONTRACTS_COMPONENT.get(player).toNbtContract());
		BWNetwork.sendToPlayer(player, new SyncContractsPacket(contracts));
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeNbt(contracts);
	}

	public static SyncContractsPacket decode(FriendlyByteBuf buf) {
		CompoundTag tag = buf.readNbt();
		return new SyncContractsPacket(tag == null ? new CompoundTag() : tag);
	}

	public void handle(Supplier<NetworkEvent.Context> context) {
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlers.syncContracts(this));
	}
}
