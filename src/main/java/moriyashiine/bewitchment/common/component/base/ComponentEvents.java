/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.base;

import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Attaches, copies and syncs component containers. Together with the level tick mixins this
 * covers what Cardinal Components did automatically on Fabric.
 */
public final class ComponentEvents {
	private static final ResourceLocation CONTAINER_ID = Bewitchment.id("components");

	private ComponentEvents() {
	}

	public static void register(IEventBus modBus, IEventBus forgeBus) {
		modBus.addListener((RegisterCapabilitiesEvent event) -> event.register(ComponentContainer.class));
		forgeBus.addGenericListener(Entity.class, ComponentEvents::attach);
		forgeBus.addListener(ComponentEvents::onClone);
		forgeBus.addListener(ComponentEvents::onStartTracking);
		forgeBus.addListener((PlayerEvent.PlayerLoggedInEvent event) -> syncAllToSelf(event.getEntity()));
		forgeBus.addListener((PlayerEvent.PlayerRespawnEvent event) -> syncAllToSelf(event.getEntity()));
		forgeBus.addListener((PlayerEvent.PlayerChangedDimensionEvent event) -> syncAllToSelf(event.getEntity()));
	}

	private static void attach(AttachCapabilitiesEvent<Entity> event) {
		ComponentContainer container = ComponentContainer.create(event.getObject());
		if (container != null) {
			event.addCapability(CONTAINER_ID, container);
			event.addListener(container::invalidate);
		}
	}

	/** Carries components over to the new player entity according to each registration's respawn strategy. */
	private static void onClone(PlayerEvent.Clone event) {
		Player original = event.getOriginal();
		Player replacement = event.getEntity();
		original.reviveCaps();
		try {
			ComponentContainer from = ComponentContainer.of(original);
			ComponentContainer to = ComponentContainer.of(replacement);
			if (from == null || to == null) {
				return;
			}
			boolean keepInventory = replacement.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
			from.components().forEach((key, oldComponent) -> {
				ComponentKey.Registration<?, ?> registration = key.registrationFor(replacement);
				Component newComponent = to.components().get(key);
				if (registration != null && newComponent != null && registration.respawnStrategy().shouldCopy(event.isWasDeath(), keepInventory)) {
					CompoundTag tag = new CompoundTag();
					oldComponent.writeToNbt(tag);
					newComponent.readFromNbt(tag);
				}
			});
		} finally {
			original.invalidateCaps();
		}
	}

	/** A player just started seeing {@code target}: send it the target's synced components. */
	private static void onStartTracking(PlayerEvent.StartTracking event) {
		if (event.getEntity() instanceof ServerPlayer tracker) {
			ComponentContainer container = ComponentContainer.of(event.getTarget());
			if (container != null) {
				container.components().keySet().forEach(key -> key.syncWith(tracker, event.getTarget()));
			}
		}
	}

	private static void syncAllToSelf(Player player) {
		if (player instanceof ServerPlayer serverPlayer) {
			ComponentContainer container = ComponentContainer.of(serverPlayer);
			if (container != null) {
				container.components().keySet().forEach(key -> key.syncWith(serverPlayer, serverPlayer));
			}
		}
	}
}
