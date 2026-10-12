/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.block.SaltLineBlock;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.registry.BWDamageSources;
import moriyashiine.bewitchment.common.registry.BWMaterials;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
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
	@Inject(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;", at = @At("RETURN"))
	private static void getDroppedStacks(BlockState state, ServerLevel world, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack stack, CallbackInfoReturnable<List<ItemStack>> callbackInfo) {
		if (entity instanceof LivingEntity living) {
			boolean damage = false;
			if (state.getBlock() instanceof SaltLineBlock && BewitchmentAPI.isWeakToSilver(living)) {
				damage = true;
			} else if (state.getBlock() == BWObjects.GARLIC_CROP && BewitchmentAPI.isVampire(entity, true)) {
				damage = true;
			} else if (state.getBlock() == BWObjects.ACONITE_CROP && BewitchmentAPI.isWerewolf(entity, true)) {
				damage = true;
			}
			if (damage) {
				entity.hurt(BWDamageSources.create(world, BWDamageSources.MAGIC_COPY), living.getMaxHealth() * 1 / 2f);
			}
			List<ItemStack> drops = callbackInfo.getReturnValue();
			if (!drops.isEmpty() && !EnchantmentHelper.getEnchantments(stack).containsKey(Enchantments.SILK_TOUCH) && state.getBlock() instanceof CropBlock crop && crop.getAge(state) == crop.getMaxAge() && BWUtil.getArmorPieces(living, armorStack -> armorStack.getItem() instanceof ArmorItem armorItem && armorItem.getMaterial() == BWMaterials.HEDGEWITCH_ARMOR) >= 3) {
				drops.replaceAll(itemStack -> new ItemStack(itemStack.getItem(), itemStack.getCount() + 1));
			}
		}
	}
}
