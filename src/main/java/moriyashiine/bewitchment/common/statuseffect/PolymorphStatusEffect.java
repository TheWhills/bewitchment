/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.common.component.entity.PolymorphComponent;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class PolymorphStatusEffect extends MobEffect {
	public PolymorphStatusEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public void addAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
		if (entity instanceof ServerPlayer player) {
			BWComponents.POLYMORPH_COMPONENT.maybeGet(player).ifPresent(polymorphComponent -> {
				if (polymorphComponent.getUuid() != null) {
					PolymorphComponent.refresh(player);
				}
			});
		}
	}
}
