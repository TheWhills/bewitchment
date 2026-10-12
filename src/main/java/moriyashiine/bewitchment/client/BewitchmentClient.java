/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.terraformersmc.terraform.boat.api.client.TerraformBoatClientHelper;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import moriyashiine.bewitchment.api.client.model.BroomEntityModel;
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
import moriyashiine.bewitchment.client.render.WitchArmorRenderer;
import moriyashiine.bewitchment.client.render.blockentity.BrazierBlockEntityRenderer;
import moriyashiine.bewitchment.client.render.blockentity.PoppetShelfBlockEntityRenderer;
import moriyashiine.bewitchment.client.render.blockentity.WitchAltarBlockEntityRenderer;
import moriyashiine.bewitchment.client.render.blockentity.WitchCauldronBlockEntityRenderer;
import moriyashiine.bewitchment.client.render.entity.*;
import moriyashiine.bewitchment.client.render.entity.living.*;
import moriyashiine.bewitchment.client.render.trinket.*;
import moriyashiine.bewitchment.client.screen.DemonScreen;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.BWChestBlockEntity;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.registry.*;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import org.lwjgl.glfw.GLFW;

@SuppressWarnings({"unchecked", "ConstantConditions"})
public class BewitchmentClient {
	public static void init() {
		new BewitchmentClient().onInitializeClient();
	}

	public static final KeyMapping TRANSFORMATION_ABILITY = KeyBindingHelper.registerKeyBinding(new KeyMapping("key." + Bewitchment.MOD_ID + ".transformation_ability", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, "itemGroup." + Bewitchment.MOD_ID));

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

