/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.base;

import moriyashiine.bewitchment.client.packet.SyncComponentPacket;
import moriyashiine.bewitchment.common.packet.BWNetwork;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

/**
 * Handle for one kind of component. Mirrors the Cardinal Components {@code ComponentKey} API
 * ({@link #get}, {@link #maybeGet}, {@link #sync}) on top of a single Forge capability per entity
 * ({@link ComponentContainer}).
 */
public final class ComponentKey<C extends Component> {
	private static final Map<ResourceLocation, ComponentKey<?>> BY_ID = new LinkedHashMap<>();

	private final ResourceLocation id;
	private final Class<C> type;
	private final List<Registration<?, C>> registrations = new ArrayList<>();

	private ComponentKey(ResourceLocation id, Class<C> type) {
		this.id = id;
		this.type = type;
	}

	public static <C extends Component> ComponentKey<C> create(ResourceLocation id, Class<C> type) {
		ComponentKey<C> key = new ComponentKey<>(id, type);
		if (BY_ID.putIfAbsent(id, key) != null) {
			throw new IllegalStateException("Duplicate component key " + id);
		}
		return key;
	}

	@Nullable
	public static ComponentKey<?> byId(ResourceLocation id) {
		return BY_ID.get(id);
	}

	public static Collection<ComponentKey<?>> all() {
		return Collections.unmodifiableCollection(BY_ID.values());
	}

	/** Attach this component to every entity of {@code target} (and subclasses). */
	public <E extends Entity> ComponentKey<C> attach(Class<E> target, Function<? super E, ? extends C> factory, RespawnCopyStrategy respawnStrategy) {
		registrations.add(new Registration<>(target, factory, respawnStrategy));
		return this;
	}

	public ResourceLocation getId() {
		return id;
	}

	public C get(Entity entity) {
		return maybeGet(entity).orElseThrow(() -> new NoSuchElementException(id + " is not attached to " + entity));
	}

	public Optional<C> maybeGet(@Nullable Entity entity) {
		if (entity == null) {
			return Optional.empty();
		}
		ComponentContainer container = ComponentContainer.of(entity);
		return container == null ? Optional.empty() : Optional.ofNullable(container.get(this));
	}

	public boolean isProvidedBy(@Nullable Entity entity) {
		return maybeGet(entity).isPresent();
	}

	/** Send this component to the entity itself (if a player) and every player tracking it. Server side only. */
	public void sync(Entity entity) {
		if (entity.level().isClientSide) {
			return;
		}
		maybeGet(entity).ifPresent(component -> {
			if (component instanceof AutoSyncedComponent synced) {
				BWNetwork.sendToTrackingAndSelf(entity, new SyncComponentPacket(entity.getId(), id, synced.writeSyncData()));
			}
		});
	}

	/** Send this component to one player only (used when tracking starts and on login/respawn). */
	public void syncWith(ServerPlayer recipient, Entity entity) {
		maybeGet(entity).ifPresent(component -> {
			if (component instanceof AutoSyncedComponent synced) {
				BWNetwork.sendToPlayer(recipient, new SyncComponentPacket(entity.getId(), id, synced.writeSyncData()));
			}
		});
	}

	/** The most specific registration whose target class matches {@code entity}, if any. */
	@Nullable
	Registration<?, C> registrationFor(Entity entity) {
		Registration<?, C> best = null;
		for (Registration<?, C> registration : registrations) {
			if (registration.target.isInstance(entity) && (best == null || best.target.isAssignableFrom(registration.target))) {
				best = registration;
			}
		}
		return best;
	}

	@Override
	public String toString() {
		return "ComponentKey[" + id + "]";
	}

	record Registration<E extends Entity, C extends Component>(Class<E> target, Function<? super E, ? extends C> factory, RespawnCopyStrategy respawnStrategy) {
		C create(Entity entity) {
			return factory.apply(target.cast(entity));
		}
	}
}
