/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.block.entity.BWChestBlockEntity;
import moriyashiine.bewitchment.api.block.CandelabraBlock;
import moriyashiine.bewitchment.api.block.PoppetShelfBlock;
import moriyashiine.bewitchment.api.block.WitchAltarBlock;
import moriyashiine.bewitchment.api.item.BroomItem;
import moriyashiine.bewitchment.api.item.PoppetItem;
import moriyashiine.bewitchment.api.item.SigilItem;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.*;
import moriyashiine.bewitchment.common.block.dragonsblood.*;
import moriyashiine.bewitchment.common.block.elder.*;
import moriyashiine.bewitchment.common.block.juniper.*;
import moriyashiine.bewitchment.common.block.util.BWCropBlock;
import moriyashiine.bewitchment.common.block.util.BWSaplingBlock;
import moriyashiine.bewitchment.common.item.*;
import moriyashiine.bewitchment.common.item.util.BWBookItem;
import moriyashiine.bewitchment.common.world.generator.tree.generator.CypressSaplingGenerator;
import moriyashiine.bewitchment.common.world.generator.tree.generator.DragonsBloodSaplingGenerator;
import moriyashiine.bewitchment.common.world.generator.tree.generator.ElderSaplingGenerator;
import moriyashiine.bewitchment.common.world.generator.tree.generator.JuniperSaplingGenerator;
import moriyashiine.bewitchment.mixin.BlocksAccessor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SignItem;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class BWObjects {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Bewitchment.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Bewitchment.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Bewitchment.MOD_ID);
	/** Boat items in creative-tab order; filled by {@link BWBoatTypes}. */
	public static final List<RegistryObject<? extends Item>> BOATS = new ArrayList<>();

	public static final WoodType JUNIPER_WOOD_TYPE = WoodType.register(new WoodType(Bewitchment.MOD_ID + ":juniper", BlockSetType.OAK));
	public static final WoodType CYPRESS_WOOD_TYPE = WoodType.register(new WoodType(Bewitchment.MOD_ID + ":cypress", BlockSetType.OAK));
	public static final WoodType ELDER_WOOD_TYPE = WoodType.register(new WoodType(Bewitchment.MOD_ID + ":elder", BlockSetType.OAK));
	public static final WoodType DRAGONS_BLOOD_WOOD_TYPE = WoodType.register(new WoodType(Bewitchment.MOD_ID + ":dragons_blood", BlockSetType.OAK));

	//misc_no_item
	public static final RegistryObject<Block> SALT_LINE = create("salt_line", () -> new SaltLineBlock(copyOf(Blocks.REDSTONE_WIRE)), false);
	public static final RegistryObject<Block> TEMPORARY_COBWEB = create("temporary_cobweb", () -> new WebBlock(copyOf(Blocks.COBWEB).randomTicks()) {
		@Override
		public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
			world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
	}, false);
	public static final RegistryObject<Block> GOLDEN_GLYPH = create("golden_glyph", () -> new GlyphBlock(BlockBehaviour.Properties.of().sound(new ForgeSoundType(1, 1, () -> SoundEvents.STONE_BREAK, () -> SoundEvents.STONE_STEP, BWSoundEvents.BLOCK_GLPYH_PLACE, () -> SoundEvents.STONE_HIT, () -> SoundEvents.STONE_FALL)).pushReaction(PushReaction.DESTROY).noCollission().strength(1, 0)), false);
	public static final RegistryObject<Block> GLYPH = create("glyph", () -> new GlyphBlock(copyOf(GOLDEN_GLYPH.get())), false);
	public static final RegistryObject<Block> FIERY_GLYPH = create("fiery_glyph", () -> new GlyphBlock(copyOf(GOLDEN_GLYPH.get()).lightLevel(state -> 4)), false);
	public static final RegistryObject<Block> ELDRITCH_GLYPH = create("eldritch_glyph", () -> new GlyphBlock(copyOf(GOLDEN_GLYPH.get()).lightLevel(state -> 2)), false);
	public static final RegistryObject<Block> SIGIL = create("sigil", () -> new SigilBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).sound(SoundType.BAMBOO).noCollission().instabreak()), false);
	//crop
	public static final RegistryObject<Block> ACONITE_CROP = create("aconite", () -> new BWCropBlock(copyOf(Blocks.WHEAT)), false);
	public static final RegistryObject<Block> BELLADONNA_CROP = create("belladonna", () -> new BWCropBlock(copyOf(ACONITE_CROP.get())), false);
	public static final RegistryObject<Block> GARLIC_CROP = create("garlic", () -> new BWCropBlock(copyOf(ACONITE_CROP.get())), false);
	public static final RegistryObject<Block> MANDRAKE_CROP = create("mandrake", () -> new BWCropBlock(copyOf(ACONITE_CROP.get())), false);
	//juniper
	public static final RegistryObject<Block> JUNIPER_LOG = create("juniper_log", () -> new RotatedPillarBlock(copyOf(Blocks.OAK_LOG)), true);
	public static final RegistryObject<Block> STRIPPED_JUNIPER_LOG = create("stripped_juniper_log", () -> new RotatedPillarBlock(copyOf(JUNIPER_LOG.get())), true);
	public static final RegistryObject<Block> JUNIPER_WOOD = create("juniper_wood", () -> new RotatedPillarBlock(copyOf(JUNIPER_LOG.get())), true);
	public static final RegistryObject<Block> STRIPPED_JUNIPER_WOOD = create("stripped_juniper_wood", () -> new RotatedPillarBlock(copyOf(JUNIPER_LOG.get())), true);
	public static final RegistryObject<Block> JUNIPER_LEAVES = create("juniper_leaves", () -> BlocksAccessor.callCreateLeavesBlock(SoundType.GRASS), true);
	public static final RegistryObject<Block> JUNIPER_SAPLING = create("juniper_sapling", () -> new BWSaplingBlock(new JuniperSaplingGenerator(), copyOf(Blocks.OAK_SAPLING)), true);
	public static final RegistryObject<Block> POTTED_JUNIPER_SAPLING = create("potted_juniper_sapling", () -> new FlowerPotBlock(JUNIPER_SAPLING.get(), copyOf(Blocks.POTTED_OAK_SAPLING)), false);
	public static final RegistryObject<Block> JUNIPER_PLANKS = create("juniper_planks", () -> new Block(copyOf(Blocks.OAK_PLANKS)), true);
	public static final RegistryObject<Block> JUNIPER_STAIRS = create("juniper_stairs", () -> new StairBlock(JUNIPER_PLANKS.get().defaultBlockState(), copyOf(Blocks.OAK_STAIRS)), true);
	public static final RegistryObject<Block> JUNIPER_SLAB = create("juniper_slab", () -> new SlabBlock(copyOf(Blocks.OAK_SLAB)), true);
	public static final RegistryObject<Block> JUNIPER_FENCE = create("juniper_fence", () -> new FenceBlock(copyOf(Blocks.OAK_FENCE)), true);
	public static final RegistryObject<Block> JUNIPER_FENCE_GATE = create("juniper_fence_gate", () -> new JuniperFenceGateBlock(copyOf(Blocks.OAK_FENCE_GATE)), true);
	public static final RegistryObject<Block> JUNIPER_PRESSURE_PLATE = create("juniper_pressure_plate", () -> new JuniperPressurePlateBlock(copyOf(Blocks.OAK_PRESSURE_PLATE)), true);
	public static final RegistryObject<Block> JUNIPER_BUTTON = create("juniper_button", () -> new JuniperButtonBlock(copyOf(Blocks.OAK_BUTTON)), true);
	public static final RegistryObject<Block> JUNIPER_TRAPDOOR = create("juniper_trapdoor", () -> new JuniperTrapdoorBlock(copyOf(Blocks.OAK_TRAPDOOR)), true);
	public static final RegistryObject<Block> JUNIPER_DOOR = create("juniper_door", () -> new JuniperDoorBlock(copyOf(Blocks.OAK_DOOR)), false);
	public static final RegistryObject<Item> JUNIPER_DOOR_ITEM = create("juniper_door", () -> new DoubleHighBlockItem(JUNIPER_DOOR.get(), gen()));
	public static final RegistryObject<Block> JUNIPER_CHEST = create("juniper_chest", () -> new JuniperChestBlock(copyOf(Blocks.CHEST), () -> BWBlockEntityTypes.JUNIPER_CHEST.get(), false), false);
	public static final RegistryObject<Item> JUNIPER_CHEST_ITEM = create("juniper_chest", () -> new BWChestItem(JUNIPER_CHEST.get(), gen(), BWChestBlockEntity.Type.JUNIPER, false));
	public static final RegistryObject<Block> TRAPPED_JUNIPER_CHEST = create("trapped_juniper_chest", () -> new JuniperChestBlock(copyOf(Blocks.CHEST), () -> BWBlockEntityTypes.JUNIPER_CHEST.get(), true), false);
	public static final RegistryObject<Item> TRAPPED_JUNIPER_CHEST_ITEM = create("trapped_juniper_chest", () -> new BWChestItem(TRAPPED_JUNIPER_CHEST.get(), gen(), BWChestBlockEntity.Type.JUNIPER, true));
	public static final RegistryObject<Block> JUNIPER_SIGN = createSign("juniper", JUNIPER_WOOD_TYPE);
	//cypress
	public static final RegistryObject<Block> CYPRESS_LOG = create("cypress_log", () -> new RotatedPillarBlock(copyOf(JUNIPER_LOG.get())), true);
	public static final RegistryObject<Block> STRIPPED_CYPRESS_LOG = create("stripped_cypress_log", () -> new RotatedPillarBlock(copyOf(JUNIPER_LOG.get())), true);
	public static final RegistryObject<Block> CYPRESS_WOOD = create("cypress_wood", () -> new RotatedPillarBlock(copyOf(JUNIPER_LOG.get())), true);
	public static final RegistryObject<Block> STRIPPED_CYPRESS_WOOD = create("stripped_cypress_wood", () -> new RotatedPillarBlock(copyOf(JUNIPER_LOG.get())), true);
	public static final RegistryObject<Block> CYPRESS_LEAVES = create("cypress_leaves", () -> BlocksAccessor.callCreateLeavesBlock(SoundType.GRASS), true);
	public static final RegistryObject<Block> CYPRESS_SAPLING = create("cypress_sapling", () -> new BWSaplingBlock(new CypressSaplingGenerator(), copyOf(JUNIPER_SAPLING.get())), true);
	public static final RegistryObject<Block> POTTED_CYPRESS_SAPLING = create("potted_cypress_sapling", () -> new FlowerPotBlock(CYPRESS_SAPLING.get(), copyOf(POTTED_JUNIPER_SAPLING.get())), false);
	public static final RegistryObject<Block> CYPRESS_PLANKS = create("cypress_planks", () -> new Block(copyOf(JUNIPER_PLANKS.get())), true);
	public static final RegistryObject<Block> CYPRESS_STAIRS = create("cypress_stairs", () -> new StairBlock(CYPRESS_PLANKS.get().defaultBlockState(), copyOf(JUNIPER_STAIRS.get())), true);
	public static final RegistryObject<Block> CYPRESS_SLAB = create("cypress_slab", () -> new SlabBlock(copyOf(JUNIPER_SLAB.get())), true);
	public static final RegistryObject<Block> CYPRESS_FENCE = create("cypress_fence", () -> new FenceBlock(copyOf(JUNIPER_FENCE.get())), true);
	public static final RegistryObject<Block> CYPRESS_FENCE_GATE = create("cypress_fence_gate", () -> new FenceGateBlock(copyOf(JUNIPER_FENCE_GATE.get()), WoodType.OAK), true);
	public static final RegistryObject<Block> CYPRESS_PRESSURE_PLATE = create("cypress_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copyOf(JUNIPER_PRESSURE_PLATE.get()), BlockSetType.OAK), true);
	public static final RegistryObject<Block> CYPRESS_BUTTON = create("cypress_button", () -> new ButtonBlock(copyOf(JUNIPER_BUTTON.get()), BlockSetType.OAK, 30, true), true);
	public static final RegistryObject<Block> CYPRESS_TRAPDOOR = create("cypress_trapdoor", () -> new TrapDoorBlock(copyOf(JUNIPER_TRAPDOOR.get()), BlockSetType.OAK), true);
	public static final RegistryObject<Block> CYPRESS_DOOR = create("cypress_door", () -> new DoorBlock(copyOf(JUNIPER_DOOR.get()), BlockSetType.OAK), false);
	public static final RegistryObject<Item> CYPRESS_DOOR_ITEM = create("cypress_door", () -> new DoubleHighBlockItem(CYPRESS_DOOR.get(), gen()));
	public static final RegistryObject<Block> CYPRESS_CHEST = create("cypress_chest", () -> new BWChestBlock(copyOf(JUNIPER_CHEST.get()), () -> BWBlockEntityTypes.BW_CHEST.get(), false), false);
	public static final RegistryObject<Item> CYPRESS_CHEST_ITEM = create("cypress_chest", () -> new BWChestItem(CYPRESS_CHEST.get(), gen(), BWChestBlockEntity.Type.CYPRESS, false));
	public static final RegistryObject<Block> TRAPPED_CYPRESS_CHEST = create("trapped_cypress_chest", () -> new BWChestBlock(copyOf(JUNIPER_CHEST.get()), () -> BWBlockEntityTypes.BW_CHEST.get(), true), false);
	public static final RegistryObject<Item> TRAPPED_CYPRESS_CHEST_ITEM = create("trapped_cypress_chest", () -> new BWChestItem(TRAPPED_CYPRESS_CHEST.get(), gen(), BWChestBlockEntity.Type.CYPRESS, true));
	public static final RegistryObject<Block> CYPRESS_SIGN = createSign("cypress", CYPRESS_WOOD_TYPE);
	//elder
	public static final RegistryObject<Block> ELDER_LOG = create("elder_log", () -> new RotatedPillarBlock(copyOf(JUNIPER_LOG.get())), true);
	public static final RegistryObject<Block> STRIPPED_ELDER_LOG = create("stripped_elder_log", () -> new RotatedPillarBlock(copyOf(ELDER_LOG.get())), true);
	public static final RegistryObject<Block> ELDER_WOOD = create("elder_wood", () -> new RotatedPillarBlock(copyOf(ELDER_LOG.get())), true);
	public static final RegistryObject<Block> STRIPPED_ELDER_WOOD = create("stripped_elder_wood", () -> new RotatedPillarBlock(copyOf(ELDER_LOG.get())), true);
	public static final RegistryObject<Block> ELDER_LEAVES = create("elder_leaves", () -> BlocksAccessor.callCreateLeavesBlock(SoundType.GRASS), true);
	public static final RegistryObject<Block> ELDER_SAPLING = create("elder_sapling", () -> new BWSaplingBlock(new ElderSaplingGenerator(), copyOf(JUNIPER_SAPLING.get())), true);
	public static final RegistryObject<Block> POTTED_ELDER_SAPLING = create("potted_elder_sapling", () -> new FlowerPotBlock(ELDER_SAPLING.get(), copyOf(POTTED_JUNIPER_SAPLING.get())), false);
	public static final RegistryObject<Block> ELDER_PLANKS = create("elder_planks", () -> new Block(copyOf(JUNIPER_PLANKS.get())), true);
	public static final RegistryObject<Block> ELDER_STAIRS = create("elder_stairs", () -> new StairBlock(ELDER_PLANKS.get().defaultBlockState(), copyOf(JUNIPER_STAIRS.get())), true);
	public static final RegistryObject<Block> ELDER_SLAB = create("elder_slab", () -> new SlabBlock(copyOf(JUNIPER_SLAB.get())), true);
	public static final RegistryObject<Block> ELDER_FENCE = create("elder_fence", () -> new FenceBlock(copyOf(JUNIPER_FENCE.get())), true);
	public static final RegistryObject<Block> ELDER_FENCE_GATE = create("elder_fence_gate", () -> new ElderFenceGateBlock(copyOf(JUNIPER_FENCE_GATE.get())), true);
	public static final RegistryObject<Block> ELDER_PRESSURE_PLATE = create("elder_pressure_plate", () -> new ElderPressurePlateBlock(copyOf(JUNIPER_PRESSURE_PLATE.get())), true);
	public static final RegistryObject<Block> ELDER_BUTTON = create("elder_button", () -> new ElderButtonBlock(copyOf(JUNIPER_BUTTON.get())), true);
	public static final RegistryObject<Block> ELDER_TRAPDOOR = create("elder_trapdoor", () -> new ElderTrapdoorBlock(copyOf(JUNIPER_TRAPDOOR.get())), true);
	public static final RegistryObject<Block> ELDER_DOOR = create("elder_door", () -> new ElderDoorBlock(copyOf(JUNIPER_DOOR.get())), false);
	public static final RegistryObject<Item> ELDER_DOOR_ITEM = create("elder_door", () -> new DoubleHighBlockItem(ELDER_DOOR.get(), gen()));
	public static final RegistryObject<Block> ELDER_CHEST = create("elder_chest", () -> new ElderChestBlock(copyOf(JUNIPER_CHEST.get()), () -> BWBlockEntityTypes.ELDER_CHEST.get(), false), false);
	public static final RegistryObject<Item> ELDER_CHEST_ITEM = create("elder_chest", () -> new BWChestItem(ELDER_CHEST.get(), gen(), BWChestBlockEntity.Type.ELDER, false));
	public static final RegistryObject<Block> TRAPPED_ELDER_CHEST = create("trapped_elder_chest", () -> new ElderChestBlock(copyOf(JUNIPER_CHEST.get()), () -> BWBlockEntityTypes.ELDER_CHEST.get(), true), false);
	public static final RegistryObject<Item> TRAPPED_ELDER_CHEST_ITEM = create("trapped_elder_chest", () -> new BWChestItem(TRAPPED_ELDER_CHEST.get(), gen(), BWChestBlockEntity.Type.ELDER, true));
	public static final RegistryObject<Block> ELDER_SIGN = createSign("elder", ELDER_WOOD_TYPE);
	//dragons_blood
	public static final RegistryObject<Block> DRAGONS_BLOOD_LOG = create("dragons_blood_log", () -> new DragonsBloodLogBlock(copyOf(JUNIPER_LOG.get()).randomTicks()), true);
	public static final RegistryObject<Block> STRIPPED_DRAGONS_BLOOD_LOG = create("stripped_dragons_blood_log", () -> new RotatedPillarBlock(copyOf(DRAGONS_BLOOD_LOG.get())), true);
	public static final RegistryObject<Block> DRAGONS_BLOOD_WOOD = create("dragons_blood_wood", () -> new RotatedPillarBlock(copyOf(DRAGONS_BLOOD_LOG.get())), true);
	public static final RegistryObject<Block> STRIPPED_DRAGONS_BLOOD_WOOD = create("stripped_dragons_blood_wood", () -> new RotatedPillarBlock(copyOf(DRAGONS_BLOOD_LOG.get())), true);
	public static final RegistryObject<Block> DRAGONS_BLOOD_LEAVES = create("dragons_blood_leaves", () -> BlocksAccessor.callCreateLeavesBlock(SoundType.GRASS), true);
	public static final RegistryObject<Block> DRAGONS_BLOOD_SAPLING = create("dragons_blood_sapling", () -> new BWSaplingBlock(new DragonsBloodSaplingGenerator(), copyOf(JUNIPER_SAPLING.get())), true);
	public static final RegistryObject<Block> POTTED_DRAGONS_BLOOD_SAPLING = create("potted_dragons_blood_sapling", () -> new FlowerPotBlock(DRAGONS_BLOOD_SAPLING.get(), copyOf(POTTED_JUNIPER_SAPLING.get())), false);
	public static final RegistryObject<Block> DRAGONS_BLOOD_PLANKS = create("dragons_blood_planks", () -> new Block(copyOf(JUNIPER_PLANKS.get())), true);
	public static final RegistryObject<Block> DRAGONS_BLOOD_STAIRS = create("dragons_blood_stairs", () -> new StairBlock(DRAGONS_BLOOD_PLANKS.get().defaultBlockState(), copyOf(JUNIPER_STAIRS.get())), true);
	public static final RegistryObject<Block> DRAGONS_BLOOD_SLAB = create("dragons_blood_slab", () -> new SlabBlock(copyOf(JUNIPER_SLAB.get())), true);
	public static final RegistryObject<Block> DRAGONS_BLOOD_FENCE = create("dragons_blood_fence", () -> new FenceBlock(copyOf(JUNIPER_FENCE.get())), true);
	public static final RegistryObject<Block> DRAGONS_BLOOD_FENCE_GATE = create("dragons_blood_fence_gate", () -> new DragonsBloodFenceGateBlock(copyOf(JUNIPER_FENCE_GATE.get())), true);
	public static final RegistryObject<Block> DRAGONS_BLOOD_PRESSURE_PLATE = create("dragons_blood_pressure_plate", () -> new DragonsBloodPressurePlateBlock(copyOf(JUNIPER_PRESSURE_PLATE.get())), true);
	public static final RegistryObject<Block> DRAGONS_BLOOD_BUTTON = create("dragons_blood_button", () -> new DragonsBloodButtonBlock(copyOf(JUNIPER_BUTTON.get())), true);
	public static final RegistryObject<Block> DRAGONS_BLOOD_TRAPDOOR = create("dragons_blood_trapdoor", () -> new DragonsBloodTrapdoorBlock(copyOf(JUNIPER_TRAPDOOR.get())), true);
	public static final RegistryObject<Block> DRAGONS_BLOOD_DOOR = create("dragons_blood_door", () -> new DragonsBloodDoorBlock(copyOf(JUNIPER_DOOR.get())), false);
	public static final RegistryObject<Item> DRAGONS_BLOOD_DOOR_ITEM = create("dragons_blood_door", () -> new DoubleHighBlockItem(DRAGONS_BLOOD_DOOR.get(), gen()));
	public static final RegistryObject<Block> DRAGONS_BLOOD_CHEST = create("dragons_blood_chest", () -> new DragonsBloodChestBlock(copyOf(JUNIPER_CHEST.get()), () -> BWBlockEntityTypes.DRAGONS_BLOOD_CHEST.get(), false), false);
	public static final RegistryObject<Item> DRAGONS_BLOOD_CHEST_ITEM = create("dragons_blood_chest", () -> new BWChestItem(DRAGONS_BLOOD_CHEST.get(), gen(), BWChestBlockEntity.Type.DRAGONS_BLOOD, false));
	public static final RegistryObject<Block> TRAPPED_DRAGONS_BLOOD_CHEST = create("trapped_dragons_blood_chest", () -> new DragonsBloodChestBlock(copyOf(JUNIPER_CHEST.get()), () -> BWBlockEntityTypes.DRAGONS_BLOOD_CHEST.get(), true), false);
	public static final RegistryObject<Item> TRAPPED_DRAGONS_BLOOD_CHEST_ITEM = create("trapped_dragons_blood_chest", () -> new BWChestItem(TRAPPED_DRAGONS_BLOOD_CHEST.get(), gen(), BWChestBlockEntity.Type.DRAGONS_BLOOD, true));
	public static final RegistryObject<Block> DRAGONS_BLOOD_SIGN = createSign("dragons_blood", DRAGONS_BLOOD_WOOD_TYPE);
	//other_plants
	public static final RegistryObject<Block> GLOWING_BRAMBLE = create("glowing_bramble", () -> new BrambleBlock(BlockBehaviour.Properties.of().sound(SoundType.GRASS).strength(2, 3).noCollission().randomTicks().lightLevel(state -> 15)), true);
	public static final RegistryObject<Block> ENDER_BRAMBLE = create("ender_bramble", () -> new BrambleBlock(BlockBehaviour.Properties.of().sound(SoundType.GRASS).strength(2, 3).noCollission().randomTicks()), true);
	public static final RegistryObject<Block> FRUITING_BRAMBLE = create("fruiting_bramble", () -> new BrambleBlock.Fruiting(copyOf(ENDER_BRAMBLE.get())), true);
	public static final RegistryObject<Block> SCORCHED_BRAMBLE = create("scorched_bramble", () -> new BrambleBlock(copyOf(ENDER_BRAMBLE.get())), true);
	public static final RegistryObject<Block> THICK_BRAMBLE = create("thick_bramble", () -> new BrambleBlock(copyOf(ENDER_BRAMBLE.get())), true);
	public static final RegistryObject<Block> FLEETING_BRAMBLE = create("fleeting_bramble", () -> new BrambleBlock(copyOf(ENDER_BRAMBLE.get())), true);
	//material_block
	public static final RegistryObject<Block> RAW_SILVER_BLOCK = create("raw_silver_block", () -> new Block(copyOf(Blocks.RAW_GOLD_BLOCK)) {
		@Override
		public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
			if (!world.isClientSide && entity instanceof LivingEntity && BewitchmentAPI.isWeakToSilver((LivingEntity) entity)) {
				entity.hurt(BWDamageSources.create(world, BWDamageSources.MAGIC_COPY), 1);
			}
			super.stepOn(world, pos, state, entity);
		}
	}, true);
	public static final RegistryObject<Block> SILVER_BLOCK = create("silver_block", () -> new Block(copyOf(Blocks.GOLD_BLOCK)) {
		@Override
		public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
			RAW_SILVER_BLOCK.get().stepOn(world, pos, state, entity);
		}
	}, true);
	public static final RegistryObject<Block> SALT_BLOCK = create("salt_block", () -> new Block(copyOf(Blocks.COAL_BLOCK)), true);
	public static final RegistryObject<Block> DRAGONS_BLOOD_RESIN_BLOCK = create("dragons_blood_resin_block", () -> new Block(copyOf(Blocks.NETHER_WART_BLOCK)), true);
	public static final RegistryObject<Block> SILVER_ORE = create("silver_ore", () -> new Block(copyOf(Blocks.GOLD_ORE)), true);
	public static final RegistryObject<Block> DEEPSLATE_SILVER_ORE = create("deepslate_silver_ore", () -> new Block(copyOf(Blocks.DEEPSLATE_GOLD_ORE)), true);
	public static final RegistryObject<Block> SALT_ORE = create("salt_ore", () -> new DropExperienceBlock(copyOf(Blocks.COAL_ORE), UniformInt.of(0, 2)), true);
	public static final RegistryObject<Block> DEEPSLATE_SALT_ORE = create("deepslate_salt_ore", () -> new DropExperienceBlock(copyOf(Blocks.DEEPSLATE_COAL_ORE), UniformInt.of(0, 2)), true);
	//misc_block
	public static final RegistryObject<Block> HEDGEWITCH_WOOL = create("hedgewitch_wool", () -> new Block(copyOf(Blocks.WHITE_WOOL)), true);
	public static final RegistryObject<Block> ALCHEMIST_WOOL = create("alchemist_wool", () -> new Block(copyOf(HEDGEWITCH_WOOL.get())), true);
	public static final RegistryObject<Block> BESMIRCHED_WOOL = create("besmirched_wool", () -> new Block(copyOf(HEDGEWITCH_WOOL.get())), true);
	public static final RegistryObject<Block> HEDGEWITCH_CARPET = create("hedgewitch_carpet", () -> new CarpetBlock(copyOf(Blocks.WHITE_CARPET)), true);
	public static final RegistryObject<Block> ALCHEMIST_CARPET = create("alchemist_carpet", () -> new CarpetBlock(copyOf(HEDGEWITCH_CARPET.get())), true);
	public static final RegistryObject<Block> BESMIRCHED_CARPET = create("besmirched_carpet", () -> new CarpetBlock(copyOf(HEDGEWITCH_CARPET.get())), true);
	public static final RegistryObject<Block> IRON_CANDELABRA = create("iron_candelabra", () -> new CandelabraBlock(copyOf(Blocks.IRON_BLOCK).pushReaction(PushReaction.BLOCK).lightLevel(blockState -> blockState.getValue(BlockStateProperties.LIT) ? 15 : 0), (byte) 16), true);
	public static final RegistryObject<Block> GOLDEN_CANDELABRA = create("golden_candelabra", () -> new CandelabraBlock(copyOf(Blocks.GOLD_BLOCK).pushReaction(PushReaction.BLOCK).lightLevel(blockState -> blockState.getValue(BlockStateProperties.LIT) ? 15 : 0), (byte) 32), true);
	public static final RegistryObject<Block> SILVER_CANDELABRA = create("silver_candelabra", () -> new CandelabraBlock(copyOf(SILVER_BLOCK.get()).pushReaction(PushReaction.BLOCK).lightLevel(blockState -> blockState.getValue(BlockStateProperties.LIT) ? 15 : 0), (byte) 32), true);
	public static final RegistryObject<Block> NETHERITE_CANDELABRA = create("netherite_candelabra", () -> new CandelabraBlock(copyOf(Blocks.NETHERITE_BLOCK).pushReaction(PushReaction.BLOCK).lightLevel(blockState -> blockState.getValue(BlockStateProperties.LIT) ? 15 : 0), (byte) 64), false);
	public static final RegistryObject<Item> NETHERITE_CANDELABRA_ITEM = create("netherite_candelabra", () -> new BlockItem(NETHERITE_CANDELABRA.get(), gen().fireResistant()));
	public static final RegistryObject<Block> BLESSED_STONE = create("blessed_stone", () -> new Block(copyOf(Blocks.BEDROCK)), true);
	//block_entity
	public static final Supplier<Block[]> STONE_WITCH_ALTAR = createAltar("stone_witch_altar", copyOf(Blocks.STONE).pushReaction(PushReaction.BLOCK));
	public static final Supplier<Block[]> MOSSY_COBBLESTONE_WITCH_ALTAR = createAltar("mossy_cobblestone_witch_altar", copyOf(Blocks.MOSSY_COBBLESTONE).pushReaction(PushReaction.BLOCK));
	public static final Supplier<Block[]> PRISMARINE_WITCH_ALTAR = createAltar("prismarine_witch_altar", copyOf(Blocks.PRISMARINE).pushReaction(PushReaction.BLOCK));
	public static final Supplier<Block[]> NETHER_BRICK_WITCH_ALTAR = createAltar("nether_brick_witch_altar", copyOf(Blocks.NETHER_BRICKS).pushReaction(PushReaction.BLOCK));
	public static final Supplier<Block[]> BLACKSTONE_WITCH_ALTAR = createAltar("blackstone_witch_altar", copyOf(Blocks.BLACKSTONE).pushReaction(PushReaction.BLOCK));
	public static final Supplier<Block[]> GOLDEN_WITCH_ALTAR = createAltar("golden_witch_altar", copyOf(Blocks.GOLD_BLOCK).pushReaction(PushReaction.BLOCK));
	public static final Supplier<Block[]> END_STONE_WITCH_ALTAR = createAltar("end_stone_witch_altar", copyOf(Blocks.END_STONE).pushReaction(PushReaction.BLOCK));
	public static final Supplier<Block[]> OBSIDIAN_WITCH_ALTAR = createAltar("obsidian_witch_altar", copyOf(Blocks.OBSIDIAN).pushReaction(PushReaction.BLOCK));
	public static final Supplier<Block[]> PURPUR_WITCH_ALTAR = createAltar("purpur_witch_altar", copyOf(Blocks.PURPUR_BLOCK).pushReaction(PushReaction.BLOCK));
	public static final RegistryObject<Block> WITCH_CAULDRON = create("witch_cauldron", () -> new WitchCauldronBlock(copyOf(Blocks.CAULDRON).pushReaction(PushReaction.BLOCK)), true);
	public static final RegistryObject<Block> CRYSTAL_BALL = create("crystal_ball", () -> new CrystalBallBlock(copyOf(SILVER_BLOCK.get()).sound(SoundType.GLASS)), true);
	public static final RegistryObject<Block> BRAZIER = create("brazier", () -> new BrazierBlock(copyOf(SILVER_BLOCK.get()).lightLevel(blockState -> blockState.getValue(BlockStateProperties.LIT) ? 15 : 0)), true);
	public static final RegistryObject<Block> OAK_POPPET_SHELF = create("oak_poppet_shelf", () -> new PoppetShelfBlock(copyOf(Blocks.OAK_PLANKS).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final RegistryObject<Block> SPRUCE_POPPET_SHELF = create("spruce_poppet_shelf", () -> new PoppetShelfBlock(copyOf(Blocks.SPRUCE_PLANKS).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final RegistryObject<Block> BIRCH_POPPET_SHELF = create("birch_poppet_shelf", () -> new PoppetShelfBlock(copyOf(Blocks.BIRCH_PLANKS).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final RegistryObject<Block> JUNGLE_POPPET_SHELF = create("jungle_poppet_shelf", () -> new PoppetShelfBlock(copyOf(Blocks.JUNGLE_PLANKS).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final RegistryObject<Block> ACACIA_POPPET_SHELF = create("acacia_poppet_shelf", () -> new PoppetShelfBlock(copyOf(Blocks.ACACIA_PLANKS).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final RegistryObject<Block> DARK_OAK_POPPET_SHELF = create("dark_oak_poppet_shelf", () -> new PoppetShelfBlock(copyOf(Blocks.DARK_OAK_PLANKS).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final RegistryObject<Block> CRIMSON_POPPET_SHELF = create("crimson_poppet_shelf", () -> new PoppetShelfBlock(copyOf(Blocks.CRIMSON_PLANKS).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final RegistryObject<Block> WARPED_POPPET_SHELF = create("warped_poppet_shelf", () -> new PoppetShelfBlock(copyOf(Blocks.WARPED_PLANKS).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final RegistryObject<Block> JUNIPER_POPPET_SHELF = create("juniper_poppet_shelf", () -> new PoppetShelfBlock(copyOf(JUNIPER_PLANKS.get()).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final RegistryObject<Block> CYPRESS_POPPET_SHELF = create("cypress_poppet_shelf", () -> new PoppetShelfBlock(copyOf(CYPRESS_PLANKS.get()).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final RegistryObject<Block> ELDER_POPPET_SHELF = create("elder_poppet_shelf", () -> new PoppetShelfBlock(copyOf(ELDER_PLANKS.get()).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final RegistryObject<Block> DRAGONS_BLOOD_POPPET_SHELF = create("dragons_blood_poppet_shelf", () -> new PoppetShelfBlock(copyOf(DRAGONS_BLOOD_PLANKS.get()).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final RegistryObject<Block> WHITE_COFFIN = create("white_coffin", () -> new CoffinBlock(DyeColor.WHITE, copyOf(CYPRESS_PLANKS.get()).noOcclusion()), true);
	public static final RegistryObject<Block> ORANGE_COFFIN = create("orange_coffin", () -> new CoffinBlock(DyeColor.ORANGE, copyOf(CYPRESS_PLANKS.get()).noOcclusion()), true);
	public static final RegistryObject<Block> MAGENTA_COFFIN = create("magenta_coffin", () -> new CoffinBlock(DyeColor.MAGENTA, copyOf(CYPRESS_PLANKS.get()).noOcclusion()), true);
	public static final RegistryObject<Block> LIGHT_BLUE_COFFIN = create("light_blue_coffin", () -> new CoffinBlock(DyeColor.LIGHT_BLUE, copyOf(CYPRESS_PLANKS.get()).noOcclusion()), true);
	public static final RegistryObject<Block> YELLOW_COFFIN = create("yellow_coffin", () -> new CoffinBlock(DyeColor.YELLOW, copyOf(CYPRESS_PLANKS.get()).noOcclusion()), true);
	public static final RegistryObject<Block> LIME_COFFIN = create("lime_coffin", () -> new CoffinBlock(DyeColor.LIME, copyOf(CYPRESS_PLANKS.get()).noOcclusion()), true);
	public static final RegistryObject<Block> PINK_COFFIN = create("pink_coffin", () -> new CoffinBlock(DyeColor.PINK, copyOf(CYPRESS_PLANKS.get()).noOcclusion()), true);
	public static final RegistryObject<Block> GRAY_COFFIN = create("gray_coffin", () -> new CoffinBlock(DyeColor.GRAY, copyOf(CYPRESS_PLANKS.get()).noOcclusion()), true);
	public static final RegistryObject<Block> LIGHT_GRAY_COFFIN = create("light_gray_coffin", () -> new CoffinBlock(DyeColor.LIGHT_GRAY, copyOf(CYPRESS_PLANKS.get()).noOcclusion()), true);
	public static final RegistryObject<Block> CYAN_COFFIN = create("cyan_coffin", () -> new CoffinBlock(DyeColor.CYAN, copyOf(CYPRESS_PLANKS.get()).noOcclusion()), true);
	public static final RegistryObject<Block> PURPLE_COFFIN = create("purple_coffin", () -> new CoffinBlock(DyeColor.PURPLE, copyOf(CYPRESS_PLANKS.get()).noOcclusion()), true);
	public static final RegistryObject<Block> BLUE_COFFIN = create("blue_coffin", () -> new CoffinBlock(DyeColor.BLUE, copyOf(CYPRESS_PLANKS.get()).noOcclusion()), true);
	public static final RegistryObject<Block> BROWN_COFFIN = create("brown_coffin", () -> new CoffinBlock(DyeColor.BROWN, copyOf(CYPRESS_PLANKS.get()).noOcclusion()), true);
	public static final RegistryObject<Block> GREEN_COFFIN = create("green_coffin", () -> new CoffinBlock(DyeColor.GREEN, copyOf(CYPRESS_PLANKS.get()).noOcclusion()), true);
	public static final RegistryObject<Block> RED_COFFIN = create("red_coffin", () -> new CoffinBlock(DyeColor.RED, copyOf(CYPRESS_PLANKS.get()).noOcclusion()), true);
	public static final RegistryObject<Block> BLACK_COFFIN = create("black_coffin", () -> new CoffinBlock(DyeColor.BLACK, copyOf(CYPRESS_PLANKS.get()).noOcclusion()), true);
	//armor
	public static final RegistryObject<Item> HEDGEWITCH_HOOD = create("hedgewitch_hood", () -> new WitchArmorItem(BWMaterials.HEDGEWITCH_ARMOR, ArmorItem.Type.HELMET, gen(), Bewitchment.id("textures/entity/armor/hedgewitch.png"), false));
	public static final RegistryObject<Item> HEDGEWITCH_HAT = create("hedgewitch_hat", () -> new WitchArmorItem(BWMaterials.HEDGEWITCH_ARMOR, ArmorItem.Type.HELMET, gen(), Bewitchment.id("textures/entity/armor/hedgewitch.png"), true));
	public static final RegistryObject<Item> HEDGEWITCH_ROBES = create("hedgewitch_robes", () -> new WitchArmorItem(BWMaterials.HEDGEWITCH_ARMOR, ArmorItem.Type.CHESTPLATE, gen(), Bewitchment.id("textures/entity/armor/hedgewitch.png"), false));
	public static final RegistryObject<Item> HEDGEWITCH_PANTS = create("hedgewitch_pants", () -> new WitchArmorItem(BWMaterials.HEDGEWITCH_ARMOR, ArmorItem.Type.LEGGINGS, gen(), Bewitchment.id("textures/entity/armor/hedgewitch.png"), false));
	public static final RegistryObject<Item> ALCHEMIST_HOOD = create("alchemist_hood", () -> new WitchArmorItem(BWMaterials.ALCHEMIST_ARMOR, ArmorItem.Type.HELMET, gen(), Bewitchment.id("textures/entity/armor/alchemist.png"), false));
	public static final RegistryObject<Item> ALCHEMIST_HAT = create("alchemist_hat", () -> new WitchArmorItem(BWMaterials.ALCHEMIST_ARMOR, ArmorItem.Type.HELMET, gen(), Bewitchment.id("textures/entity/armor/alchemist.png"), true));
	public static final RegistryObject<Item> ALCHEMIST_ROBES = create("alchemist_robes", () -> new WitchArmorItem(BWMaterials.ALCHEMIST_ARMOR, ArmorItem.Type.CHESTPLATE, gen(), Bewitchment.id("textures/entity/armor/alchemist.png"), false));
	public static final RegistryObject<Item> ALCHEMIST_PANTS = create("alchemist_pants", () -> new WitchArmorItem(BWMaterials.ALCHEMIST_ARMOR, ArmorItem.Type.LEGGINGS, gen(), Bewitchment.id("textures/entity/armor/alchemist.png"), false));
	public static final RegistryObject<Item> BESMIRCHED_HOOD = create("besmirched_hood", () -> new WitchArmorItem(BWMaterials.BESMIRCHED_ARMOR, ArmorItem.Type.HELMET, gen(), Bewitchment.id("textures/entity/armor/besmirched.png"), false));
	public static final RegistryObject<Item> BESMIRCHED_HAT = create("besmirched_hat", () -> new WitchArmorItem(BWMaterials.BESMIRCHED_ARMOR, ArmorItem.Type.HELMET, gen(), Bewitchment.id("textures/entity/armor/besmirched.png"), true));
	public static final RegistryObject<Item> BESMIRCHED_ROBES = create("besmirched_robes", () -> new WitchArmorItem(BWMaterials.BESMIRCHED_ARMOR, ArmorItem.Type.CHESTPLATE, gen(), Bewitchment.id("textures/entity/armor/besmirched.png"), false));
	public static final RegistryObject<Item> BESMIRCHED_PANTS = create("besmirched_pants", () -> new WitchArmorItem(BWMaterials.BESMIRCHED_ARMOR, ArmorItem.Type.LEGGINGS, gen(), Bewitchment.id("textures/entity/armor/besmirched.png"), false));
	public static final RegistryObject<Item> HARBINGER = create("harbinger", () -> new WitchArmorItem(BWMaterials.HARBINGER_ARMOR, ArmorItem.Type.BOOTS, gen().rarity(Rarity.RARE).durability(444).fireResistant(), Bewitchment.id("textures/entity/armor/harbinger.png"), false));
	//trinket
	public static final RegistryObject<Item> NAZAR = create("nazar", () -> new AccessoryItem(gen().stacksTo(1)));
	public static final RegistryObject<Item> SPECTER_BANGLE = create("specter_bangle", () -> new SpecterBangleItem(gen().stacksTo(1)));
	public static final RegistryObject<Item> PRICKLY_BELT = create("prickly_belt", () -> new PricklyBeltItem(gen().stacksTo(1)));
	public static final RegistryObject<Item> HELLISH_BAUBLE = create("hellish_bauble", () -> new HellishBaubleItem(gen().stacksTo(1).fireResistant()));
	public static final RegistryObject<Item> DRUID_BAND = create("druid_band", () -> new DruidBandItem(gen().stacksTo(1)));
	public static final RegistryObject<Item> ZEPHYR_HARNESS = create("zephyr_harness", () -> new ZephyrHarnessItem(gen().stacksTo(1)));
	//tool
	public static final RegistryObject<Item> ATHAME = create("athame", () -> new AthameItem(BWMaterials.SILVER_TOOL, 1, -2, gen()));
	public static final RegistryObject<Item> SILVER_ARROW = create("silver_arrow", () -> new SilverArrowItem(gen()));
	public static final RegistryObject<Item> SCEPTER = create("scepter", () -> new ScepterItem(gen().rarity(Rarity.RARE).durability(64).fireResistant()));
	public static final RegistryObject<Item> CADUCEUS = create("caduceus", () -> new CaduceusItem(0, -3, Tiers.NETHERITE, BlockTags.MINEABLE_WITH_PICKAXE, gen().rarity(Rarity.RARE).durability(1998).fireResistant()));
	public static final RegistryObject<Item> HORNED_SPEAR = create("horned_spear", () -> new HornedSpearItem(Tiers.NETHERITE, 2, -2.4f, gen().rarity(Rarity.RARE).durability(1008).fireResistant()));
	public static final RegistryObject<Item> CHALK = create("chalk", () -> new ChalkItem(gen().durability(128), GLYPH.get()));
	public static final RegistryObject<Item> GOLDEN_CHALK = create("golden_chalk", () -> new ChalkItem(gen().durability(128), GOLDEN_GLYPH.get()));
	public static final RegistryObject<Item> FIERY_CHALK = create("fiery_chalk", () -> new ChalkItem(gen().durability(128), FIERY_GLYPH.get()));
	public static final RegistryObject<Item> ELDRITCH_CHALK = create("eldritch_chalk", () -> new ChalkItem(gen().durability(128), ELDRITCH_GLYPH.get()));
	public static final RegistryObject<Item> TAGLOCK = create("taglock", () -> new TaglockItem(gen().craftRemainder(Items.GLASS_BOTTLE)));
	public static final RegistryObject<Item> WAYSTONE = create("waystone", () -> new WaystoneItem(gen().durability(3)));
	public static final RegistryObject<Item> JUNIPER_BROOM = create("juniper_broom", () -> new BroomItem(gen().stacksTo(1), BWEntityTypes.JUNIPER_BROOM));
	public static final RegistryObject<Item> CYPRESS_BROOM = create("cypress_broom", () -> new BroomItem(gen().stacksTo(1), BWEntityTypes.CYPRESS_BROOM));
	public static final RegistryObject<Item> ELDER_BROOM = create("elder_broom", () -> new BroomItem(gen().stacksTo(1), BWEntityTypes.ELDER_BROOM));
	public static final RegistryObject<Item> DRAGONS_BLOOD_BROOM = create("dragons_blood_broom", () -> new DragonsBloodBroomItem(gen().stacksTo(1), BWEntityTypes.DRAGONS_BLOOD_BROOM));
	public static final RegistryObject<Item> MENDING_SIGIL = create("mending_sigil", () -> new SigilItem(gen(), BWSigils.MENDING.get()));
	public static final RegistryObject<Item> CLEANSING_SIGIL = create("cleansing_sigil", () -> new SigilItem(gen(), BWSigils.CLEANSING.get()));
	public static final RegistryObject<Item> JUDGMENT_SIGIL = create("judgment_sigil", () -> new SigilItem(gen(), BWSigils.JUDGMENT.get()));
	public static final RegistryObject<Item> DECAY_SIGIL = create("decay_sigil", () -> new SigilItem(gen(), BWSigils.DECAY.get()));
	public static final RegistryObject<Item> SHRIEKING_SIGIL = create("shrieking_sigil", () -> new SigilItem(gen(), BWSigils.SHRIEKING.get()));
	public static final RegistryObject<Item> SENTINEL_SIGIL = create("sentinel_sigil", () -> new SigilItem(gen(), BWSigils.SENTINEL.get()));
	public static final RegistryObject<Item> SLIPPERY_SIGIL = create("slippery_sigil", () -> new SigilItem(gen(), BWSigils.SLIPPERY.get()));
	public static final RegistryObject<Item> SHADOWS_SIGIL = create("shadows_sigil", () -> new SigilItem(gen(), BWSigils.SHADOWS.get()));
	public static final RegistryObject<Item> EXTENDING_SIGIL = create("extending_sigil", () -> new SigilItem(gen(), BWSigils.EXTENDING.get()));
	public static final RegistryObject<Item> SMELLY_SIGIL = create("smelly_sigil", () -> new SigilItem(gen(), BWSigils.SMELLY.get()));
	public static final RegistryObject<Item> RUIN_SIGIL = create("ruin_sigil", () -> new SigilItem(gen(), BWSigils.RUIN.get()));
	public static final RegistryObject<Item> HEAVY_SIGIL = create("heavy_sigil", () -> new SigilItem(gen(), BWSigils.HEAVY.get()));
	public static final RegistryObject<PoppetItem> PROTECTION_POPPET = create("protection_poppet", () -> new PoppetItem(gen().durability(128), true));
	public static final RegistryObject<PoppetItem> DEATH_PROTECTION_POPPET = create("death_protection_poppet", () -> new PoppetItem(gen().durability(1), false));
	public static final RegistryObject<PoppetItem> VOODOO_POPPET = create("voodoo_poppet", () -> new PoppetItem(gen().durability(128), false));
	public static final RegistryObject<PoppetItem> VOODOO_PROTECTION_POPPET = create("voodoo_protection_poppet", () -> new PoppetItem(gen().durability(128), true));
	public static final RegistryObject<PoppetItem> MENDING_POPPET = create("mending_poppet", () -> new PoppetItem(gen().durability(1), true));
	public static final RegistryObject<PoppetItem> CURSE_POPPET = create("curse_poppet", () -> new CursePoppetItem(gen().durability(1), true));
	public static final RegistryObject<PoppetItem> VAMPIRIC_POPPET = create("vampiric_poppet", () -> new PoppetItem(gen().durability(128), false));
	public static final RegistryObject<PoppetItem> JUDGMENT_POPPET = create("judgment_poppet", () -> new PoppetItem(gen().durability(64), true));
	public static final RegistryObject<PoppetItem> FATIGUE_POPPET = create("fatigue_poppet", () -> new PoppetItem(gen().durability(64), true));
	public static final RegistryObject<Item> DEMONIC_CONTRACT = create("demonic_contract", () -> new ContractItem(gen().rarity(Rarity.RARE).stacksTo(1)));
	//material_item
	public static final RegistryObject<Item> CLEANSING_BALM = create("cleansing_balm", () -> new Item(gen().craftRemainder(Items.GLASS_BOTTLE)));
	public static final RegistryObject<Item> GRIM_ELIXIR = create("grim_elixir", () -> new Item(gen().craftRemainder(Items.GLASS_BOTTLE)));
	public static final RegistryObject<Item> AQUA_CERATE = create("aqua_cerate", () -> new Item(gen().craftRemainder(Items.GLASS_BOTTLE)));
	public static final RegistryObject<Item> FIERY_SERUM = create("fiery_serum", () -> new Item(gen().craftRemainder(Items.GLASS_BOTTLE)));
	public static final RegistryObject<Item> EARTH_ICHOR = create("earth_ichor", () -> new Item(gen().craftRemainder(Items.GLASS_BOTTLE)));
	public static final RegistryObject<Item> HEAVEN_EXTRACT = create("heaven_extract", () -> new Item(gen().craftRemainder(Items.GLASS_BOTTLE)));
	public static final RegistryObject<Item> RAW_SILVER = create("raw_silver", () -> new Item(gen()));
	public static final RegistryObject<Item> SILVER_INGOT = create("silver_ingot", () -> new Item(gen()));
	public static final RegistryObject<Item> SILVER_NUGGET = create("silver_nugget", () -> new Item(gen()));
	public static final RegistryObject<Item> SALT = create("salt", () -> new ItemNameBlockItem(SALT_LINE.get(), gen()));
	public static final RegistryObject<Item> ACONITE = create("aconite", () -> new Item(gen()));
	public static final RegistryObject<Item> ACONITE_SEEDS = create("aconite_seeds", () -> new ItemNameBlockItem(ACONITE_CROP.get(), gen()));
	public static final RegistryObject<Item> BELLADONNA = create("belladonna", () -> new Item(gen()));
	public static final RegistryObject<Item> BELLADONNA_SEEDS = create("belladonna_seeds", () -> new ItemNameBlockItem(BELLADONNA_CROP.get(), gen()));
	public static final RegistryObject<Item> GARLIC = create("garlic", () -> new ItemNameBlockItem(GARLIC_CROP.get(), gen().food(Foods.POTATO)));
	public static final RegistryObject<Item> MANDRAKE_ROOT = create("mandrake_root", () -> new Item(gen()));
	public static final RegistryObject<Item> MANDRAKE_SEEDS = create("mandrake_seeds", () -> new ItemNameBlockItem(MANDRAKE_CROP.get(), gen()));
	public static final RegistryObject<Item> OAK_BARK = create("oak_bark", () -> new Item(gen()));
	public static final RegistryObject<Item> SPRUCE_BARK = create("spruce_bark", () -> new Item(gen()));
	public static final RegistryObject<Item> BIRCH_BARK = create("birch_bark", () -> new Item(gen()));
	public static final RegistryObject<Item> JUNIPER_BARK = create("juniper_bark", () -> new Item(gen()));
	public static final RegistryObject<Item> CYPRESS_BARK = create("cypress_bark", () -> new Item(gen()));
	public static final RegistryObject<Item> ELDER_BARK = create("elder_bark", () -> new Item(gen()));
	public static final RegistryObject<Item> WOOD_ASH = create("wood_ash", () -> new Item(gen()));
	public static final RegistryObject<Item> DRAGONS_BLOOD_RESIN = create("dragons_blood_resin", () -> new Item(gen()));
	public static final RegistryObject<Item> SNAKE_TONGUE = create("snake_tongue", () -> new Item(gen()));
	public static final RegistryObject<Item> ECTOPLASM = create("ectoplasm", () -> new Item(gen()));
	public static final RegistryObject<Item> DEMON_HORN = create("demon_horn", () -> new Item(gen().fireResistant()));
	public static final RegistryObject<Item> DEMON_HEART = create("demon_heart", () -> new Item(gen().food(BWFoodComponents.DEMON_HEART).rarity(Rarity.UNCOMMON).fireResistant()));
	public static final RegistryObject<Item> BOTTLE_OF_BLOOD = create("bottle_of_blood", () -> new BottleOfBloodItem(gen().stacksTo(16).craftRemainder(Items.GLASS_BOTTLE)));
	public static final RegistryObject<Item> GRILLED_GARLIC = create("grilled_garlic", () -> new Item(gen().food(Foods.CARROT)));
	public static final RegistryObject<Item> GARLIC_BREAD = create("garlic_bread", () -> new Item(gen().food(BWFoodComponents.GARLIC_BREAD)));
	public static final RegistryObject<Item> WITCHBERRY = create("witchberry", () -> new Item(gen().food(BWFoodComponents.WITCHBERRY)));
	public static final RegistryObject<Item> WITCHBERRY_PIE = create("witchberry_pie", () -> new Item(gen().food(BWFoodComponents.WITCHBERRY_PIE)));
	public static final RegistryObject<Item> WITCHBERRY_COOKIE = create("witchberry_cookie", () -> new Item(gen().food(BWFoodComponents.WITCHBERRY_COOKIE)));
	public static final RegistryObject<Item> GROTESQUE_STEW = create("grotesque_stew", () -> new GrotestqueStewItem(gen().food(BWFoodComponents.DEMON_HEART).stacksTo(1).craftRemainder(Items.BOWL)));
	//spawn_egg
	public static final RegistryObject<Item> OWL_SPAWN_EGG = create("owl_spawn_egg", () -> new ForgeSpawnEggItem(BWEntityTypes.OWL, 0x7f3f00, 0xc0c0c0, gen()));
	public static final RegistryObject<Item> RAVEN_SPAWN_EGG = create("raven_spawn_egg", () -> new ForgeSpawnEggItem(BWEntityTypes.RAVEN, 0x3f3f3f, 0x000000, gen()));
	public static final RegistryObject<Item> SNAKE_SPAWN_EGG = create("snake_spawn_egg", () -> new ForgeSpawnEggItem(BWEntityTypes.SNAKE, 0x7f3f00, 0x3f3f3f, gen()));
	public static final RegistryObject<Item> TOAD_SPAWN_EGG = create("toad_spawn_egg", () -> new ForgeSpawnEggItem(BWEntityTypes.TOAD, 0x3f3f00, 0x00c200, gen()));
	public static final RegistryObject<Item> GHOST_SPAWN_EGG = create("ghost_spawn_egg", () -> new ForgeSpawnEggItem(BWEntityTypes.GHOST, 0xcacaca, 0x969696, gen()));
	public static final RegistryObject<Item> VAMPIRE_SPAWN_EGG = create("vampire_spawn_egg", () -> new ForgeSpawnEggItem(BWEntityTypes.VAMPIRE, 0xddcca8, 0x7d0000, gen()));
	public static final RegistryObject<Item> WEREWOLF_SPAWN_EGG = create("werewolf_spawn_egg", () -> new ForgeSpawnEggItem(BWEntityTypes.WEREWOLF, 0x9f9a96, 0x532f0f, gen()));
	public static final RegistryObject<Item> HELLHOUND_SPAWN_EGG = create("hellhound_spawn_egg", () -> new ForgeSpawnEggItem(BWEntityTypes.HELLHOUND, 0xc82000, 0x802020, gen()));
	public static final RegistryObject<Item> DEMON_SPAWN_EGG = create("demon_spawn_egg", () -> new ForgeSpawnEggItem(BWEntityTypes.DEMON, 0x802020, 0xc82000, gen()));
	public static final RegistryObject<Item> LEONARD_SPAWN_EGG = create("leonard_spawn_egg", () -> new ForgeSpawnEggItem(BWEntityTypes.LEONARD, 0x5e3214, 0xa00303, gen()));
	public static final RegistryObject<Item> BAPHOMET_SPAWN_EGG = create("baphomet_spawn_egg", () -> new ForgeSpawnEggItem(BWEntityTypes.BAPHOMET, 0xa00303, 0x5e3214, gen()));
	public static final RegistryObject<Item> LILITH_SPAWN_EGG = create("lilith_spawn_egg", () -> new ForgeSpawnEggItem(BWEntityTypes.LILITH, 0x222621, 0xc9cbcd, gen()));
	public static final RegistryObject<Item> HERNE_SPAWN_EGG = create("herne_spawn_egg", () -> new ForgeSpawnEggItem(BWEntityTypes.HERNE, 0x5d482d, 0x294e00, gen()));
	//books
	public static final RegistryObject<Item> BOOK_OF_SHADOWS = create("book_of_shadows", () -> new BWBookItem(gen().stacksTo(1)));

	private static <T extends Block> RegistryObject<T> create(String name, Supplier<T> block, boolean createItem) {
		RegistryObject<T> registered = BLOCKS.register(name, block);
		if (createItem) {
			ITEMS.register(name, () -> new BlockItem(registered.get(), gen()));
		}
		return registered;
	}

	private static <T extends Item> RegistryObject<T> create(String name, Supplier<T> item) {
		return ITEMS.register(name, item);
	}

	private static BlockBehaviour.Properties copyOf(BlockBehaviour block) {
		return BlockBehaviour.Properties.copy(block);
	}

	private static Item.Properties gen() {
		return new Item.Properties();
	}

	/**
	 * Standing, wall, hanging and wall-hanging signs plus their items, on the vanilla sign blocks.
	 * The vanilla sign block entities accept these blocks through {@code BlockEntityTypeMixin}, and the
	 * textures follow vanilla's {@code <namespace>:entity/signs/[hanging/]<wood>} layout.
	 */
	private static RegistryObject<Block> createSign(String name, WoodType woodType) {
		RegistryObject<Block> sign = create(name + "_sign", () -> new StandingSignBlock(copyOf(Blocks.OAK_SIGN), woodType), false);
		RegistryObject<Block> wallSign = create(name + "_wall_sign", () -> new WallSignBlock(copyOf(Blocks.OAK_WALL_SIGN).lootFrom(sign), woodType), false);
		RegistryObject<Block> hangingSign = create(name + "_hanging_sign", () -> new CeilingHangingSignBlock(copyOf(Blocks.OAK_HANGING_SIGN), woodType), false);
		RegistryObject<Block> wallHangingSign = create(name + "_wall_hanging_sign", () -> new WallHangingSignBlock(copyOf(Blocks.OAK_WALL_HANGING_SIGN).lootFrom(hangingSign), woodType), false);
		create(name + "_sign", () -> new SignItem(gen().stacksTo(16), sign.get(), wallSign.get()));
		create(name + "_hanging_sign", () -> new HangingSignItem(hangingSign.get(), wallHangingSign.get(), gen().stacksTo(16)));
		return sign;
	}

	/**
	 * Registers the unformed altar (with item), its mossy variant, 16 dyed variants and 3 themed
	 * variants. Returns all 21 in the original order: [unformed, moss, 16 dyes, hedgewitch, alchemist, besmirched].
	 */
	private static Supplier<Block[]> createAltar(String name, BlockBehaviour.Properties properties) {
		BlockBehaviour.Properties settings = properties.lightLevel(blockState -> blockState.getValue(BlockStateProperties.LEVEL));
		List<RegistryObject<Block>> altars = new ArrayList<>(21);
		RegistryObject<Block> unformed = create(name, () -> new WitchAltarBlock(settings, null, false), true);
		altars.add(unformed);
		altars.add(create("moss_" + name, () -> new WitchAltarBlock(settings, unformed.get(), true), false));
		for (int i = 0; i < 16; i++) {
			String color = DyeColor.byId(i).getName();
			altars.add(create(color + "_" + name, () -> new WitchAltarBlock(settings, unformed.get(), true), false));
		}
		altars.add(create("hedgewitch_" + name, () -> new WitchAltarBlock(settings, unformed.get(), true), false));
		altars.add(create("alchemist_" + name, () -> new WitchAltarBlock(settings, unformed.get(), true), false));
		altars.add(create("besmirched_" + name, () -> new WitchAltarBlock(settings, unformed.get(), true), false));
		return () -> altars.stream().map(RegistryObject::get).toArray(Block[]::new);
	}

	public static final RegistryObject<CreativeModeTab> GROUP = CREATIVE_MODE_TABS.register(Bewitchment.MOD_ID, () -> CreativeModeTab.builder()
			.title(Component.translatable("itemGroup." + Bewitchment.MOD_ID))
			.icon(() -> ATHAME.get().getDefaultInstance())
			.displayItems((displayContext, entries) -> {
				for (RegistryObject<Item> entry : ITEMS.getEntries()) {
					Item item = entry.get();
					if (item instanceof ContractItem) {
						BWRegistries.CONTRACT.forEach(contract -> {
							ItemStack stack = new ItemStack(item);
							stack.getOrCreateTag().putString("Contract", BWRegistries.CONTRACT.getKey(contract).toString());
							stack.getOrCreateTag().putInt("Duration", 168000);
							entries.accept(stack);
						});
					} else if (BOATS.stream().noneMatch(boat -> boat.getId().equals(entry.getId()))) {
						entries.accept(item);
					}
				}
				BOATS.forEach(boat -> entries.accept(boat.get()));
			})
			.build());

	/** Log -> stripped log pairs for axe stripping (was Fabric's StrippableBlockRegistry). */
	private static Map<Block, Block> strippables;

	public static void register(IEventBus modBus) {
		BLOCKS.register(modBus);
		ITEMS.register(modBus);
		CREATIVE_MODE_TABS.register(modBus);
	}

	/** Flammability and compost chances; vanilla maps, so this must run on the main thread (enqueueWork). */
	static void commonSetup() {
		FireBlock fire = (FireBlock) Blocks.FIRE;
		fire.setFlammable(STRIPPED_JUNIPER_LOG.get(), 5, 5);
		fire.setFlammable(STRIPPED_JUNIPER_WOOD.get(), 5, 5);
		fire.setFlammable(JUNIPER_LOG.get(), 5, 5);
		fire.setFlammable(JUNIPER_WOOD.get(), 5, 5);
		fire.setFlammable(JUNIPER_LEAVES.get(), 30, 60);
		fire.setFlammable(JUNIPER_PLANKS.get(), 5, 20);
		fire.setFlammable(JUNIPER_STAIRS.get(), 5, 20);
		fire.setFlammable(JUNIPER_SLAB.get(), 5, 20);
		fire.setFlammable(JUNIPER_FENCE.get(), 5, 20);
		fire.setFlammable(JUNIPER_FENCE_GATE.get(), 5, 20);
		fire.setFlammable(STRIPPED_CYPRESS_LOG.get(), 5, 5);
		fire.setFlammable(STRIPPED_CYPRESS_WOOD.get(), 5, 5);
		fire.setFlammable(CYPRESS_LOG.get(), 5, 5);
		fire.setFlammable(CYPRESS_WOOD.get(), 5, 5);
		fire.setFlammable(CYPRESS_LEAVES.get(), 30, 60);
		fire.setFlammable(CYPRESS_PLANKS.get(), 5, 20);
		fire.setFlammable(CYPRESS_STAIRS.get(), 5, 20);
		fire.setFlammable(CYPRESS_SLAB.get(), 5, 20);
		fire.setFlammable(CYPRESS_FENCE.get(), 5, 20);
		fire.setFlammable(CYPRESS_FENCE_GATE.get(), 5, 20);
		fire.setFlammable(STRIPPED_ELDER_LOG.get(), 5, 5);
		fire.setFlammable(STRIPPED_ELDER_WOOD.get(), 5, 5);
		fire.setFlammable(ELDER_LOG.get(), 5, 5);
		fire.setFlammable(ELDER_WOOD.get(), 5, 5);
		fire.setFlammable(ELDER_LEAVES.get(), 30, 60);
		fire.setFlammable(ELDER_PLANKS.get(), 5, 20);
		fire.setFlammable(ELDER_STAIRS.get(), 5, 20);
		fire.setFlammable(ELDER_SLAB.get(), 5, 20);
		fire.setFlammable(ELDER_FENCE.get(), 5, 20);
		fire.setFlammable(ELDER_FENCE_GATE.get(), 5, 20);
		fire.setFlammable(STRIPPED_DRAGONS_BLOOD_LOG.get(), 5, 5);
		fire.setFlammable(STRIPPED_DRAGONS_BLOOD_WOOD.get(), 5, 5);
		fire.setFlammable(DRAGONS_BLOOD_LOG.get(), 5, 5);
		fire.setFlammable(DRAGONS_BLOOD_WOOD.get(), 5, 5);
		fire.setFlammable(DRAGONS_BLOOD_LEAVES.get(), 30, 60);
		fire.setFlammable(DRAGONS_BLOOD_PLANKS.get(), 5, 20);
		fire.setFlammable(DRAGONS_BLOOD_STAIRS.get(), 5, 20);
		fire.setFlammable(DRAGONS_BLOOD_SLAB.get(), 5, 20);
		fire.setFlammable(DRAGONS_BLOOD_FENCE.get(), 5, 20);
		fire.setFlammable(DRAGONS_BLOOD_FENCE_GATE.get(), 5, 20);
		fire.setFlammable(HEDGEWITCH_WOOL.get(), 30, 60);
		fire.setFlammable(ALCHEMIST_WOOL.get(), 30, 60);
		fire.setFlammable(BESMIRCHED_WOOL.get(), 30, 60);
		fire.setFlammable(HEDGEWITCH_CARPET.get(), 60, 20);
		fire.setFlammable(ALCHEMIST_CARPET.get(), 60, 20);
		fire.setFlammable(BESMIRCHED_CARPET.get(), 60, 20);
		ComposterBlock.COMPOSTABLES.put(JUNIPER_LEAVES.get().asItem(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(JUNIPER_SAPLING.get().asItem(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(CYPRESS_LEAVES.get().asItem(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(CYPRESS_SAPLING.get().asItem(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(ELDER_LEAVES.get().asItem(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(ELDER_SAPLING.get().asItem(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(DRAGONS_BLOOD_LEAVES.get().asItem(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(DRAGONS_BLOOD_SAPLING.get().asItem(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(ACONITE.get().asItem(), 0.65f);
		ComposterBlock.COMPOSTABLES.put(ACONITE_SEEDS.get().asItem(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(BELLADONNA.get().asItem(), 0.65f);
		ComposterBlock.COMPOSTABLES.put(BELLADONNA_SEEDS.get().asItem(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(GARLIC.get().asItem(), 0.65f);
		ComposterBlock.COMPOSTABLES.put(MANDRAKE_ROOT.get().asItem(), 0.65f);
		ComposterBlock.COMPOSTABLES.put(MANDRAKE_SEEDS.get().asItem(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(WOOD_ASH.get().asItem(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(DRAGONS_BLOOD_RESIN.get().asItem(), 0.65f);
		ComposterBlock.COMPOSTABLES.put(GRILLED_GARLIC.get().asItem(), 0.85f);
		ComposterBlock.COMPOSTABLES.put(GARLIC_BREAD.get().asItem(), 1f);
		ComposterBlock.COMPOSTABLES.put(WITCHBERRY.get().asItem(), 0.65f);
		ComposterBlock.COMPOSTABLES.put(WITCHBERRY_PIE.get().asItem(), 1f);
		ComposterBlock.COMPOSTABLES.put(WITCHBERRY_COOKIE.get().asItem(), 0.85f);
	}

	/** Axe stripping for Bewitchment logs and wood. */
	static void onToolModification(BlockEvent.BlockToolModificationEvent event) {
		if (event.getToolAction() != ToolActions.AXE_STRIP) {
			return;
		}
		if (strippables == null) {
			strippables = Map.of(
					JUNIPER_LOG.get(), STRIPPED_JUNIPER_LOG.get(),
					JUNIPER_WOOD.get(), STRIPPED_JUNIPER_WOOD.get(),
					CYPRESS_LOG.get(), STRIPPED_CYPRESS_LOG.get(),
					CYPRESS_WOOD.get(), STRIPPED_CYPRESS_WOOD.get(),
					ELDER_LOG.get(), STRIPPED_ELDER_LOG.get(),
					ELDER_WOOD.get(), STRIPPED_ELDER_WOOD.get(),
					DRAGONS_BLOOD_LOG.get(), STRIPPED_DRAGONS_BLOOD_LOG.get(),
					DRAGONS_BLOOD_WOOD.get(), STRIPPED_DRAGONS_BLOOD_WOOD.get());
		}
		Block stripped = strippables.get(event.getState().getBlock());
		if (stripped != null) {
			event.setFinalState(stripped.withPropertiesOf(event.getState()));
		}
	}

	/** Furnace fuel for Bewitchment items (was Fabric's FuelRegistry). */
	static void onFurnaceFuel(FurnaceFuelBurnTimeEvent event) {
		ItemStack stack = event.getItemStack();
		Item item = stack.getItem();
		if (item == JUNIPER_FENCE.get().asItem()) {
			event.setBurnTime(300);
			return;
		}
		if (item == JUNIPER_FENCE_GATE.get().asItem()) {
			event.setBurnTime(300);
			return;
		}
		if (item == CYPRESS_FENCE.get().asItem()) {
			event.setBurnTime(300);
			return;
		}
		if (item == CYPRESS_FENCE_GATE.get().asItem()) {
			event.setBurnTime(300);
			return;
		}
		if (item == ELDER_FENCE.get().asItem()) {
			event.setBurnTime(300);
			return;
		}
		if (item == ELDER_FENCE_GATE.get().asItem()) {
			event.setBurnTime(300);
			return;
		}
		if (item == DRAGONS_BLOOD_FENCE.get().asItem()) {
			event.setBurnTime(300);
			return;
		}
		if (item == DRAGONS_BLOOD_FENCE_GATE.get().asItem()) {
			event.setBurnTime(300);
			return;
		}
		if (item == SCORCHED_BRAMBLE.get().asItem()) {
			event.setBurnTime(800);
			return;
		}
		if (stack.is(BWTags.BARKS)) {
			event.setBurnTime(100);
		}
	}
}
