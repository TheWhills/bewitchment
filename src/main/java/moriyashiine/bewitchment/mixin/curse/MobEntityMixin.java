/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.curse;

import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWCurses;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(Mob.class)
public abstract class MobEntityMixin extends LivingEntity {
	@Unique
	private boolean spawnedByArachnophobia = false;

	@Shadow
	public abstract void setTarget(@Nullable LivingEntity target);

	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@ModifyVariable(method = "setTarget", at = @At("HEAD"), argsOnly = true)
	private LivingEntity modifyTarget(LivingEntity target) {
		if (!level().isClientSide && target != null) {
			UUID insanityTargetUUID = BWComponents.FAKE_MOB_COMPONENT.get(this).getTarget();
			if (insanityTargetUUID != null && !target.getUUID().equals(insanityTargetUUID)) {
				return null;
			}
		}
		return target;
	}

	@Inject(method = "dropFromLootTable", at = @At("HEAD"))
	private void dropLoot(DamageSource source, boolean causedByPlayer, CallbackInfo callbackInfo) {
		if (!level().isClientSide && (Object) this instanceof Spider && !spawnedByArachnophobia && source.getEntity() instanceof LivingEntity livingAttacker && BWComponents.CURSES_COMPONENT.get(livingAttacker).hasCurse(BWCurses.ARACHNOPHOBIA.get())) {
			for (int i = 0; i < random.nextInt(3) + 3; i++) {
				Spider spider;
				if (random.nextFloat() < 1 / 8192f) {
					spider = EntityType.SPIDER.create(level());
				} else {
					spider = EntityType.CAVE_SPIDER.create(level());
					((MobEntityMixin) (Object) spider).spawnedByArachnophobia = true;
				}
				if (spider != null) {
					spider.absMoveTo(getX(), getY(), getZ(), random.nextFloat() * 360, 0);
					spider.finalizeSpawn((ServerLevelAccessor) level(), level().getCurrentDifficultyAt(blockPosition()), MobSpawnType.EVENT, null, null);
					spider.setTarget(livingAttacker);
					level().addFreshEntity(spider);
				}
			}
		}
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void readCustomDataFromNbt(CompoundTag nbt, CallbackInfo callbackInfo) {
		if ((Object) this instanceof CaveSpider) {
			spawnedByArachnophobia = nbt.getBoolean("SpawnedByArachnophobia");
		}
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void writeCustomDataToNbt(CompoundTag nbt, CallbackInfo callbackInfo) {
		if ((Object) this instanceof CaveSpider) {
			nbt.putBoolean("SpawnedByArachnophobia", spawnedByArachnophobia);
		}
	}
}
