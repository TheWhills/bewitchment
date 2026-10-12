/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.component.base;

import org.jetbrains.annotations.Nullable;

/**
 * Implemented on every {@code Entity} by {@code EntityComponentHolderMixin}. Holds the entity's
 * {@link ComponentContainer} directly, so components stay readable for the entity's whole life,
 * including the rest of the tick after it dies (Forge empties capabilities as soon as an entity is
 * removed, while Cardinal Components kept them), and lookups skip the capability map.
 */
public interface ComponentHolder {
	@Nullable
	ComponentContainer bewitchment$getComponents();

	void bewitchment$setComponents(@Nullable ComponentContainer container);
}
