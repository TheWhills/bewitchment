/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.brew;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.TippedArrowRecipe;

@SuppressWarnings("ConstantConditions")
@Mixin(TippedArrowRecipe.class)
public class TippedArrowRecipeMixin {
	@Inject(method = "assemble(Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
	private void craft(CraftingContainer recipeInputInventory, RegistryAccess dynamicRegistryManager, CallbackInfoReturnable<ItemStack> cir) {
		ItemStack stack = recipeInputInventory.getItem(1 + recipeInputInventory.getWidth());
		if (stack.hasTag() && stack.getTag().contains("BewitchmentBrew")) {
			int color = PotionUtils.getColor(stack);
			UUID uuid = null;
			String name = null;
			if (stack.getTag().contains("PolymorphUUID")) {
				uuid = stack.getTag().getUUID("PolymorphUUID");
				name = stack.getTag().getString("PolymorphName");
			}
			stack = cir.getReturnValue();
			stack.getOrCreateTag().putBoolean("BewitchmentBrew", true);
			stack.getOrCreateTag().putInt("CustomPotionColor", color);
			if (uuid != null) {
				stack.getOrCreateTag().putUUID("PolymorphUUID", uuid);
				stack.getOrCreateTag().putString("PolymorphName", name);
			}
			cir.setReturnValue(stack);
		}
	}
}
