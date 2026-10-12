/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.base;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The one capability Bewitchment attaches to an entity: every component that applies to the
 * entity's class, saved together under {@code ForgeCaps."bewitchment:components"}.
 */
public final class ComponentContainer implements ICapabilitySerializable<CompoundTag> {
	public static final Capability<ComponentContainer> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	private final Map<ComponentKey<?>, Component> components;
	private final List<ServerTickingComponent> serverTickers = new ArrayList<>();
	private final List<ClientTickingComponent> clientTickers = new ArrayList<>();
	private final LazyOptional<ComponentContainer> self = LazyOptional.of(() -> this);

	private ComponentContainer(Map<ComponentKey<?>, Component> components) {
		this.components = components;
		for (Component component : components.values()) {
			if (component instanceof ServerTickingComponent ticking) {
				serverTickers.add(ticking);
			}
			if (component instanceof ClientTickingComponent ticking) {
				clientTickers.add(ticking);
			}
		}
	}

	/** Builds the container for {@code entity}, or {@code null} if no component applies to it. */
	@Nullable
	static ComponentContainer create(Entity entity) {
		Map<ComponentKey<?>, Component> components = new LinkedHashMap<>();
		for (ComponentKey<?> key : ComponentKey.all()) {
			ComponentKey.Registration<?, ?> registration = key.registrationFor(entity);
			if (registration != null) {
				components.put(key, registration.create(entity));
			}
		}
		return components.isEmpty() ? null : new ComponentContainer(components);
	}

	@Nullable
	public static ComponentContainer of(Entity entity) {
		return entity.getCapability(CAPABILITY).resolve().orElse(null);
	}

	@Nullable
	@SuppressWarnings("unchecked")
	public <C extends Component> C get(ComponentKey<C> key) {
		return (C) components.get(key);
	}

	public Map<ComponentKey<?>, Component> components() {
		return Collections.unmodifiableMap(components);
	}

	public void serverTick() {
		for (int i = 0; i < serverTickers.size(); i++) {
			serverTickers.get(i).serverTick();
		}
	}

	public void clientTick() {
		for (int i = 0; i < clientTickers.size(); i++) {
			clientTickers.get(i).clientTick();
		}
	}

	void invalidate() {
		self.invalidate();
	}

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
		return CAPABILITY.orEmpty(capability, self);
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		components.forEach((key, component) -> {
			CompoundTag componentTag = new CompoundTag();
			component.writeToNbt(componentTag);
			tag.put(key.getId().toString(), componentTag);
		});
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag tag) {
		components.forEach((key, component) -> {
			String id = key.getId().toString();
			if (tag.contains(id, CompoundTag.TAG_COMPOUND)) {
				component.readFromNbt(tag.getCompound(id));
			}
		});
	}
}
