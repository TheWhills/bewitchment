/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.packet;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

/** Client to server: whether the rider of a broom is holding forward. */
public record TogglePressingForwardPacket(boolean pressingForward) {
	public static void send(boolean pressingForward) {
		BWNetwork.sendToServer(new TogglePressingForwardPacket(pressingForward));
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(pressingForward);
	}

	public static TogglePressingForwardPacket decode(FriendlyByteBuf buf) {
		return new TogglePressingForwardPacket(buf.readBoolean());
	}

	public void handle(Supplier<NetworkEvent.Context> context) {
		ServerPlayer player = context.get().getSender();
		if (player == null) {
			return;
		}
		if (pressingForward && BewitchmentAPI.getFamiliar(player) != BWEntityTypes.OWL.get()) {
			if (!BewitchmentAPI.drainMagic(player, 1, true)) {
				return;
			}
			if (player.tickCount % 60 == 0) {
				BewitchmentAPI.drainMagic(player, 1, false);
			}
		}
		BWComponents.BROOM_USER_COMPONENT.get(player).setPressingForward(pressingForward);
	}
}
