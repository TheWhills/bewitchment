/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.api.registry.Contract;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class SyncContractsPacket {
	public static final ResourceLocation ID = Bewitchment.id("sync_contracts");

	public static void send(ServerPlayer player) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		CompoundTag contracts = new CompoundTag();
		contracts.put("Contracts", BWComponents.CONTRACTS_COMPONENT.get(player).toNbtContract());
		buf.writeNbt(contracts);
		ServerPlayNetworking.send(player, ID, buf);
	}

	@SuppressWarnings({"ConstantConditions", "Convert2Lambda"})
	public static class Receiver implements ClientPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
			CompoundTag contractsCompound = buf.readNbt();
			client.execute(new Runnable() {
				@Override
				public void run() {
					if (client.player != null) {
						BWComponents.CONTRACTS_COMPONENT.maybeGet(client.player).ifPresent(contractsComponent -> {
							contractsComponent.getContracts().clear();
							ListTag contractsList = contractsCompound.getList("Contracts", Tag.TAG_COMPOUND);
							for (int i = 0; i < contractsList.size(); i++) {
								CompoundTag contractCompound = contractsList.getCompound(i);
								contractsComponent.addContract(new Contract.Instance(BWRegistries.CONTRACT.get(new ResourceLocation(contractCompound.getString("Contract"))), contractCompound.getInt("Duration"), contractCompound.getInt("Cost")));
							}
						});
					}
				}
			});
		}
	}
}