	// TODO(forge-port): move to FMLClientSetupEvent / RegisterRenderers etc.
	private void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(SyncContractsPacket.ID, new SyncContractsPacket.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncDemonTradesPacket.ID, new SyncDemonTradesPacket.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncPoppetShelfPacket.ID, new SyncPoppetShelfPacket.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SyncHornedSpearPacket.ID, new SyncHornedSpearPacket.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SpawnSmokeParticlesPacket.ID, new SpawnSmokeParticlesPacket.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SpawnPortalParticlesPacket.ID, new SpawnPortalParticlesPacket.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SpawnExplosionParticlesPacket.ID, new SpawnExplosionParticlesPacket.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(SpawnSpecterBangleParticlesPacket.ID, new SpawnSpecterBangleParticlesPacket.Receiver());
		ClientTickEvents.END_WORLD_TICK.register(new TransformationAbilityEvent());
		ClientSendMessageEvents.ALLOW_CHAT.register(new CauldronTeleportEvent());
		ParticleFactoryRegistry.getInstance().register(BWParticleTypes.CAULDRON_BUBBLE.get(), CauldronBubbleParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(BWParticleTypes.INCENSE_SMOKE.get(), IncenseSmokeParticle.Factory::new);
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> 0xffff00, BWObjects.GOLDEN_GLYPH.get());
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> 0xc00000, BWObjects.FIERY_GLYPH.get());
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> 0x8000a0, BWObjects.ELDRITCH_GLYPH.get());
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> tintIndex == 1 ? ((BedBlock) state.getBlock()).getColor().getFireworkColor() : 0xffffff, BWObjects.WHITE_COFFIN.get(), BWObjects.ORANGE_COFFIN.get(), BWObjects.MAGENTA_COFFIN.get(), BWObjects.LIGHT_BLUE_COFFIN.get(), BWObjects.YELLOW_COFFIN.get(), BWObjects.LIME_COFFIN.get(), BWObjects.PINK_COFFIN.get(), BWObjects.GRAY_COFFIN.get(), BWObjects.LIGHT_GRAY_COFFIN.get(), BWObjects.CYAN_COFFIN.get(), BWObjects.PURPLE_COFFIN.get(), BWObjects.BLUE_COFFIN.get(), BWObjects.BROWN_COFFIN.get(), BWObjects.GREEN_COFFIN.get(), BWObjects.RED_COFFIN.get(), BWObjects.BLACK_COFFIN.get());
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 1 ? ((BedBlock) Block.byItem(stack.getItem())).getColor().getFireworkColor() : 0xffffff, BWObjects.WHITE_COFFIN.get(), BWObjects.ORANGE_COFFIN.get(), BWObjects.MAGENTA_COFFIN.get(), BWObjects.LIGHT_BLUE_COFFIN.get(), BWObjects.YELLOW_COFFIN.get(), BWObjects.LIME_COFFIN.get(), BWObjects.PINK_COFFIN.get(), BWObjects.GRAY_COFFIN.get(), BWObjects.LIGHT_GRAY_COFFIN.get(), BWObjects.CYAN_COFFIN.get(), BWObjects.PURPLE_COFFIN.get(), BWObjects.BLUE_COFFIN.get(), BWObjects.BROWN_COFFIN.get(), BWObjects.GREEN_COFFIN.get(), BWObjects.RED_COFFIN.get(), BWObjects.BLACK_COFFIN.get());
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 0 ? 0x7f0000 : 0xffffff, BWObjects.BOTTLE_OF_BLOOD.get());
		ItemProperties.register(BWObjects.HEDGEWITCH_HAT.get(), Bewitchment.id("variant"), (stack, world, entity, seed) -> stack.getHoverName().getString().toLowerCase().contains("faith") ? 1 : 0);
		ItemProperties.register(BWObjects.ALCHEMIST_HAT.get(), Bewitchment.id("variant"), (stack, world, entity, seed) -> stack.getHoverName().getString().toLowerCase().contains("faith") ? 1 : 0);
		ItemProperties.register(BWObjects.BESMIRCHED_HAT.get(), Bewitchment.id("variant"), (stack, world, entity, seed) -> stack.getHoverName().getString().toLowerCase().contains("faith") ? 1 : 0);
		ItemProperties.register(BWObjects.NAZAR.get(), Bewitchment.id("variant"), (stack, world, entity, seed) -> stack.hasTag() && stack.getTag().getBoolean("Worn") ? 1 : 0);
		ItemProperties.register(BWObjects.PRICKLY_BELT.get(), Bewitchment.id("variant"), (stack, world, entity, seed) -> stack.hasTag() && stack.getTag().getInt("PotionUses") > 0 ? 1 : 0);
		ItemProperties.register(BWObjects.HORNED_SPEAR.get(), Bewitchment.id("variant"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1 : 0);
		ItemProperties.register(BWObjects.TAGLOCK.get(), Bewitchment.id("variant"), (stack, world, entity, seed) -> TaglockItem.hasTaglock(stack) ? 1 : 0);
		ItemProperties.register(BWObjects.WAYSTONE.get(), Bewitchment.id("variant"), (stack, world, entity, seed) -> stack.hasTag() && stack.getOrCreateTag().contains("LocationPos") ? 1 : 0);
		BlockEntityRenderers.register(BWBlockEntityTypes.BW_CHEST.get(), ChestRenderer::new);
		BlockEntityRenderers.register(BWBlockEntityTypes.WITCH_ALTAR.get(), ctx -> new WitchAltarBlockEntityRenderer());
		BlockEntityRenderers.register(BWBlockEntityTypes.WITCH_CAULDRON.get(), ctx -> new WitchCauldronBlockEntityRenderer());
		BlockEntityRenderers.register(BWBlockEntityTypes.BRAZIER.get(), ctx -> new BrazierBlockEntityRenderer());
		BlockEntityRenderers.register(BWBlockEntityTypes.POPPET_SHELF.get(), ctx -> new PoppetShelfBlockEntityRenderer());
		BlockEntityRenderers.register(BWBlockEntityTypes.JUNIPER_CHEST.get(), ChestRenderer::new);
		BlockEntityRenderers.register(BWBlockEntityTypes.ELDER_CHEST.get(), ChestRenderer::new);
		BlockEntityRenderers.register(BWBlockEntityTypes.DRAGONS_BLOOD_CHEST.get(), ChestRenderer::new);
		TerraformBoatClientHelper.registerModelLayers(Bewitchment.id("juniper_boat"), false);
		TerraformBoatClientHelper.registerModelLayers(Bewitchment.id("cypress_boat"), false);
		TerraformBoatClientHelper.registerModelLayers(Bewitchment.id("elder_boat"), false);
		TerraformBoatClientHelper.registerModelLayers(Bewitchment.id("dragons_blood_boat"), false);
		EntityModelLayerRegistry.registerModelLayer(CONTRIBUTOR_HORNS_MODEL_LAYER, ContributorHornsModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(WITCH_ARMOR_MODEL_LAYER, WitchArmorModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SPECTER_BANGLE_MODEL_LAYER, SpecterBangleModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(PRICKLY_BELT_MODEL_LAYER, PricklyBeltModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(DRUID_BAND_MODEL_LAYER, DruidBandModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ZEPHYR_HARNESS_MODEL_LAYER, ZephyrHarnessModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(BROOM_MODEL_LAYER, BroomEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.JUNIPER_BROOM.get(), JuniperBroomEntityRenderer::new);
		EntityRendererRegistry.register(BWEntityTypes.CYPRESS_BROOM.get(), CypressBroomEntityRenderer::new);
		EntityRendererRegistry.register(BWEntityTypes.ELDER_BROOM.get(), ElderBroomEntityRenderer::new);
		EntityRendererRegistry.register(BWEntityTypes.DRAGONS_BLOOD_BROOM.get(), DragonsBloodBroomEntityRenderer::new);
		EntityRendererRegistry.register(BWEntityTypes.SILVER_ARROW.get(), SilverArrowEntityRenderer::new);
		EntityRendererRegistry.register(BWEntityTypes.HORNED_SPEAR.get(), HornedSpearEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(OWL_MODEL_LAYER, OwlEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.OWL.get(), OwlEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(RAVEN_MODEL_LAYER, RavenEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.RAVEN.get(), RavenEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(SNAKE_MODEL_LAYER, SnakeEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.SNAKE.get(), SnakeEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(TOAD_MODEL_LAYER, ToadEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.TOAD.get(), ToadEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(GHOST_MODEL_LAYER, GhostEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.GHOST.get(), GhostEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(VAMPIRE_MODEL_LAYER, VampireEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.VAMPIRE.get(), VampireEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(WEREWOLF_MODEL_LAYER, WerewolfEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.WEREWOLF.get(), WerewolfEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(HELLHOUND_MODEL_LAYER, HellhoundEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.HELLHOUND.get(), HellhoundEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(MALE_DEMON_MODEL_LAYER, DemonEntityModel::getTexturedModelDataMale);
		EntityModelLayerRegistry.registerModelLayer(FEMALE_DEMON_MODEL_LAYER, DemonEntityModel::getTexturedModelDataFemale);
		EntityRendererRegistry.register(BWEntityTypes.DEMON.get(), DemonEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(LEONARD_MODEL_LAYER, LeonardEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.LEONARD.get(), LeonardEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(BAPHOMET_MODEL_LAYER, BaphometEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.BAPHOMET.get(), BaphometEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(LILITH_MODEL_LAYER, LilithEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.LILITH.get(), LilithEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(HERNE_MODEL_LAYER, HerneEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(BWEntityTypes.HERNE.get(), HerneEntityRenderer::new);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), BWObjects.SALT_LINE.get(), BWObjects.TEMPORARY_COBWEB.get(), BWObjects.GLYPH.get(), BWObjects.GOLDEN_GLYPH.get(), BWObjects.FIERY_GLYPH.get(), BWObjects.ELDRITCH_GLYPH.get(), BWObjects.SIGIL.get());
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), BWObjects.ACONITE_CROP.get(), BWObjects.BELLADONNA_CROP.get(), BWObjects.GARLIC_CROP.get(), BWObjects.MANDRAKE_CROP.get());
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), BWObjects.JUNIPER_SAPLING.get(), BWObjects.POTTED_JUNIPER_SAPLING.get(), BWObjects.JUNIPER_DOOR.get(), BWObjects.JUNIPER_TRAPDOOR.get());
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), BWObjects.CYPRESS_SAPLING.get(), BWObjects.POTTED_CYPRESS_SAPLING.get(), BWObjects.CYPRESS_DOOR.get(), BWObjects.CYPRESS_TRAPDOOR.get());
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), BWObjects.ELDER_SAPLING.get(), BWObjects.POTTED_ELDER_SAPLING.get(), BWObjects.ELDER_DOOR.get(), BWObjects.ELDER_TRAPDOOR.get());
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), BWObjects.DRAGONS_BLOOD_SAPLING.get(), BWObjects.POTTED_DRAGONS_BLOOD_SAPLING.get(), BWObjects.DRAGONS_BLOOD_DOOR.get(), BWObjects.DRAGONS_BLOOD_TRAPDOOR.get());
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), BWObjects.GLOWING_BRAMBLE.get(), BWObjects.ENDER_BRAMBLE.get(), BWObjects.FRUITING_BRAMBLE.get(), BWObjects.SCORCHED_BRAMBLE.get(), BWObjects.THICK_BRAMBLE.get(), BWObjects.FLEETING_BRAMBLE.get());
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), BWObjects.STONE_WITCH_ALTAR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), BWObjects.MOSSY_COBBLESTONE_WITCH_ALTAR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), BWObjects.PRISMARINE_WITCH_ALTAR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), BWObjects.NETHER_BRICK_WITCH_ALTAR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), BWObjects.BLACKSTONE_WITCH_ALTAR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), BWObjects.GOLDEN_WITCH_ALTAR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), BWObjects.END_STONE_WITCH_ALTAR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), BWObjects.OBSIDIAN_WITCH_ALTAR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), BWObjects.PURPUR_WITCH_ALTAR);
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.translucent(), BWObjects.CRYSTAL_BALL.get());
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), BWObjects.BRAZIER.get());
		MenuScreens.register(BWScreenHandlerTypes.DEMON_SCREEN_HANDLER.get(), DemonScreen::new);
		MenuScreens.register(BWScreenHandlerTypes.BAPHOMET_SCREEN_HANDLER.get(), DemonScreen::new);
