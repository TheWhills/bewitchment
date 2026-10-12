/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.vampirehack;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWDamageSources;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = {Entity.class, LivingEntity.class, Player.class, ServerPlayer.class}, priority = 1)
public class FireToSunDamageMixin {
	@ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
	private DamageSource bewitchment$turnFireDamageIntoSunDamageForVampires(DamageSource source) {
		if (source.is(DamageTypeTags.IS_FIRE) && BewitchmentAPI.isVampire(Entity.class.cast(this), true)) {
			return BWDamageSources.create(Entity.class.cast(this).level(), BWDamageSources.SUN);
		}
		return source;
	}
}
