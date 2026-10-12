/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.transformation;

import moriyashiine.bewitchment.api.registry.Transformation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class VampireTransformation extends Transformation {
	@Override
	public void onAdded(Player player) {
		BuiltInRegistries.MOB_EFFECT.stream().forEach(effect -> {
			MobEffectInstance effectInstance = player.getEffect(effect);
			if (effectInstance != null && !player.canBeAffected(effectInstance)) {
				player.removeEffect(effect);
			}
		});
	}

	@Override
	public void onRemoved(Player player) {
		player.removeEffect(MobEffects.NIGHT_VISION);
	}
}
