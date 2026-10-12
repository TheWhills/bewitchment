/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.fortune;

import moriyashiine.bewitchment.api.registry.Fortune;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class IllnessFortune extends Fortune {
	public IllnessFortune(boolean positive) {
		super(positive);
	}

	@Override
	public boolean finish(ServerLevel world, Player target) {
		target.addEffect(new MobEffectInstance(MobEffects.POISON, 600, 1));
		target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600, 1));
		return super.finish(world, target);
	}
}
