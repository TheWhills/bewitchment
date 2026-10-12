/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.world;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Rolls another loot table once and adds its drops (grass/fern seeds, nether fortress chest
 * extras). Replaces Fabric's LootTableEvents.MODIFY, which added a pool referencing the table.
 */
public class AddTableLootModifier extends LootModifier {
	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Bewitchment.MOD_ID);

	public static final Supplier<Codec<AddTableLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(instance -> codecStart(instance)
			.and(ResourceLocation.CODEC.fieldOf("table").forGetter(modifier -> modifier.table))
			.apply(instance, AddTableLootModifier::new)));

	public static final RegistryObject<Codec<AddTableLootModifier>> ADD_TABLE = SERIALIZERS.register("add_table", CODEC);

	private final ResourceLocation table;

	public AddTableLootModifier(LootItemCondition[] conditions, ResourceLocation table) {
		super(conditions);
		this.table = table;
	}

	@NotNull
	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		context.getResolver().getLootTable(table).getRandomItemsRaw(context, generatedLoot::add);
		return generatedLoot;
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return CODEC.get();
	}
}
