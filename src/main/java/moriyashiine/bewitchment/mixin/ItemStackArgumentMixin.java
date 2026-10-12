/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import com.mojang.authlib.GameProfile;
import moriyashiine.bewitchment.common.item.TaglockItem;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(ItemInput.class)
public class ItemStackArgumentMixin {
	@Inject(method = "createItemStack", at = @At("RETURN"))
	private void createStack(int amount, boolean checkOverstack, CallbackInfoReturnable<ItemStack> callbackInfo) {
		ItemStack stack = callbackInfo.getReturnValue();
		if (stack.getItem() instanceof TaglockItem && stack.hasTag() && stack.getTag().contains("OwnerName") && !stack.getTag().contains("OwnerUUID")) {
			SkullBlockEntity.updateGameprofile(new GameProfile(null, stack.getTag().getString("OwnerName")), gameProfile -> {
				stack.getTag().putUUID("OwnerUUID", gameProfile.getId());
				stack.getTag().putBoolean("FromPlayer", true);
			});
		}
	}
}
