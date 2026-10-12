/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.fortune;

import moriyashiine.bewitchment.api.registry.Fortune;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EncounterFortune extends Fortune {
	public EncounterFortune(boolean positive) {
		super(positive);
	}

	@Override
	public boolean finish(ServerLevel world, Player target) {
		Entity entity = getRandomEntity(world);
		for (int i = 0; i < 8; i++) {
			BlockPos pos = target.blockPosition().offset(Mth.nextInt(world.random, -3, 3), 0, Mth.nextInt(world.random, -3, 3));
			if (!world.getBlockState(pos).blocksMotion()) {
				if (entity instanceof Mob mob) {
					mob.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.EVENT, null, null);
					mob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, Integer.MAX_VALUE, 1));
					mob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
					mob.addEffect(new MobEffectInstance(MobEffects.REGENERATION, Integer.MAX_VALUE, 1));
					mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, Integer.MAX_VALUE, 1));
				}
				entity.absMoveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, world.random.nextFloat() * 360, 0);
				world.addFreshEntity(entity);
				return true;
			}
		}
		return false;
	}

	private static Entity getRandomEntity(Level world) {
		Holder<EntityType<?>> entity = null;
		while (entity == null || !entity.is(BWTags.ENCOUNTER_FORTUNE)) {
			entity = BuiltInRegistries.ENTITY_TYPE.getRandom(world.random).orElse(null);
		}
		return entity.value().create(world);
	}
}
