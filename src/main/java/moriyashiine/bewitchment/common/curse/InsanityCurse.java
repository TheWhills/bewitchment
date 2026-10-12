/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.curse;

import moriyashiine.bewitchment.api.registry.Curse;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.MobSpawnSettings;
import java.util.List;

public class InsanityCurse extends Curse {
	public InsanityCurse(Type type) {
		super(type);
	}

	@Override
	public void tick(LivingEntity target) {
		if (target.tickCount % 20 == 0 && target.getRandom().nextFloat() < 1 / 100f) {
			List<MobSpawnSettings.SpawnerData> entries = target.level().getBiome(target.blockPosition()).value().getMobSettings().getMobs(MobCategory.MONSTER).unwrap();
			if (entries.isEmpty()) {
				return;
			}
			Entity entity = null;
			int tries = 0;
			while (tries < 16) {
				Entity potentialSpawn = entries.get(target.getRandom().nextInt(entries.size())).type.create(target.level());
				if (potentialSpawn != null && !potentialSpawn.getType().is(BWTags.INSANITY_BLACKLIST)) {
					entity = potentialSpawn;
					break;
				}
				tries++;
			}
			if (entity instanceof Mob mob) {
				BWUtil.attemptTeleport(entity, target.blockPosition(), 24, false);
				mob.finalizeSpawn((ServerLevelAccessor) target.level(), target.level().getCurrentDifficultyAt(target.blockPosition()), MobSpawnType.EVENT, null, null);
				BWComponents.FAKE_MOB_COMPONENT.get(mob).setTarget(target.getUUID());
				entity.setSilent(true);
				mob.setCanPickUpLoot(false);
				target.level().addFreshEntity(entity);
			}
		}
	}
}
