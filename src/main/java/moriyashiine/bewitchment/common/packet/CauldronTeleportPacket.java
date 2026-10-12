/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.packet;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.block.entity.UsesAltarPower;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.WitchAltarBlockEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWPledges;
import moriyashiine.bewitchment.common.world.BWWorldState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import java.util.Map;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

/** Client to server: teleport to the nearest witch cauldron named {@code message}. */
public record CauldronTeleportPacket(BlockPos cauldronPos, String message) {
	private static final double MAX_DISTANCE_SQ = 8 * 8;

	public static void send(BlockPos cauldronPos, String message) {
		BWNetwork.sendToServer(new CauldronTeleportPacket(cauldronPos, message));
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeBlockPos(cauldronPos);
		buf.writeUtf(message);
	}

	public static CauldronTeleportPacket decode(FriendlyByteBuf buf) {
		return new CauldronTeleportPacket(buf.readBlockPos(), buf.readUtf(Short.MAX_VALUE));
	}

	public void handle(Supplier<NetworkEvent.Context> context) {
		ServerPlayer player = context.get().getSender();
		if (player == null) {
			return;
		}
		Level world = player.level();
		// The position comes from the client: only accept a loaded altar-powered cauldron next to the player.
		if (!world.isLoaded(cauldronPos) || !(world.getBlockEntity(cauldronPos) instanceof UsesAltarPower usesAltarPower) || player.distanceToSqr(Vec3.atCenterOf(cauldronPos)) > MAX_DISTANCE_SQ) {
			return;
		}
		BlockPos closest = null;
		BWWorldState worldState = BWWorldState.get(world);
		for (Map.Entry<Long, String> entry : worldState.witchCauldrons.entrySet()) {
			if (message.equals(entry.getValue())) {
				BlockPos foundCauldronPos = BlockPos.of(entry.getKey());
				if (closest == null || foundCauldronPos.distToCenterSqr(player.position()) < closest.distToCenterSqr(player.position())) {
					closest = foundCauldronPos;
				}
			}
		}
		if (closest != null) {
			boolean hasPower = BewitchmentAPI.isPledged(player, BWPledges.LEONARD);
			if (!hasPower) {
				BlockPos altarPos = usesAltarPower.getAltarPos();
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
	}
}
