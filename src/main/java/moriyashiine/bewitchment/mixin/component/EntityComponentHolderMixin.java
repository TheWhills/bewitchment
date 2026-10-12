/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.component;

import moriyashiine.bewitchment.common.component.base.ComponentContainer;
import moriyashiine.bewitchment.common.component.base.ComponentHolder;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public class EntityComponentHolderMixin implements ComponentHolder {
	// No initializer: the container is set while Entity's constructor gathers capabilities.
	@Unique
	private ComponentContainer bewitchment$components;

	@Nullable
	@Override
	public ComponentContainer bewitchment$getComponents() {
		return bewitchment$components;
	}

	@Override
	public void bewitchment$setComponents(@Nullable ComponentContainer container) {
		bewitchment$components = container;
	}
}
