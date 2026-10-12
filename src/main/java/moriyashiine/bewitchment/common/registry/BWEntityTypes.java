/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.entity.DragonsBloodBroomEntity;
import moriyashiine.bewitchment.common.entity.ElderBroomEntity;
import moriyashiine.bewitchment.common.entity.JuniperBroomEntity;
import moriyashiine.bewitchment.common.entity.living.*;
import moriyashiine.bewitchment.common.entity.projectile.HornedSpearEntity;
import moriyashiine.bewitchment.common.entity.projectile.SilverArrowEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;
import net.minecraftforge.registries.ForgeRegistries;

public class BWEntityTypes {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Bewitchment.MOD_ID);

	public static final RegistryObject<EntityType<JuniperBroomEntity>> JUNIPER_BROOM = create("juniper_broom", () -> EntityType.Builder.<JuniperBroomEntity>of(JuniperBroomEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(Bewitchment.id("juniper_broom").toString()));
	public static final RegistryObject<EntityType<BroomEntity>> CYPRESS_BROOM = create("cypress_broom", () -> EntityType.Builder.<BroomEntity>of(BroomEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(Bewitchment.id("cypress_broom").toString()));
	public static final RegistryObject<EntityType<ElderBroomEntity>> ELDER_BROOM = create("elder_broom", () -> EntityType.Builder.<ElderBroomEntity>of(ElderBroomEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(Bewitchment.id("elder_broom").toString()));
	public static final RegistryObject<EntityType<DragonsBloodBroomEntity>> DRAGONS_BLOOD_BROOM = create("dragons_blood_broom", () -> EntityType.Builder.<DragonsBloodBroomEntity>of(DragonsBloodBroomEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(Bewitchment.id("dragons_blood_broom").toString()));

	public static final RegistryObject<EntityType<SilverArrowEntity>> SILVER_ARROW = create("silver_arrow", () -> EntityType.Builder.<SilverArrowEntity>of(SilverArrowEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(Bewitchment.id("silver_arrow").toString()));
	public static final RegistryObject<EntityType<HornedSpearEntity>> HORNED_SPEAR = create("horned_spear", () -> EntityType.Builder.<HornedSpearEntity>of(HornedSpearEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(Bewitchment.id("horned_spear").toString()));

	public static final RegistryObject<EntityType<OwlEntity>> OWL = create("owl", () -> EntityType.Builder.of(OwlEntity::new, MobCategory.CREATURE).sized(0.5f, 0.75f).build(Bewitchment.id("owl").toString()));
	public static final RegistryObject<EntityType<RavenEntity>> RAVEN = create("raven", () -> EntityType.Builder.of(RavenEntity::new, MobCategory.CREATURE).sized(0.4f, 0.4f).build(Bewitchment.id("raven").toString()));
	public static final RegistryObject<EntityType<SnakeEntity>> SNAKE = create("snake", () -> EntityType.Builder.of(SnakeEntity::new, MobCategory.CREATURE).sized(0.75f, 0.25f).build(Bewitchment.id("snake").toString()));
	public static final RegistryObject<EntityType<ToadEntity>> TOAD = create("toad", () -> EntityType.Builder.of(ToadEntity::new, MobCategory.CREATURE).sized(0.5f, 0.5f).build(Bewitchment.id("toad").toString()));

	public static final RegistryObject<EntityType<GhostEntity>> GHOST = create("ghost", () -> EntityType.Builder.of(GhostEntity::new, MobCategory.MONSTER).sized(0.6f, 1.8f).fireImmune().build(Bewitchment.id("ghost").toString()));
	public static final RegistryObject<EntityType<VampireEntity>> VAMPIRE = create("vampire", () -> EntityType.Builder.of(VampireEntity::new, MobCategory.MONSTER).sized(0.6f, 1.95f).build(Bewitchment.id("vampire").toString()));
	public static final RegistryObject<EntityType<WerewolfEntity>> WEREWOLF = create("werewolf", () -> EntityType.Builder.of(WerewolfEntity::new, MobCategory.MONSTER).sized(0.8f, 2.8f).build(Bewitchment.id("werewolf").toString()));
	public static final RegistryObject<EntityType<HellhoundEntity>> HELLHOUND = create("hellhound", () -> EntityType.Builder.of(HellhoundEntity::new, MobCategory.MONSTER).sized(0.6f, 0.85f).fireImmune().build(Bewitchment.id("hellhound").toString()));
	public static final RegistryObject<EntityType<DemonEntity>> DEMON = create("demon", () -> EntityType.Builder.of(DemonEntity::new, MobCategory.MONSTER).sized(0.8f, 2.4f).fireImmune().build(Bewitchment.id("demon").toString()));

	public static final RegistryObject<EntityType<LeonardEntity>> LEONARD = create("leonard", () -> EntityType.Builder.of(LeonardEntity::new, MobCategory.MONSTER).sized(0.8f, 2.8f).fireImmune().build(Bewitchment.id("leonard").toString()));
	public static final RegistryObject<EntityType<BaphometEntity>> BAPHOMET = create("baphomet", () -> EntityType.Builder.of(BaphometEntity::new, MobCategory.MONSTER).sized(0.8f, 2.8f).fireImmune().build(Bewitchment.id("baphomet").toString()));
	public static final RegistryObject<EntityType<LilithEntity>> LILITH = create("lilith", () -> EntityType.Builder.of(LilithEntity::new, MobCategory.MONSTER).sized(0.8f, 2.8f).fireImmune().build(Bewitchment.id("lilith").toString()));
	public static final RegistryObject<EntityType<HerneEntity>> HERNE = create("herne", () -> EntityType.Builder.of(HerneEntity::new, MobCategory.MONSTER).sized(0.8f, 2.8f).fireImmune().build(Bewitchment.id("herne").toString()));

	private static <T extends Entity> RegistryObject<EntityType<T>> create(String name, Supplier<EntityType<T>> type) {
		return ENTITY_TYPES.register(name, type);
	}

	static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(VAMPIRE.get(), VampireEntity.createAttributes().build());
		event.put(HELLHOUND.get(), HellhoundEntity.createAttributes().build());
		event.put(OWL.get(), OwlEntity.createAttributes().build());
		event.put(RAVEN.get(), RavenEntity.createAttributes().build());
		event.put(SNAKE.get(), SnakeEntity.createAttributes().build());
		event.put(TOAD.get(), ToadEntity.createAttributes().build());
		event.put(GHOST.get(), GhostEntity.createAttributes().build());
		event.put(WEREWOLF.get(), WerewolfEntity.createAttributes().build());
		event.put(DEMON.get(), DemonEntity.createAttributes().build());
		event.put(LEONARD.get(), LeonardEntity.createAttributes().build());
		event.put(BAPHOMET.get(), BaphometEntity.createAttributes().build());
		event.put(LILITH.get(), LilithEntity.createAttributes().build());
		event.put(HERNE.get(), HerneEntity.createAttributes().build());
	}
}
