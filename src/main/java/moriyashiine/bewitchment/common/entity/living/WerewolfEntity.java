/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity.living;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.packet.BWNetwork;
import moriyashiine.bewitchment.client.packet.SpawnSmokeParticlesPacket;
import moriyashiine.bewitchment.common.entity.living.util.BWHostileEntity;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;
import net.minecraft.tags.BiomeTags;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ConstantConditions")
public class WerewolfEntity extends BWHostileEntity {
	public CompoundTag storedVillager;

	public WerewolfEntity(EntityType<? extends Monster> entityType, Level world) {
		super(entityType, world);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20).add(Attributes.ATTACK_DAMAGE, 15).add(Attributes.ARMOR, 20).add(Attributes.ARMOR_TOUGHNESS, 8).add(Attributes.MOVEMENT_SPEED, 0.4).add(ForgeMod.ENTITY_REACH.get(), 4).add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1);
	}

	public static boolean canSpawn(EntityType<WerewolfEntity> type, ServerLevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
		return world.getLevel().isNight() && BewitchmentAPI.getMoonPhase(world.getLevel().getLevel()) == 0 && Monster.checkMonsterSpawnRules(type, world, spawnReason, pos, random);
	}

	@Override
	public void tick() {
		super.tick();
		if (!level().isClientSide) {
			getArmorSlots().forEach(stack -> spawnAtLocation(stack.split(1)));
			if (BWUtil.isTool(getMainHandItem())) {
				spawnAtLocation(getMainHandItem().split(1));
			}
			if (BWUtil.isTool(getOffhandItem())) {
				spawnAtLocation(getOffhandItem().split(1));
			}
			if (storedVillager != null && tickCount % 20 == 0 && (level().isDay() || BewitchmentAPI.getMoonPhase(level()) != 0)) {
				Villager entity = EntityType.VILLAGER.create(level());
				if (entity != null) {
					BWNetwork.sendToTracking(this, new SpawnSmokeParticlesPacket(this));
					level().playSound(null, getX(), getY(), getZ(), BWSoundEvents.ENTITY_GENERIC_TRANSFORM.get(), getSoundSource(), getSoundVolume(), getVoicePitch());
					entity.load(storedVillager);
					entity.absMoveTo(getX(), getY(), getZ(), random.nextFloat() * 360, 0);
					entity.setHealth(entity.getMaxHealth() * (getHealth() / getMaxHealth()));
					entity.setRemainingFireTicks(getRemainingFireTicks());
					entity.removeAllEffects();
					getActiveEffects().forEach(entity::addEffect);
					BWComponents.CURSES_COMPONENT.maybeGet(entity).ifPresent(entityCursesComponent -> BWComponents.CURSES_COMPONENT.maybeGet(this).ifPresent(thisCursesComponent -> {
						entityCursesComponent.getCurses().clear();
						thisCursesComponent.getCurses().forEach(entityCursesComponent::addCurse);
					}));
					BWComponents.WEREWOLF_VILLAGER_COMPONENT.get(entity).setStoredWerewolf(saveWithoutId(new CompoundTag()));
					level().addFreshEntity(entity);
					remove(RemovalReason.DISCARDED);
				}
			}
		}
	}

	@Override
	protected boolean hasShiny() {
		return true;
	}

	@Override
	public int getVariants() {
		return getVariantsStatic();
	}

	@Override
	public MobType getMobType() {
		return BewitchmentAPI.DEMON;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return BWSoundEvents.ENTITY_WEREWOLF_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BWSoundEvents.ENTITY_WEREWOLF_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BWSoundEvents.ENTITY_WEREWOLF_DEATH.get();
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData, @Nullable CompoundTag entityTag) {
		SpawnGroupData data = super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityTag);
		if (entityData.get(VARIANT) != 0) {
			Holder<Biome> biome = world.getBiome(blockPosition());
			if (biome.is(BiomeTags.IS_FOREST)) {
				entityData.set(VARIANT, random.nextBoolean() ? 1 : 2);
			} else if (biome.is(BiomeTags.IS_TAIGA)) {
				entityData.set(VARIANT, random.nextBoolean() ? 3 : 4);
			}
			if (biome.is(Tags.Biomes.IS_SNOWY)) {
				entityData.set(VARIANT, random.nextBoolean() ? 5 : 6);
			} else {
				entityData.set(VARIANT, random.nextInt(getVariants() - 1) + 1);
			}
		}
		if (spawnReason == MobSpawnType.NATURAL || spawnReason == MobSpawnType.SPAWN_EGG) {
			storedVillager = EntityType.VILLAGER.create((Level) world).saveWithoutId(new CompoundTag());
		}
		return data;
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		if (nbt.contains("StoredVillager")) {
			storedVillager = nbt.getCompound("StoredVillager");
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		if (storedVillager != null) {
			nbt.put("StoredVillager", storedVillager);
		}
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, new MeleeAttackGoal(this, 1, true));
		goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1));
		goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8));
		goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		targetSelector.addGoal(0, new HurtByTargetGoal(this));
		targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> entity instanceof Player || entity instanceof Sheep || entity instanceof AbstractVillager || entity.getMobType() == MobType.ILLAGER));
	}

	public static int getVariantsStatic() {
		return 7;
	}
}
