/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.contract;

import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWContracts;
import moriyashiine.bewitchment.common.registry.BWTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Block.class)
public abstract class BlockMixin {
	@Inject(method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;", at = @At("RETURN"))
	private static void getDroppedStacks(BlockState state, ServerLevel world, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack stack, CallbackInfoReturnable<List<ItemStack>> callbackInfo) {
		if (entity instanceof Player player) {
			BWComponents.CONTRACTS_COMPONENT.maybeGet(player).ifPresent(contractsComponent -> {
				List<ItemStack> drops = callbackInfo.getReturnValue();
				if (!drops.isEmpty() && !EnchantmentHelper.getEnchantments(stack).containsKey(Enchantments.SILK_TOUCH)) {
					if (contractsComponent.hasContract(BWContracts.GREED)) {
						for (int i = 0; i < drops.size(); i++) {
							if (state.is(BWTags.ORES)) {
								for (Recipe<?> smeltingRecipe : world.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING)) {
									for (Ingredient ingredient : smeltingRecipe.getIngredients()) {
										if (ingredient.test(drops.get(i))) {
											drops.set(i, new ItemStack(smeltingRecipe.getResultItem(world.registryAccess()).getItem(), smeltingRecipe.getResultItem(world.registryAccess()).getCount() * drops.get(i).getCount()));
										}
									}
								}
								drops.set(i, new ItemStack(drops.get(i).getItem(), drops.get(i).getCount() * 2));
							}
						}
					}
				}
			});
		}
	}
}
