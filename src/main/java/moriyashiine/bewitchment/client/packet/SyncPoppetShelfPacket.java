/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.PoppetShelfBlockEntity;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;

public class SyncPoppetShelfPacket {
	public static final ResourceLocation ID = Bewitchment.id("sync_poppet_shelf");

	public static void send(ServerPlayer player, BlockPos pos) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeLong(pos.asLong());
		CompoundTag nbt = new CompoundTag();
		NonNullList<ItemStack> inventory = BWWorldState.get(player.level()).poppetShelves.get(pos.asLong());
		if (inventory != null) {
			ContainerHelper.saveAllItems(nbt, inventory);
		}
		buf.writeNbt(nbt);
		ServerPlayNetworking.send(player, ID, buf);
	}

	@SuppressWarnings({"ConstantConditions", "Convert2Lambda"})
	public static class Receiver implements ClientPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
			BlockPos pos = BlockPos.of(buf.readLong());
			NonNullList<ItemStack> inventory = NonNullList.withSize(9, ItemStack.EMPTY);
			CompoundTag nbt = buf.readNbt();
			if (!nbt.isEmpty()) {
				ContainerHelper.loadAllItems(nbt, inventory);
			}
			client.execute(new Runnable() {
				@Override
				public void run() {
					if (client.level.getBlockEntity(pos) instanceof PoppetShelfBlockEntity poppetShelfBlockEntity) {
						poppetShelfBlockEntity.clientInventory = inventory;
					}
				}
			});
		}
	}
}
