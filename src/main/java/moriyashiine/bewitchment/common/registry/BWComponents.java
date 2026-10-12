/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.component.*;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.component.entity.*;
import moriyashiine.bewitchment.common.component.base.ComponentKey;
import moriyashiine.bewitchment.common.component.base.RespawnCopyStrategy;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.LargeFireball;

/**
 * Bewitchment's per-entity components and the entities they attach to. Formerly Cardinal
 * Components registrations; now backed by one Forge capability per entity (see ComponentContainer).
 */
public class BWComponents {
	public static final ComponentKey<ContractsComponent> CONTRACTS_COMPONENT = ComponentKey.create(Bewitchment.id("contracts"), ContractsComponent.class);
	public static final ComponentKey<FortuneComponent> FORTUNE_COMPONENT = ComponentKey.create(Bewitchment.id("fortune"), FortuneComponent.class);
	public static final ComponentKey<MagicComponent> MAGIC_COMPONENT = ComponentKey.create(Bewitchment.id("magic"), MagicComponent.class);
	public static final ComponentKey<PledgeComponent> PLEDGE_COMPONENT = ComponentKey.create(Bewitchment.id("pledge"), PledgeComponent.class);
	public static final ComponentKey<TransformationComponent> TRANSFORMATION_COMPONENT = ComponentKey.create(Bewitchment.id("transformation"), TransformationComponent.class);
	public static final ComponentKey<BloodComponent> BLOOD_COMPONENT = ComponentKey.create(Bewitchment.id("blood"), BloodComponent.class);
	public static final ComponentKey<CursesComponent> CURSES_COMPONENT = ComponentKey.create(Bewitchment.id("curses"), CursesComponent.class);

	public static final ComponentKey<AdditionalWerewolfDataComponent> ADDITIONAL_WEREWOLF_DATA_COMPONENT = ComponentKey.create(Bewitchment.id("additional_werewolf_data"), AdditionalWerewolfDataComponent.class);
	public static final ComponentKey<RespawnTimerComponent> RESPAWN_TIMER_COMPONENT = ComponentKey.create(Bewitchment.id("respawn_timer"), RespawnTimerComponent.class);
	public static final ComponentKey<TeleportTimerComponent> TELEPORT_TIMER_COMPONENT = ComponentKey.create(Bewitchment.id("teleport_timer"), TeleportTimerComponent.class);
	public static final ComponentKey<FullInvisibilityComponent> FULL_INVISIBILITY_COMPONENT = ComponentKey.create(Bewitchment.id("full_invisibility"), FullInvisibilityComponent.class);
	public static final ComponentKey<BroomUserComponent> BROOM_USER_COMPONENT = ComponentKey.create(Bewitchment.id("broom_user"), BroomUserComponent.class);
	public static final ComponentKey<PolymorphComponent> POLYMORPH_COMPONENT = ComponentKey.create(Bewitchment.id("polymorph"), PolymorphComponent.class);
	public static final ComponentKey<AdditionalWaterDataComponent> ADDITIONAL_WATER_DATA_COMPONENT = ComponentKey.create(Bewitchment.id("additional_water_data"), AdditionalWaterDataComponent.class);
	public static final ComponentKey<FamiliarComponent> FAMILIAR_COMPONENT = ComponentKey.create(Bewitchment.id("familiar"), FamiliarComponent.class);
	public static final ComponentKey<MinionComponent> MINION_COMPONENT = ComponentKey.create(Bewitchment.id("minion"), MinionComponent.class);
	public static final ComponentKey<FakeMobComponent> FAKE_MOB_COMPONENT = ComponentKey.create(Bewitchment.id("fake_mob"), FakeMobComponent.class);
	public static final ComponentKey<WerewolfVillagerComponent> WEREWOLF_VILLAGER_COMPONENT = ComponentKey.create(Bewitchment.id("werewolf_villager"), WerewolfVillagerComponent.class);
	public static final ComponentKey<CaduceusFireballComponent> CADUCEUS_FIREBALL_COMPONENT = ComponentKey.create(Bewitchment.id("caduceus_fireball"), CaduceusFireballComponent.class);

	/** Called once from mod construction, before any entity can be created. */
	public static void init() {
		CONTRACTS_COMPONENT.attach(Player.class, ContractsComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		FORTUNE_COMPONENT.attach(Player.class, FortuneComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		MAGIC_COMPONENT.attach(Player.class, MagicComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		PLEDGE_COMPONENT.attach(Player.class, PledgeComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		TRANSFORMATION_COMPONENT.attach(Player.class, TransformationComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		ADDITIONAL_WEREWOLF_DATA_COMPONENT.attach(Player.class, AdditionalWerewolfDataComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		BROOM_USER_COMPONENT.attach(Player.class, BroomUserComponent::new, RespawnCopyStrategy.NEVER_COPY);
		FULL_INVISIBILITY_COMPONENT.attach(Player.class, FullInvisibilityComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		POLYMORPH_COMPONENT.attach(Player.class, PolymorphComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		RESPAWN_TIMER_COMPONENT.attach(Player.class, player -> new RespawnTimerComponent(), RespawnCopyStrategy.LOSSLESS_ONLY);
		TELEPORT_TIMER_COMPONENT.attach(Player.class, player -> new TeleportTimerComponent(), RespawnCopyStrategy.LOSSLESS_ONLY);
		BLOOD_COMPONENT.attach(LivingEntity.class, BloodComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		CURSES_COMPONENT.attach(LivingEntity.class, CursesComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		POLYMORPH_COMPONENT.attach(Arrow.class, PolymorphComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		POLYMORPH_COMPONENT.attach(AreaEffectCloud.class, PolymorphComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		ADDITIONAL_WATER_DATA_COMPONENT.attach(Entity.class, AdditionalWaterDataComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
		FAMILIAR_COMPONENT.attach(LivingEntity.class, FamiliarComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		MINION_COMPONENT.attach(Mob.class, MinionComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		FAKE_MOB_COMPONENT.attach(Mob.class, FakeMobComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		WEREWOLF_VILLAGER_COMPONENT.attach(Villager.class, WerewolfVillagerComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		CADUCEUS_FIREBALL_COMPONENT.attach(LargeFireball.class, fireballEntity -> new CaduceusFireballComponent(), RespawnCopyStrategy.ALWAYS_COPY);
	}
}
