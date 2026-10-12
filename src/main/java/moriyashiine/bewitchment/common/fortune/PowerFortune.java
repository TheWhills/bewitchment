/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.fortune;

import moriyashiine.bewitchment.api.registry.Fortune;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class PowerFortune extends Fortune {
	public PowerFortune(boolean positive) {
		super(positive);
	}

	@Override
	public boolean tick(ServerLevel world, Player target) {
		if (target.getLastHurtByMob() != null) {
			target.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600));
			target.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600));
			return true;
		}
		return super.tick(world, target);
	}
}
