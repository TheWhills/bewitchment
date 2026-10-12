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

/** Server to client: spawn smoke particles around an entity. */
public record SpawnSmokeParticlesPacket(int entityId) {
	public SpawnSmokeParticlesPacket(Entity entity) {
		this(entity.getId());
	}

	public static void send(ServerPlayer player, Entity entity) {
		BWNetwork.sendToPlayer(player, new SpawnSmokeParticlesPacket(entity));
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(entityId);
	}

	public static SpawnSmokeParticlesPacket decode(FriendlyByteBuf buf) {
		return new SpawnSmokeParticlesPacket(buf.readInt());
	}

	public void handle(Supplier<NetworkEvent.Context> context) {
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlers.spawnParticlesAround(this.entityId, ClientPacketHandlers.Particles.SMOKE));
	}
}
