/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client;
import moriyashiine.bewitchment.api.client.model.BroomEntityModel;
import moriyashiine.bewitchment.client.integration.appleskin.BWAppleskinIntegration;
import moriyashiine.bewitchment.client.event.CauldronTeleportEvent;
import moriyashiine.bewitchment.client.event.TransformationAbilityEvent;
import moriyashiine.bewitchment.client.model.ContributorHornsModel;
import moriyashiine.bewitchment.client.model.entity.living.*;
import moriyashiine.bewitchment.client.model.equipment.armor.WitchArmorModel;
import moriyashiine.bewitchment.client.model.equipment.trinket.DruidBandModel;
import moriyashiine.bewitchment.client.model.equipment.trinket.PricklyBeltModel;
import moriyashiine.bewitchment.client.model.equipment.trinket.SpecterBangleModel;
import moriyashiine.bewitchment.client.model.equipment.trinket.ZephyrHarnessModel;
import moriyashiine.bewitchment.client.packet.*;
import moriyashiine.bewitchment.client.particle.CauldronBubbleParticle;
import moriyashiine.bewitchment.client.particle.IncenseSmokeParticle;
import moriyashiine.bewitchment.client.render.blockentity.BrazierBlockEntityRenderer;
import moriyashiine.bewitchment.client.render.blockentity.PoppetShelfBlockEntityRenderer;
import moriyashiine.bewitchment.client.render.blockentity.WitchAltarBlockEntityRenderer;
import moriyashiine.bewitchment.client.render.blockentity.WitchCauldronBlockEntityRenderer;
import moriyashiine.bewitchment.client.render.entity.*;
import moriyashiine.bewitchment.client.render.entity.living.*;
import moriyashiine.bewitchment.client.render.trinket.*;
import moriyashiine.bewitchment.client.screen.DemonScreen;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.registry.*;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.EntityRenderersEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

@SuppressWarnings({"unchecked", "ConstantConditions"})
public class BewitchmentClient {
	public static final KeyMapping TRANSFORMATION_ABILITY = new KeyMapping("key." + Bewitchment.MOD_ID + ".transformation_ability", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, "itemGroup." + Bewitchment.MOD_ID);

