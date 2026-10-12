/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public record AltarMapEntry(Block unformed, Block formed, Item carpet) {
}
