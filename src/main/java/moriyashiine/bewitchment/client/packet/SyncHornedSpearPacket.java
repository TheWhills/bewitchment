/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.projectile.HornedSpearEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class SyncHornedSpearPacket {
	public static final ResourceLocation ID = Bewitchment.id("sync_horned_spear");

	public static void send(ServerPlayer player, HornedSpearEntity entity) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(entity.getId());
		buf.writeItem(entity.spear);
		ServerPlayNetworking.send(player, ID, buf);
	}

	@SuppressWarnings("ConstantConditions")
	public static class Receiver implements ClientPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
			int entityId = buf.readInt();
			ItemStack spear = buf.readItem();
			client.execute(() -> ((HornedSpearEntity) client.level.getEntity(entityId)).spear = spear);
		}
	}
}
