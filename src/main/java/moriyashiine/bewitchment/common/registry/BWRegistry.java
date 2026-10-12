/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Read view over one of Bewitchment's custom Forge registries that keeps the
 * vanilla {@code Registry} method names the rest of the mod was written against,
 * so call sites did not need rewriting when the registries moved from Fabric's
 * registry builder to {@link net.minecraftforge.registries.RegistryBuilder}.
 */
public final class BWRegistry<T> implements Iterable<T> {
	private final ResourceKey<Registry<T>> key;
	private final Supplier<IForgeRegistry<T>> registry;
	private List<T> byIndex;

	BWRegistry(ResourceKey<Registry<T>> key, Supplier<IForgeRegistry<T>> registry) {
		this.key = key;
		this.registry = registry;
	}

	public ResourceKey<Registry<T>> key() {
		return key;
	}

	public IForgeRegistry<T> forge() {
		return registry.get();
	}

	@Nullable
	public T get(@Nullable ResourceLocation id) {
		return id == null ? null : registry.get().getValue(id);
	}

	@Nullable
	public ResourceLocation getKey(T value) {
		return registry.get().getKey(value);
	}

	public Optional<T> getOptional(@Nullable ResourceLocation id) {
		return id != null && registry.get().containsKey(id) ? Optional.ofNullable(registry.get().getValue(id)) : Optional.empty();
	}

	public boolean containsKey(ResourceLocation id) {
		return registry.get().containsKey(id);
	}

	public Set<ResourceLocation> keySet() {
		return registry.get().getKeys();
	}

	public int size() {
		return registry.get().getKeys().size();
	}

	/**
	 * Entry at {@code index} in registration order, {@code 0 <= index < size()}. Matches how the
	 * mod used vanilla {@code byId} (random selection), without depending on Forge numeric IDs.
	 */
	@Nullable
	public T byId(int index) {
		if (byIndex == null || byIndex.size() != size()) {
			byIndex = new ArrayList<>(registry.get().getValues());
		}
		return index >= 0 && index < byIndex.size() ? byIndex.get(index) : null;
	}

	public Stream<T> stream() {
		return StreamSupport.stream(spliterator(), false);
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return registry.get().getValues().iterator();
	}
}
