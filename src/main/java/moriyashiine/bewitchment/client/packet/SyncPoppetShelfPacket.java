/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.packet;

import moriyashiine.bewitchment.common.packet.BWNetwork;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Server to client: contents of a poppet shelf (stored in world state, not the block entity). */
public record SyncPoppetShelfPacket(BlockPos pos, CompoundTag inventory) {
	public static SyncPoppetShelfPacket of(Level level, BlockPos pos) {
		CompoundTag nbt = new CompoundTag();
		NonNullList<ItemStack> inventory = BWWorldState.get(level).poppetShelves.get(pos.asLong());
		if (inventory != null) {
			ContainerHelper.saveAllItems(nbt, inventory);
		}
		return new SyncPoppetShelfPacket(pos, nbt);
	}

	public static void send(ServerPlayer player, BlockPos pos) {
		BWNetwork.sendToPlayer(player, of(player.level(), pos));
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeNbt(inventory);
	}

	public static SyncPoppetShelfPacket decode(FriendlyByteBuf buf) {
		BlockPos pos = buf.readBlockPos();
		CompoundTag tag = buf.readNbt();
		return new SyncPoppetShelfPacket(pos, tag == null ? new CompoundTag() : tag);
	}

	public void handle(Supplier<NetworkEvent.Context> context) {
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlers.syncPoppetShelf(this));
	}
}
