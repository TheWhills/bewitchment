/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.block.entity.UsesAltarPower;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWPledges;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.Map;

public class CauldronTeleportPacket {
	public static final ResourceLocation ID = Bewitchment.id("cauldron_teleport");

	public static void send(BlockPos cauldronPos, String message) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeBlockPos(cauldronPos);
		buf.writeUtf(message);
		ClientPlayNetworking.send(ID, buf);
	}

	@SuppressWarnings("ConstantConditions")
	public static class Receiver implements ServerPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
			BlockPos cauldronPos = buf.readBlockPos();
			String message = buf.readUtf(Short.MAX_VALUE);
			server.execute(() -> {
				Level world = player.level();
				BlockPos closest = null;
				BWWorldState worldState = BWWorldState.get(world);
				for (Map.Entry<Long, String> entry : worldState.witchCauldrons.entrySet()) {
					if (message.equals(entry.getValue())) {
						BlockPos foundCauldronPos = BlockPos.of(entry.getKey());
						if (closest == null || cauldronPos.distToCenterSqr(player.position()) < closest.distToCenterSqr(player.position())) {
							closest = foundCauldronPos;
						}
					}
				}
				if (closest != null) {
					boolean hasPower = BewitchmentAPI.isPledged(player, BWPledges.LEONARD);
					if (!hasPower) {
						BlockPos altarPos = ((UsesAltarPower) world.getBlockEntity(cauldronPos)).getAltarPos();
						if (altarPos != null) {
							BlockEntity altarBE = world.getBlockEntity(altarPos);
							if (altarBE instanceof WitchAltarBlockEntity altar && altar.drain((int) Math.sqrt(closest.distToCenterSqr(player.position())) / 2, false)) {
								hasPower = true;
							}
						}
					}
					if (hasPower) {
						BWUtil.teleport(player, closest.getX() + 0.5, closest.getY() - 0.4375, closest.getZ() + 0.5, true);
					} else {
						player.displayClientMessage(Component.translatable(Bewitchment.MOD_ID + ".message.insufficent_altar_power", message), true);
					}
				} else {
					player.displayClientMessage(Component.translatable(Bewitchment.MOD_ID + ".message.invalid_cauldron", message), true);
				}
			});
		}
	}
}
