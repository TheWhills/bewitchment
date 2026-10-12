/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.ritualfunction;

import moriyashiine.bewitchment.api.registry.RitualFunction;
import moriyashiine.bewitchment.common.entity.living.ToadEntity;
import moriyashiine.bewitchment.common.entity.living.util.BWTameableEntity;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.function.Predicate;

public class WednesdayRitualFunction extends RitualFunction {
	public WednesdayRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public String getInvalidMessage() {
		return "ritual.precondition.wednesday";
	}

	@Override
	public boolean isValid(ServerLevel world, BlockPos pos, Container inventory) {
		return LocalDateTime.now().getDayOfWeek() == DayOfWeek.WEDNESDAY;
	}

	@Override
	public void tick(Level world, BlockPos glyphPos, BlockPos effectivePos, boolean catFamiliar) {
		if (!world.isClientSide) {
			if (world.getGameTime() % 20 == 0) {
				for (int i = 0; i < world.random.nextInt(4) + (catFamiliar ? 3 : 1); i++) {
					ToadEntity entity = BWEntityTypes.TOAD.create(world);
					if (entity != null) {
						entity.finalizeSpawn((ServerLevelAccessor) world, world.getCurrentDifficultyAt(effectivePos), MobSpawnType.EVENT, null, null);
						entity.getEntityData().set(BWTameableEntity.VARIANT, 0);
						entity.absMoveTo(effectivePos.getX() + 0.5 + Mth.nextDouble(world.random, -3, 3), effectivePos.getY() + 3, effectivePos.getZ() + 0.5 + Mth.nextDouble(world.random, -3, 3), world.random.nextFloat() * 360, 0);
						entity.addEffect(new MobEffectInstance(BWStatusEffects.WEDNESDAY, world.random.nextInt(100)));
						entity.isFromWednesdayRitual = true;
						world.addFreshEntity(entity);
					}
				}
			}
		}
	}
}
