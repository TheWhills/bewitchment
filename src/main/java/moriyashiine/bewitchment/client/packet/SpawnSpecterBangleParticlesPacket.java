/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class SpawnSpecterBangleParticlesPacket {
	public static final ResourceLocation ID = Bewitchment.id("spawn_specter_bangle_particles");

	public static void send(ServerPlayer player, Entity entity) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(entity.getId());
		ServerPlayNetworking.send(player, ID, buf);
	}

	public static class Receiver implements ClientPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
			int id = buf.readInt();
			client.execute(() -> {
				ClientLevel world = client.level;
				if (world != null) {
					Entity entity = world.getEntity(id);
					if (entity != null) {
						world.addParticle(ParticleTypes.SMOKE, entity.getRandomX(1), entity.getY(), entity.getRandomZ(1), 0, 0, 0);
					}
				}
			});
		}
	}
}