// Sign wood types; chest and sign textures are picked up by the vanilla atlases (entity/chest, entity/signs).
		Sheets.addWoodType(BWObjects.JUNIPER_WOOD_TYPE);
		Sheets.addWoodType(BWObjects.CYPRESS_WOOD_TYPE);
		Sheets.addWoodType(BWObjects.ELDER_WOOD_TYPE);
		Sheets.addWoodType(BWObjects.DRAGONS_BLOOD_WOOD_TYPE);
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.JUNIPER_CHEST.get(), (stack, mode, matrices, vertexConsumers, light, overlay) -> Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST.get(), BlockPos.ZERO, BWObjects.JUNIPER_CHEST.get().defaultBlockState(), BWChestBlockEntity.Type.JUNIPER, false), matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.TRAPPED_JUNIPER_CHEST.get(), (stack, mode, matrices, vertexConsumers, light, overlay) -> Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST.get(), BlockPos.ZERO, BWObjects.TRAPPED_JUNIPER_CHEST.get().defaultBlockState(), BWChestBlockEntity.Type.JUNIPER, true), matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.CYPRESS_CHEST.get(), (stack, mode, matrices, vertexConsumers, light, overlay) -> Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST.get(), BlockPos.ZERO, BWObjects.CYPRESS_CHEST.get().defaultBlockState(), BWChestBlockEntity.Type.CYPRESS, false), matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.TRAPPED_CYPRESS_CHEST.get(), (stack, mode, matrices, vertexConsumers, light, overlay) -> Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST.get(), BlockPos.ZERO, BWObjects.TRAPPED_CYPRESS_CHEST.get().defaultBlockState(), BWChestBlockEntity.Type.CYPRESS, true), matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.ELDER_CHEST.get(), (stack, mode, matrices, vertexConsumers, light, overlay) -> Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST.get(), BlockPos.ZERO, BWObjects.ELDER_CHEST.get().defaultBlockState(), BWChestBlockEntity.Type.ELDER, false), matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.TRAPPED_ELDER_CHEST.get(), (stack, mode, matrices, vertexConsumers, light, overlay) -> Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST.get(), BlockPos.ZERO, BWObjects.TRAPPED_ELDER_CHEST.get().defaultBlockState(), BWChestBlockEntity.Type.ELDER, true), matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.DRAGONS_BLOOD_CHEST.get(), (stack, mode, matrices, vertexConsumers, light, overlay) -> Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST.get(), BlockPos.ZERO, BWObjects.DRAGONS_BLOOD_CHEST.get().defaultBlockState(), BWChestBlockEntity.Type.DRAGONS_BLOOD, false), matrices, vertexConsumers, light, overlay));
		BuiltinItemRendererRegistry.INSTANCE.register(BWObjects.TRAPPED_DRAGONS_BLOOD_CHEST.get(), (stack, mode, matrices, vertexConsumers, light, overlay) -> Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(new BWChestBlockEntity(BWBlockEntityTypes.BW_CHEST.get(), BlockPos.ZERO, BWObjects.TRAPPED_DRAGONS_BLOOD_CHEST.get().defaultBlockState(), BWChestBlockEntity.Type.DRAGONS_BLOOD, true), matrices, vertexConsumers, light, overlay));
		ArmorRenderer.register(new WitchArmorRenderer(Bewitchment.id("textures/entity/armor/hedgewitch.png"), BWObjects.HEDGEWITCH_HAT.get()), BWObjects.HEDGEWITCH_HOOD.get(), BWObjects.HEDGEWITCH_HAT.get(), BWObjects.HEDGEWITCH_ROBES.get(), BWObjects.HEDGEWITCH_PANTS.get());
		ArmorRenderer.register(new WitchArmorRenderer(Bewitchment.id("textures/entity/armor/alchemist.png"), BWObjects.ALCHEMIST_HAT.get()), BWObjects.ALCHEMIST_HOOD.get(), BWObjects.ALCHEMIST_HAT.get(), BWObjects.ALCHEMIST_ROBES.get(), BWObjects.ALCHEMIST_PANTS.get());
		ArmorRenderer.register(new WitchArmorRenderer(Bewitchment.id("textures/entity/armor/besmirched.png"), BWObjects.BESMIRCHED_HAT.get()), BWObjects.BESMIRCHED_HOOD.get(), BWObjects.BESMIRCHED_HAT.get(), BWObjects.BESMIRCHED_ROBES.get(), BWObjects.BESMIRCHED_PANTS.get());
		ArmorRenderer.register(new WitchArmorRenderer(Bewitchment.id("textures/entity/armor/harbinger.png"), null), BWObjects.HARBINGER.get());
		TrinketRendererRegistry.registerRenderer(BWObjects.NAZAR.get(), new NazarRenderer());
		TrinketRendererRegistry.registerRenderer(BWObjects.SPECTER_BANGLE.get(), new SpecterBangleRenderer());
		TrinketRendererRegistry.registerRenderer(BWObjects.PRICKLY_BELT.get(), new PricklyBeltRenderer());
		TrinketRendererRegistry.registerRenderer(BWObjects.HELLISH_BAUBLE.get(), new HellishBaubleRenderer());
		TrinketRendererRegistry.registerRenderer(BWObjects.DRUID_BAND.get(), new DruidBandRenderer());
		TrinketRendererRegistry.registerRenderer(BWObjects.ZEPHYR_HARNESS.get(), new ZephyrHarnessRenderer());
	}

	public static ModelPart getPart(ModelLayerLocation layer) {
		return Minecraft.getInstance().getEntityModels().bakeLayer(layer);
	}
}
