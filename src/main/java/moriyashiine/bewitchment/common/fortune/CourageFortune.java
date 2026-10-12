/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.fortune;

import moriyashiine.bewitchment.api.registry.Fortune;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class CourageFortune extends Fortune {
	public CourageFortune(boolean positive) {
		super(positive);
	}

	@Override
	public boolean tick(ServerLevel world, Player target) {
		if (target.getLastHurtByMob() != null) {
			target.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 1));
			target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 1));
			return true;
		}
		return super.tick(world, target);
	}
}
