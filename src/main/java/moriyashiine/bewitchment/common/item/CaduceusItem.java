/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CaduceusItem extends DiggerItem {
	public CaduceusItem(float attackDamage, float attackSpeed, Tier material, TagKey<Block> effectiveBlocks, Properties settings) {
		super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		return ItemUtils.startUsingInstantly(world, user, hand);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		if (user instanceof Player player) {
			if (!world.isClientSide && BewitchmentAPI.drainMagic(player, 2, false)) {
				LargeFireball fireball = new LargeFireball(world, user, user.getLookAngle().x, user.getLookAngle().y, user.getLookAngle().z, 1);
				fireball.setOwner(user);
				fireball.setPosRaw(fireball.getX(), fireball.getY() + 1, fireball.getZ());
				BWComponents.CADUCEUS_FIREBALL_COMPONENT.get(fireball).setFromCaduceus(true);
				world.playSound(null, user.blockPosition(), BWSoundEvents.ENTITY_GENERIC_SHOOT.get(), SoundSource.HOSTILE, 1, 1);
				world.addFreshEntity(fireball);
				stack.hurtAndBreak(1, user, stackUser -> stackUser.broadcastBreakEvent(user.getUsedItemHand()));
			}
		}
		return stack;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 24;
	}

	@Override
	public boolean isCorrectToolForDrops(BlockState state) {
		return true;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return speed * 0.75f;
	}
}
