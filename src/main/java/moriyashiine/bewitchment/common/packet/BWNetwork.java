/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.packet;

import moriyashiine.bewitchment.client.packet.*;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

/**
 * Bewitchment's network channel. Replaces Fabric's per-packet global receivers.
 * Every message is registered with a fixed direction, so a client can never make
 * the server run a client-bound handler or the reverse.
 */
public final class BWNetwork {
	/** Bump when any packet's wire format changes; mismatched clients are refused at login. */
	private static final String PROTOCOL_VERSION = "1";

	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(Bewitchment.id("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

	private BWNetwork() {
	}

	public static void register() {
		int id = 0;
		// client -> server
		CHANNEL.messageBuilder(CauldronTeleportPacket.class, id++, NetworkDirection.PLAY_TO_SERVER).encoder(CauldronTeleportPacket::encode).decoder(CauldronTeleportPacket::decode).consumerMainThread(CauldronTeleportPacket::handle).add();
		CHANNEL.messageBuilder(TransformationAbilityPacket.class, id++, NetworkDirection.PLAY_TO_SERVER).encoder(TransformationAbilityPacket::encode).decoder(TransformationAbilityPacket::decode).consumerMainThread(TransformationAbilityPacket::handle).add();
		CHANNEL.messageBuilder(TogglePressingForwardPacket.class, id++, NetworkDirection.PLAY_TO_SERVER).encoder(TogglePressingForwardPacket::encode).decoder(TogglePressingForwardPacket::decode).consumerMainThread(TogglePressingForwardPacket::handle).add();
		// server -> client
		CHANNEL.messageBuilder(SpawnExplosionParticlesPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT).encoder(SpawnExplosionParticlesPacket::encode).decoder(SpawnExplosionParticlesPacket::decode).consumerMainThread(SpawnExplosionParticlesPacket::handle).add();
		CHANNEL.messageBuilder(SpawnPortalParticlesPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT).encoder(SpawnPortalParticlesPacket::encode).decoder(SpawnPortalParticlesPacket::decode).consumerMainThread(SpawnPortalParticlesPacket::handle).add();
		CHANNEL.messageBuilder(SpawnSmokeParticlesPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT).encoder(SpawnSmokeParticlesPacket::encode).decoder(SpawnSmokeParticlesPacket::decode).consumerMainThread(SpawnSmokeParticlesPacket::handle).add();
		CHANNEL.messageBuilder(SpawnSpecterBangleParticlesPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT).encoder(SpawnSpecterBangleParticlesPacket::encode).decoder(SpawnSpecterBangleParticlesPacket::decode).consumerMainThread(SpawnSpecterBangleParticlesPacket::handle).add();
		CHANNEL.messageBuilder(SyncContractsPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT).encoder(SyncContractsPacket::encode).decoder(SyncContractsPacket::decode).consumerMainThread(SyncContractsPacket::handle).add();
		CHANNEL.messageBuilder(SyncDemonTradesPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT).encoder(SyncDemonTradesPacket::encode).decoder(SyncDemonTradesPacket::decode).consumerMainThread(SyncDemonTradesPacket::handle).add();
		CHANNEL.messageBuilder(SyncHornedSpearPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT).encoder(SyncHornedSpearPacket::encode).decoder(SyncHornedSpearPacket::decode).consumerMainThread(SyncHornedSpearPacket::handle).add();
		CHANNEL.messageBuilder(SyncPoppetShelfPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT).encoder(SyncPoppetShelfPacket::encode).decoder(SyncPoppetShelfPacket::decode).consumerMainThread(SyncPoppetShelfPacket::handle).add();
	}

	public static void sendToServer(Object message) {
		CHANNEL.sendToServer(message);
	}

	public static void sendToPlayer(ServerPlayer player, Object message) {
		CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
	}

	/** Every player tracking {@code entity}, not including the entity itself (same set as Fabric's PlayerLookup.tracking). */
	public static void sendToTracking(Entity entity, Object message) {
		CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
	}

	/** Every player tracking {@code entity}, plus the entity itself if it is a player. */
	public static void sendToTrackingAndSelf(Entity entity, Object message) {
		CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
	}

	/** Every player tracking the chunk containing {@code blockEntity}. */
	public static void sendToTracking(BlockEntity blockEntity, Object message) {
		Optional.ofNullable(blockEntity.getLevel()).ifPresent(level -> CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(blockEntity.getBlockPos())), message));
	}
}
