/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.transformation;

import moriyashiine.bewitchment.api.registry.Transformation;
import moriyashiine.bewitchment.common.entity.living.WerewolfEntity;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.world.entity.player.Player;

public class WerewolfTransformation extends Transformation {
	@Override
	public void onAdded(Player player) {
		BWComponents.ADDITIONAL_WEREWOLF_DATA_COMPONENT.get(player).setVariant(player.getRandom().nextInt(WerewolfEntity.getVariantsStatic()));
	}
}
