/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import com.google.common.collect.Sets;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.block.entity.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import java.util.HashSet;
import java.util.Set;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;
import net.minecraftforge.registries.ForgeRegistries;

public class BWBlockEntityTypes {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Bewitchment.MOD_ID);

	public static final RegistryObject<BlockEntityType<BWChestBlockEntity>> BW_CHEST = create("bw_chest", () -> BlockEntityType.Builder.of(BWChestBlockEntity::new, BWObjects.JUNIPER_CHEST.get(), BWObjects.TRAPPED_JUNIPER_CHEST.get(), BWObjects.CYPRESS_CHEST.get(), BWObjects.TRAPPED_CYPRESS_CHEST.get(), BWObjects.ELDER_CHEST.get(), BWObjects.TRAPPED_ELDER_CHEST.get()).build(null));
	public static final RegistryObject<BlockEntityType<WitchAltarBlockEntity>> WITCH_ALTAR = create("witch_altar", () -> BlockEntityType.Builder.of(WitchAltarBlockEntity::new, merge(BWObjects.STONE_WITCH_ALTAR.get(), BWObjects.MOSSY_COBBLESTONE_WITCH_ALTAR.get(), BWObjects.PRISMARINE_WITCH_ALTAR.get(), BWObjects.NETHER_BRICK_WITCH_ALTAR.get(), BWObjects.BLACKSTONE_WITCH_ALTAR.get(), BWObjects.GOLDEN_WITCH_ALTAR.get(), BWObjects.END_STONE_WITCH_ALTAR.get(), BWObjects.OBSIDIAN_WITCH_ALTAR.get(), BWObjects.PURPUR_WITCH_ALTAR.get())).build(null));
	public static final RegistryObject<BlockEntityType<WitchCauldronBlockEntity>> WITCH_CAULDRON = create("witch_cauldron", () -> BlockEntityType.Builder.of(WitchCauldronBlockEntity::new, BWObjects.WITCH_CAULDRON.get()).build(null));
	public static final RegistryObject<BlockEntityType<GlyphBlockEntity>> GLYPH = create("glyph", () -> BlockEntityType.Builder.of(GlyphBlockEntity::new, BWObjects.GOLDEN_GLYPH.get()).build(null));
	public static final RegistryObject<BlockEntityType<BrazierBlockEntity>> BRAZIER = create("brazier", () -> BlockEntityType.Builder.of(BrazierBlockEntity::new, BWObjects.BRAZIER.get()).build(null));
	public static final RegistryObject<BlockEntityType<TaglockHolderBlockEntity>> TAGLOCK_HOLDER = create("taglock_holder", () -> BlockEntityType.Builder.of(TaglockHolderBlockEntity::new, BWObjects.JUNIPER_FENCE_GATE.get(), BWObjects.JUNIPER_PRESSURE_PLATE.get(), BWObjects.JUNIPER_BUTTON.get(), BWObjects.JUNIPER_TRAPDOOR.get(), BWObjects.JUNIPER_DOOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<LockableBlockEntity>> LOCKABLE = create("lockable", () -> BlockEntityType.Builder.of(LockableBlockEntity::new, BWObjects.ELDER_FENCE_GATE.get(), BWObjects.ELDER_PRESSURE_PLATE.get(), BWObjects.ELDER_BUTTON.get(), BWObjects.ELDER_TRAPDOOR.get(), BWObjects.ELDER_DOOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<SigilBlockEntity>> SIGIL = create("sigil", () -> BlockEntityType.Builder.of(SigilBlockEntity::new, BWObjects.SIGIL.get(), BWObjects.DRAGONS_BLOOD_FENCE_GATE.get(), BWObjects.DRAGONS_BLOOD_PRESSURE_PLATE.get(), BWObjects.DRAGONS_BLOOD_BUTTON.get(), BWObjects.DRAGONS_BLOOD_TRAPDOOR.get(), BWObjects.DRAGONS_BLOOD_DOOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<PoppetShelfBlockEntity>> POPPET_SHELF = create("poppet_shelf", () -> BlockEntityType.Builder.of(PoppetShelfBlockEntity::new, BWObjects.OAK_POPPET_SHELF.get(), BWObjects.SPRUCE_POPPET_SHELF.get(), BWObjects.BIRCH_POPPET_SHELF.get(), BWObjects.JUNGLE_POPPET_SHELF.get(), BWObjects.ACACIA_POPPET_SHELF.get(), BWObjects.DARK_OAK_POPPET_SHELF.get(), BWObjects.CRIMSON_POPPET_SHELF.get(), BWObjects.WARPED_POPPET_SHELF.get(), BWObjects.JUNIPER_POPPET_SHELF.get(), BWObjects.CYPRESS_POPPET_SHELF.get(), BWObjects.ELDER_POPPET_SHELF.get(), BWObjects.DRAGONS_BLOOD_POPPET_SHELF.get()).build(null));
	public static final RegistryObject<BlockEntityType<JuniperChestBlockEntity>> JUNIPER_CHEST = create("juniper_chest", () -> BlockEntityType.Builder.of(JuniperChestBlockEntity::new, BWObjects.JUNIPER_CHEST.get(), BWObjects.TRAPPED_JUNIPER_CHEST.get()).build(null));
	public static final RegistryObject<BlockEntityType<ElderChestBlockEntity>> ELDER_CHEST = create("elder_chest", () -> BlockEntityType.Builder.of(ElderChestBlockEntity::new, BWObjects.ELDER_CHEST.get(), BWObjects.TRAPPED_ELDER_CHEST.get()).build(null));
	public static final RegistryObject<BlockEntityType<DragonsBloodChestBlockEntity>> DRAGONS_BLOOD_CHEST = create("dragons_blood_chest", () -> BlockEntityType.Builder.of(DragonsBloodChestBlockEntity::new, BWObjects.DRAGONS_BLOOD_CHEST.get(), BWObjects.TRAPPED_DRAGONS_BLOOD_CHEST.get()).build(null));

	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> create(String name, Supplier<BlockEntityType<T>> type) {
		return BLOCK_ENTITY_TYPES.register(name, type);
	}

	private static Block[] merge(Block[]... blockArrays) {
		Set<Block> merged = new HashSet<>();
		for (Block[] blockArray : blockArrays) {
			merged.addAll(Sets.newHashSet(blockArray));
		}
		return merged.toArray(new Block[0]);
	}
}
