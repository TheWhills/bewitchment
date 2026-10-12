/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Server to client: the full sync data of one auto-synced component on one entity. */
public record SyncComponentPacket(int entityId, ResourceLocation componentId, CompoundTag data) {
	public void encode(FriendlyByteBuf buf) {
		buf.writeVarInt(entityId);
		buf.writeResourceLocation(componentId);
		buf.writeNbt(data);
	}

	public static SyncComponentPacket decode(FriendlyByteBuf buf) {
		int entityId = buf.readVarInt();
		ResourceLocation componentId = buf.readResourceLocation();
		CompoundTag data = buf.readNbt();
		return new SyncComponentPacket(entityId, componentId, data == null ? new CompoundTag() : data);
	}

	public void handle(Supplier<NetworkEvent.Context> context) {
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlers.syncComponent(this));
	}
}
