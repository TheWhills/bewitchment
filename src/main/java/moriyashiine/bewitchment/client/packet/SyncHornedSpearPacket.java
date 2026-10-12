/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.packet;

import moriyashiine.bewitchment.common.entity.projectile.HornedSpearEntity;
import moriyashiine.bewitchment.common.packet.BWNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Server to client: the item stack a thrown horned spear is carrying. */
public record SyncHornedSpearPacket(int entityId, ItemStack spear) {
	public SyncHornedSpearPacket(HornedSpearEntity entity) {
		this(entity.getId(), entity.spear);
	}

	public static void send(ServerPlayer player, HornedSpearEntity entity) {
		BWNetwork.sendToPlayer(player, new SyncHornedSpearPacket(entity));
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(entityId);
		buf.writeItem(spear);
	}

	public static SyncHornedSpearPacket decode(FriendlyByteBuf buf) {
		return new SyncHornedSpearPacket(buf.readInt(), buf.readItem());
	}

	public void handle(Supplier<NetworkEvent.Context> context) {
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlers.syncHornedSpear(this));
	}
}
