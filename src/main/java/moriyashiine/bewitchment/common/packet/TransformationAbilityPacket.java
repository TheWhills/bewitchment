/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.packet;

import io.github.ladysnake.pal.AbilitySource;
import io.github.ladysnake.pal.Pal;
import io.github.ladysnake.pal.VanillaAbilities;
import io.netty.buffer.Unpooled;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.component.TransformationComponent;
import moriyashiine.bewitchment.client.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import virtuoel.pehkui.api.ScaleData;

public class TransformationAbilityPacket {
	public static final ResourceLocation ID = Bewitchment.id("transformation_ability");

	public static final AbilitySource VAMPIRE_FLIGHT_SOURCE = Pal.getAbilitySource(Bewitchment.id("vampire_flight"));

	private static final float VAMPIRE_WIDTH = EntityType.BAT.getWidth() / EntityType.PLAYER.getWidth();
	private static final float VAMPIRE_HEIGHT = EntityType.BAT.getHeight() / EntityType.PLAYER.getHeight();
	private static final float WEREWOLF_WIDTH = BWEntityTypes.WEREWOLF.get().getWidth() / EntityType.PLAYER.getWidth();
	private static final float WEREWOLF_HEIGHT = BWEntityTypes.WEREWOLF.get().getHeight() / EntityType.PLAYER.getHeight();

	public static void send() {
		ClientPlayNetworking.send(ID, new FriendlyByteBuf(Unpooled.buffer()));
	}

	private static boolean canUseAbility(Player player) {
		TransformationComponent transformationComponent = BWComponents.TRANSFORMATION_COMPONENT.get(player);
		if (transformationComponent.getTransformation() == BWTransformations.VAMPIRE.get()) {
			return true;
		}
		if (transformationComponent.getTransformation() == BWTransformations.WEREWOLF.get()) {
			return !BWComponents.ADDITIONAL_WEREWOLF_DATA_COMPONENT.get(player).isForcedTransformation();
		}
		return false;
	}

	public static void useAbility(Player player, boolean forced) {
		BWComponents.TRANSFORMATION_COMPONENT.maybeGet(player).ifPresent(transformationComponent -> {
			Level world = player.level();
			boolean isAlternateForm = transformationComponent.isAlternateForm();
			ScaleData width = BWScaleTypes.MODIFY_WIDTH_TYPE.getScaleData(player);
			ScaleData height = BWScaleTypes.MODIFY_HEIGHT_TYPE.getScaleData(player);
			if (transformationComponent.getTransformation() == BWTransformations.VAMPIRE.get() && (forced || (BewitchmentAPI.isPledged(player, BWPledges.LILITH) && BWComponents.BLOOD_COMPONENT.get(player).getBlood() > 0))) {
				PlayerLookup.tracking(player).forEach(trackingPlayer -> SpawnSmokeParticlesPacket.send(trackingPlayer, player));
				SpawnSmokeParticlesPacket.send((ServerPlayer) player, player);
				world.playSound(null, player.blockPosition(), BWSoundEvents.ENTITY_GENERIC_TRANSFORM.get(), player.getSoundSource(), 1, 1);
				transformationComponent.setAlternateForm(!isAlternateForm);
				if (isAlternateForm) {
					width.setScale(width.getBaseScale() / VAMPIRE_WIDTH);
					height.setScale(height.getBaseScale() / VAMPIRE_HEIGHT);
					VAMPIRE_FLIGHT_SOURCE.revokeFrom(player, VanillaAbilities.ALLOW_FLYING);
					VAMPIRE_FLIGHT_SOURCE.revokeFrom(player, VanillaAbilities.FLYING);
				} else {
					width.setScale(width.getBaseScale() * VAMPIRE_WIDTH);
					height.setScale(height.getBaseScale() * VAMPIRE_HEIGHT);
					VAMPIRE_FLIGHT_SOURCE.grantTo(player, VanillaAbilities.ALLOW_FLYING);
					VAMPIRE_FLIGHT_SOURCE.grantTo(player, VanillaAbilities.FLYING);
				}
			} else if (transformationComponent.getTransformation() == BWTransformations.WEREWOLF.get() && (forced || BewitchmentAPI.isPledged(player, BWPledges.HERNE))) {
				PlayerLookup.tracking(player).forEach(trackingPlayer -> SpawnSmokeParticlesPacket.send(trackingPlayer, player));
				SpawnSmokeParticlesPacket.send((ServerPlayer) player, player);
				world.playSound(null, player.blockPosition(), BWSoundEvents.ENTITY_GENERIC_TRANSFORM.get(), player.getSoundSource(), 1, 1);
				transformationComponent.setAlternateForm(!isAlternateForm);
				if (isAlternateForm) {
					width.setScale(width.getBaseScale() / WEREWOLF_WIDTH);
					height.setScale(height.getBaseScale() / WEREWOLF_HEIGHT);
					if (player.hasEffect(MobEffects.NIGHT_VISION) && player.getEffect(MobEffects.NIGHT_VISION).isAmbient()) {
						player.removeEffect(MobEffects.NIGHT_VISION);
					}
				} else {
					width.setScale(width.getBaseScale() * WEREWOLF_WIDTH);
					height.setScale(height.getBaseScale() * WEREWOLF_HEIGHT);
				}
			}
		});
	}

	public static class Receiver implements ServerPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
			server.execute(() -> {
				if (canUseAbility(player)) {
					useAbility(player, false);
				}
			});
		}
	}
}