	public static final ModelLayerLocation CONTRIBUTOR_HORNS_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("contributor_horns"), "main");
	public static final ModelLayerLocation WITCH_ARMOR_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("witch_armor"), "main");
	public static final ModelLayerLocation SPECTER_BANGLE_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("specter_bangle"), "main");
	public static final ModelLayerLocation PRICKLY_BELT_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("prickly_belt"), "main");
	public static final ModelLayerLocation DRUID_BAND_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("druid_band"), "main");
	public static final ModelLayerLocation ZEPHYR_HARNESS_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("zephyr_harness"), "main");

	public static final ModelLayerLocation BROOM_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("broom"), "main");
	public static final ModelLayerLocation OWL_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("owl"), "main");
	public static final ModelLayerLocation RAVEN_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("raven"), "main");
	public static final ModelLayerLocation SNAKE_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("snake"), "main");
	public static final ModelLayerLocation TOAD_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("toad"), "main");
	public static final ModelLayerLocation GHOST_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("ghost"), "main");
	public static final ModelLayerLocation VAMPIRE_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("vampire"), "main");
	public static final ModelLayerLocation WEREWOLF_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("werewolf"), "main");
	public static final ModelLayerLocation HELLHOUND_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("hellhound"), "main");
	public static final ModelLayerLocation MALE_DEMON_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("male_demon"), "main");
	public static final ModelLayerLocation FEMALE_DEMON_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("female_demon"), "main");
	public static final ModelLayerLocation LEONARD_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("leonard"), "main");
	public static final ModelLayerLocation BAPHOMET_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("baphomet"), "main");
	public static final ModelLayerLocation LILITH_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("lilith"), "main");
	public static final ModelLayerLocation HERNE_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("herne"), "main");

	/**
	 * Client entry point, reached only on the physical client (DistExecutor in the @Mod constructor).
	 * Registration happens in Forge's client events; the Fabric API registries these replaced are
	 * noted per method.
	 */
	public static void init() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(BewitchmentClient::onClientSetup);
		modBus.addListener(BewitchmentClient::onRegisterKeyMappings);
		modBus.addListener(BewitchmentClient::onRegisterParticles);
		modBus.addListener(BewitchmentClient::onRegisterBlockColors);
		modBus.addListener(BewitchmentClient::onRegisterItemColors);
		modBus.addListener(BewitchmentClient::onRegisterRenderers);
		modBus.addListener(BewitchmentClient::onRegisterLayerDefinitions);
		MinecraftForge.EVENT_BUS.addListener(TransformationAbilityEvent::onLevelTick);
		MinecraftForge.EVENT_BUS.addListener(CauldronTeleportEvent::onClientChat);
		if (ModList.get().isLoaded("appleskin")) {
			BWAppleskinIntegration.register();
		}
	}

	/** Item model predicates, menu screens and sign wood types; these touch vanilla maps, so main thread. */
	private static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ItemProperties.register(BWObjects.HEDGEWITCH_HAT.get(), Bewitchment.id("variant"), (stack, world, entity, seed) -> stack.getHoverName().getString().toLowerCase().contains("faith") ? 1 : 0);
			ItemProperties.register(BWObjects.ALCHEMIST_HAT.get(), Bewitchment.id("variant"), (stack, world, entity, seed) -> stack.getHoverName().getString().toLowerCase().contains("faith") ? 1 : 0);
			ItemProperties.register(BWObjects.BESMIRCHED_HAT.get(), Bewitchment.id("variant"), (stack, world, entity, seed) -> stack.getHoverName().getString().toLowerCase().contains("faith") ? 1 : 0);
			ItemProperties.register(BWObjects.NAZAR.get(), Bewitchment.id("variant"), (stack, world, entity, seed) -> stack.hasTag() && stack.getTag().getBoolean("Worn") ? 1 : 0);
			ItemProperties.register(BWObjects.PRICKLY_BELT.get(), Bewitchment.id("variant"), (stack, world, entity, seed) -> stack.hasTag() && stack.getTag().getInt("PotionUses") > 0 ? 1 : 0);
			ItemProperties.register(BWObjects.HORNED_SPEAR.get(), Bewitchment.id("variant"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1 : 0);
			ItemProperties.register(BWObjects.TAGLOCK.get(), Bewitchment.id("variant"), (stack, world, entity, seed) -> TaglockItem.hasTaglock(stack) ? 1 : 0);
			ItemProperties.register(BWObjects.WAYSTONE.get(), Bewitchment.id("variant"), (stack, world, entity, seed) -> stack.hasTag() && stack.getOrCreateTag().contains("LocationPos") ? 1 : 0);
			MenuScreens.register(BWScreenHandlerTypes.DEMON_SCREEN_HANDLER.get(), DemonScreen::new);
			MenuScreens.register(BWScreenHandlerTypes.BAPHOMET_SCREEN_HANDLER.get(), DemonScreen::new);
			Sheets.addWoodType(BWObjects.JUNIPER_WOOD_TYPE);
			Sheets.addWoodType(BWObjects.CYPRESS_WOOD_TYPE);
			Sheets.addWoodType(BWObjects.ELDER_WOOD_TYPE);
			Sheets.addWoodType(BWObjects.DRAGONS_BLOOD_WOOD_TYPE);
			// Accessory renderers for Curios slots (were Trinkets renderers).
			CuriosRendererRegistry.register(BWObjects.NAZAR.get(), NazarRenderer::new);
			CuriosRendererRegistry.register(BWObjects.SPECTER_BANGLE.get(), SpecterBangleRenderer::new);
			CuriosRendererRegistry.register(BWObjects.PRICKLY_BELT.get(), PricklyBeltRenderer::new);
			CuriosRendererRegistry.register(BWObjects.HELLISH_BAUBLE.get(), HellishBaubleRenderer::new);
			CuriosRendererRegistry.register(BWObjects.DRUID_BAND.get(), DruidBandRenderer::new);
			CuriosRendererRegistry.register(BWObjects.ZEPHYR_HARNESS.get(), ZephyrHarnessRenderer::new);
		});
	}

	private static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(TRANSFORMATION_ABILITY);
	}

	/** Was Fabric's ParticleFactoryRegistry. */
	private static void onRegisterParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(BWParticleTypes.CAULDRON_BUBBLE.get(), CauldronBubbleParticle.Factory::new);
		event.registerSpriteSet(BWParticleTypes.INCENSE_SMOKE.get(), IncenseSmokeParticle.Factory::new);
	}

	/** Was ColorProviderRegistry.BLOCK. */
	private static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
		event.register((state, world, pos, tintIndex) -> 0xffff00, BWObjects.GOLDEN_GLYPH.get());
		event.register((state, world, pos, tintIndex) -> 0xc00000, BWObjects.FIERY_GLYPH.get());
		event.register((state, world, pos, tintIndex) -> 0x8000a0, BWObjects.ELDRITCH_GLYPH.get());
		event.register((state, world, pos, tintIndex) -> tintIndex == 1 ? ((BedBlock) state.getBlock()).getColor().getFireworkColor() : 0xffffff, BWObjects.WHITE_COFFIN.get(), BWObjects.ORANGE_COFFIN.get(), BWObjects.MAGENTA_COFFIN.get(), BWObjects.LIGHT_BLUE_COFFIN.get(), BWObjects.YELLOW_COFFIN.get(), BWObjects.LIME_COFFIN.get(), BWObjects.PINK_COFFIN.get(), BWObjects.GRAY_COFFIN.get(), BWObjects.LIGHT_GRAY_COFFIN.get(), BWObjects.CYAN_COFFIN.get(), BWObjects.PURPLE_COFFIN.get(), BWObjects.BLUE_COFFIN.get(), BWObjects.BROWN_COFFIN.get(), BWObjects.GREEN_COFFIN.get(), BWObjects.RED_COFFIN.get(), BWObjects.BLACK_COFFIN.get());
	}

	/** Was ColorProviderRegistry.ITEM. */
	private static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
		event.register((stack, tintIndex) -> tintIndex == 1 ? ((BedBlock) Block.byItem(stack.getItem())).getColor().getFireworkColor() : 0xffffff, BWObjects.WHITE_COFFIN.get(), BWObjects.ORANGE_COFFIN.get(), BWObjects.MAGENTA_COFFIN.get(), BWObjects.LIGHT_BLUE_COFFIN.get(), BWObjects.YELLOW_COFFIN.get(), BWObjects.LIME_COFFIN.get(), BWObjects.PINK_COFFIN.get(), BWObjects.GRAY_COFFIN.get(), BWObjects.LIGHT_GRAY_COFFIN.get(), BWObjects.CYAN_COFFIN.get(), BWObjects.PURPLE_COFFIN.get(), BWObjects.BLUE_COFFIN.get(), BWObjects.BROWN_COFFIN.get(), BWObjects.GREEN_COFFIN.get(), BWObjects.RED_COFFIN.get(), BWObjects.BLACK_COFFIN.get());
		event.register((stack, tintIndex) -> tintIndex == 0 ? 0x7f0000 : 0xffffff, BWObjects.BOTTLE_OF_BLOOD.get());
	}

	/** Was BlockEntityRenderers / EntityRendererRegistry. Boat renderers are added in the boats step. */
	private static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(BWBlockEntityTypes.BW_CHEST.get(), ChestRenderer::new);
		event.registerBlockEntityRenderer(BWBlockEntityTypes.WITCH_ALTAR.get(), ctx -> new WitchAltarBlockEntityRenderer());
		event.registerBlockEntityRenderer(BWBlockEntityTypes.WITCH_CAULDRON.get(), ctx -> new WitchCauldronBlockEntityRenderer());
		event.registerBlockEntityRenderer(BWBlockEntityTypes.BRAZIER.get(), ctx -> new BrazierBlockEntityRenderer());
		event.registerBlockEntityRenderer(BWBlockEntityTypes.POPPET_SHELF.get(), ctx -> new PoppetShelfBlockEntityRenderer());
		event.registerBlockEntityRenderer(BWBlockEntityTypes.JUNIPER_CHEST.get(), ChestRenderer::new);
		event.registerBlockEntityRenderer(BWBlockEntityTypes.ELDER_CHEST.get(), ChestRenderer::new);
		event.registerBlockEntityRenderer(BWBlockEntityTypes.DRAGONS_BLOOD_CHEST.get(), ChestRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.JUNIPER_BROOM.get(), JuniperBroomEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.CYPRESS_BROOM.get(), CypressBroomEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.ELDER_BROOM.get(), ElderBroomEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.DRAGONS_BLOOD_BROOM.get(), DragonsBloodBroomEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.SILVER_ARROW.get(), SilverArrowEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.HORNED_SPEAR.get(), HornedSpearEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.OWL.get(), OwlEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.RAVEN.get(), RavenEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.SNAKE.get(), SnakeEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.TOAD.get(), ToadEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.GHOST.get(), GhostEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.VAMPIRE.get(), VampireEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.WEREWOLF.get(), WerewolfEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.HELLHOUND.get(), HellhoundEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.DEMON.get(), DemonEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.LEONARD.get(), LeonardEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.BAPHOMET.get(), BaphometEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.LILITH.get(), LilithEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.HERNE.get(), HerneEntityRenderer::new);
	}

	/** Was EntityModelLayerRegistry. Block render layers moved to "render_type" in the block models;
	 * chest items render through BWChestItem and witch armor through WitchArmorItem. */
	private static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(CONTRIBUTOR_HORNS_MODEL_LAYER, ContributorHornsModel::getTexturedModelData);
		event.registerLayerDefinition(WITCH_ARMOR_MODEL_LAYER, WitchArmorModel::getTexturedModelData);
		event.registerLayerDefinition(SPECTER_BANGLE_MODEL_LAYER, SpecterBangleModel::getTexturedModelData);
		event.registerLayerDefinition(PRICKLY_BELT_MODEL_LAYER, PricklyBeltModel::getTexturedModelData);
		event.registerLayerDefinition(DRUID_BAND_MODEL_LAYER, DruidBandModel::getTexturedModelData);
		event.registerLayerDefinition(ZEPHYR_HARNESS_MODEL_LAYER, ZephyrHarnessModel::getTexturedModelData);
		event.registerLayerDefinition(BROOM_MODEL_LAYER, BroomEntityModel::getTexturedModelData);
		event.registerLayerDefinition(OWL_MODEL_LAYER, OwlEntityModel::getTexturedModelData);
		event.registerLayerDefinition(RAVEN_MODEL_LAYER, RavenEntityModel::getTexturedModelData);
		event.registerLayerDefinition(SNAKE_MODEL_LAYER, SnakeEntityModel::getTexturedModelData);
		event.registerLayerDefinition(TOAD_MODEL_LAYER, ToadEntityModel::getTexturedModelData);
		event.registerLayerDefinition(GHOST_MODEL_LAYER, GhostEntityModel::getTexturedModelData);
		event.registerLayerDefinition(VAMPIRE_MODEL_LAYER, VampireEntityModel::getTexturedModelData);
		event.registerLayerDefinition(WEREWOLF_MODEL_LAYER, WerewolfEntityModel::getTexturedModelData);
		event.registerLayerDefinition(HELLHOUND_MODEL_LAYER, HellhoundEntityModel::getTexturedModelData);
		event.registerLayerDefinition(MALE_DEMON_MODEL_LAYER, DemonEntityModel::getTexturedModelDataMale);
		event.registerLayerDefinition(FEMALE_DEMON_MODEL_LAYER, DemonEntityModel::getTexturedModelDataFemale);
		event.registerLayerDefinition(LEONARD_MODEL_LAYER, LeonardEntityModel::getTexturedModelData);
		event.registerLayerDefinition(BAPHOMET_MODEL_LAYER, BaphometEntityModel::getTexturedModelData);
		event.registerLayerDefinition(LILITH_MODEL_LAYER, LilithEntityModel::getTexturedModelData);
		event.registerLayerDefinition(HERNE_MODEL_LAYER, HerneEntityModel::getTexturedModelData);
	}

	public static ModelPart getPart(ModelLayerLocation layer) {
		return Minecraft.getInstance().getEntityModels().bakeLayer(layer);
	}
}
