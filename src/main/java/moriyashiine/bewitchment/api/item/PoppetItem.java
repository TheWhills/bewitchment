/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.item;

import moriyashiine.bewitchment.common.item.TaglockItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class PoppetItem extends Item {
	public static final Predicate<DamageSource> PROTECTION_POPPET_SOURCES = source -> source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypes.FALLING_BLOCK) || source.is(DamageTypes.FALLING_STALACTITE) || source.is(DamageTypes.FALLING_ANVIL) || source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypes.CACTUS) || source.is(DamageTypes.CRAMMING) || source.is(DamageTypes.DROWN) || source.is(DamageTypes.DRY_OUT) || source.is(DamageTypes.FLY_INTO_WALL) || source.is(DamageTypes.FREEZE) || source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.LIGHTNING_BOLT) || source.is(DamageTypes.SWEET_BERRY_BUSH) || source.is(DamageTypes.STARVE);

	public final boolean worksInShelf;

	public PoppetItem(Properties settings, boolean worksInShelf) {
		super(settings);
		this.worksInShelf = worksInShelf;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
		if (TaglockItem.hasTaglock(stack)) {
			tooltip.add(Component.literal(TaglockItem.getTaglockName(stack)).withStyle(ChatFormatting.GRAY));
		}
	}
}
