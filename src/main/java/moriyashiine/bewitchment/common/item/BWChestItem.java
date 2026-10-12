/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.item;

import moriyashiine.bewitchment.client.render.BWChestItemRenderer;
import moriyashiine.bewitchment.common.block.entity.BWChestBlockEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

/** Item form of a Bewitchment chest; drawn by rendering the chest block entity. */
public class BWChestItem extends BlockItem {
	private final BWChestBlockEntity.Type chestType;
	private final boolean trapped;

	public BWChestItem(Block block, Properties properties, BWChestBlockEntity.Type chestType, boolean trapped) {
		super(block, properties);
		this.chestType = chestType;
		this.trapped = trapped;
	}

	public BWChestBlockEntity.Type getChestType() {
		return chestType;
	}

	public boolean isTrapped() {
		return trapped;
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public BWChestItemRenderer getCustomRenderer() {
				return BWChestItemRenderer.get();
			}
		});
	}
}
