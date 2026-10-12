package moriyashiine.bewitchment.client.event;

import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.common.packet.TogglePressingForwardPacket;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import net.minecraft.world.entity.player.Player;

/** Client: transformation ability key and broom forward input, checked at the end of each client level tick. */
public final class TransformationAbilityEvent {
	private static int transformationAbilityCooldown = 0;

	private TransformationAbilityEvent() {
	}

	public static void onLevelTick(TickEvent.LevelTickEvent event) {
		if (event.phase != TickEvent.Phase.END || !event.level.isClientSide) {
			return;
		}
		Player player = Minecraft.getInstance().player;
		if (player != null) {
			if (transformationAbilityCooldown > 0) {
				transformationAbilityCooldown--;
			} else if (BewitchmentClient.TRANSFORMATION_ABILITY.isDown()) {
				transformationAbilityCooldown = 20;
				moriyashiine.bewitchment.common.packet.TransformationAbilityPacket.send();
			}
			if (BWComponents.BROOM_USER_COMPONENT.get(player).isPressingForward()) {
				TogglePressingForwardPacket.send(false);
			}
			if (Minecraft.getInstance().options.keyUp.isDown() && player.getVehicle() instanceof BroomEntity) {
				TogglePressingForwardPacket.send(true);
			}
		}
	}
}
