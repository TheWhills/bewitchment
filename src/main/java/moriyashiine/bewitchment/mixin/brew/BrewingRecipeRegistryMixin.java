/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.brew;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(PotionBrewing.class)
public abstract class BrewingRecipeRegistryMixin {
	@Inject(method = "mix", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, ordinal = 0, target = "Lnet/minecraft/world/item/alchemy/PotionUtils;setPotion(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/world/item/ItemStack;"), cancellable = true)
	private static void craft(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<ItemStack> callbackInfo) {
		if (ingredient.hasTag()) {
			CompoundTag nbt = ingredient.getTag();
			if (nbt.getBoolean("BewitchmentBrew")) {
				ItemStack potionStack = new ItemStack(input.getItem() == Items.GUNPOWDER ? Items.SPLASH_POTION : input.getItem() == Items.DRAGON_BREATH ? Items.LINGERING_POTION : Items.POTION);
				potionStack.getOrCreateTag().merge(nbt);
				callbackInfo.setReturnValue(potionStack);
			}
		}
	}
}
