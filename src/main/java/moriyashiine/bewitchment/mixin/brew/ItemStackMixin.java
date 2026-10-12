/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.brew;

import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	@Unique
	private static final MutableComponent POTION = Component.translatable("item." + Bewitchment.MOD_ID + ".potion");
	@Unique
	private static final MutableComponent SPLASH_POTION = Component.translatable("item." + Bewitchment.MOD_ID + ".splash_potion");
	@Unique
	private static final MutableComponent LINGERING_POTION = Component.translatable("item." + Bewitchment.MOD_ID + ".lingering_potion");
	@Unique
	private static final MutableComponent TIPPED_ARROW = Component.translatable("item." + Bewitchment.MOD_ID + ".tipped_arrow");

	@Shadow
	@Nullable
	public abstract CompoundTag getNbt();

	@Shadow
	public abstract Item getItem();

	@Inject(method = "getName", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, ordinal = 0, target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"), cancellable = true)
	private void getName(CallbackInfoReturnable<Component> callbackInfo) {
		CompoundTag nbt = getNbt();
		if (nbt != null && nbt.getBoolean("BewitchmentBrew")) {
			if (getItem() == Items.POTION) {
				callbackInfo.setReturnValue(POTION);
			} else if (getItem() == Items.SPLASH_POTION) {
				callbackInfo.setReturnValue(SPLASH_POTION);
			} else if (getItem() == Items.LINGERING_POTION) {
				callbackInfo.setReturnValue(LINGERING_POTION);
			} else if (getItem() == Items.TIPPED_ARROW) {
				callbackInfo.setReturnValue(TIPPED_ARROW);
			}
		}
	}
}
