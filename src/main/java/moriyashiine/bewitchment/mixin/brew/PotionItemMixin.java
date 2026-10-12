/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.brew;

import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public abstract class PotionItemMixin {
	@Inject(method = "finishUsing", at = @At("HEAD"))
	private void finishUsing(ItemStack stack, Level world, LivingEntity user, CallbackInfoReturnable<ItemStack> callbackInfo) {
		if (!world.isClientSide) {
			BWComponents.POLYMORPH_COMPONENT.maybeGet(user).ifPresent(polymorphComponent -> {
				CompoundTag compound = stack.getTag();
				if (compound != null) {
					String name = compound.getString("PolymorphName");
					if (!name.isEmpty()) {
						polymorphComponent.setUuid(compound.getUUID("PolymorphUUID"));
						polymorphComponent.setName(name);
					}
				}
			});
		}
	}
}
