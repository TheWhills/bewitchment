/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.statuseffect;

import moriyashiine.bewitchment.client.packet.SpawnExplosionParticlesPacket;
import moriyashiine.bewitchment.common.packet.BWNetwork;
import moriyashiine.bewitchment.common.entity.living.ToadEntity;
import moriyashiine.bewitchment.common.registry.BWDamageSources;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class WednesdayStatusEffect extends MobEffect {
	public WednesdayStatusEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
		super.removeAttributeModifiers(entity, attributes, amplifier);
		entity.level().playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 1, 1);
		BWNetwork.sendToTrackingAndSelf(entity, new SpawnExplosionParticlesPacket(entity));
		if (entity instanceof ToadEntity toad && toad.isFromWednesdayRitual) {
			entity.remove(Entity.RemovalReason.DISCARDED);
		} else {
			entity.hurt(BWDamageSources.create(entity.level(), BWDamageSources.WEDNESDAY), Float.MAX_VALUE);
		}
	}
}
