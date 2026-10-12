/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.fortune;

import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWFortunes;
import moriyashiine.bewitchment.common.registry.BWObjects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(Block.class)
public abstract class BlockMixin {
	@Inject(method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;", at = @At("RETURN"))
	private static void getDroppedStacks(BlockState state, ServerLevel world, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity entity, ItemStack stack, CallbackInfoReturnable<List<ItemStack>> callbackInfo) {
		if (entity instanceof Player player) {
			BWComponents.FORTUNE_COMPONENT.maybeGet(player).ifPresent(fortuneComponent -> {
				List<ItemStack> drops = callbackInfo.getReturnValue();
				if (!drops.isEmpty() && !EnchantmentHelper.getEnchantments(stack).containsKey(Enchantments.SILK_TOUCH)) {
					if (fortuneComponent.getFortune() != null) {
						if (fortuneComponent.getFortune().fortune == BWFortunes.TREASURE.get() && world.random.nextFloat() < 1 / 25f) {
							Set<ItemStack> treasure = new HashSet<>();
							for (int i = 0; i < world.random.nextInt(1) + 1; i++) {
								switch (world.random.nextInt(4)) {
									case 0 ->
											treasure.add(new ItemStack(Items.DIAMOND, Mth.nextInt(world.random, 1, 3)));
									case 1 ->
											treasure.add(new ItemStack(Items.GOLD_INGOT, Mth.nextInt(world.random, 1, 5)));
									case 2 ->
											treasure.add(new ItemStack(Items.IRON_INGOT, Mth.nextInt(world.random, 2, 10)));
									case 3 ->
											treasure.add(new ItemStack(BWObjects.SILVER_INGOT.get(), Mth.nextInt(world.random, 1, 5)));
									default -> treasure.add(ItemStack.EMPTY);
								}
							}
							drops.addAll(treasure);
							fortuneComponent.getFortune().duration = 0;
						} else if (fortuneComponent.getFortune().fortune == BWFortunes.INFESTED.get() && world.random.nextFloat() < 1 / 25f) {
							Silverfish silverfish = EntityType.SILVERFISH.create(world);
							if (silverfish != null) {
								silverfish.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.EVENT, null, null);
								silverfish.absMoveTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, world.random.nextFloat() * 360, 0);
								world.addFreshEntity(silverfish);
								fortuneComponent.getFortune().duration = 0;
							}
						}
					}
				}
			});
		}
	}
}
