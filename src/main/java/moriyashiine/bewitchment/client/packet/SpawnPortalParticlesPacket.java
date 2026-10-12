/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.packet;

import moriyashiine.bewitchment.common.packet.BWNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Server to client: spawn portal particles around an entity. */
public record SpawnPortalParticlesPacket(int entityId) {
	public SpawnPortalParticlesPacket(Entity entity) {
		this(entity.getId());
	}

	public static void send(ServerPlayer player, Entity entity) {
		BWNetwork.sendToPlayer(player, new SpawnPortalParticlesPacket(entity));
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(entityId);
	}

	public static SpawnPortalParticlesPacket decode(FriendlyByteBuf buf) {
		return new SpawnPortalParticlesPacket(buf.readInt());
	}

	public void handle(Supplier<NetworkEvent.Context> context) {
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlers.spawnParticlesAround(this.entityId, ClientPacketHandlers.Particles.PORTAL));
	}
}
