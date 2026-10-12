/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BWProperties {
	public static final BooleanProperty NATURAL = BooleanProperty.create("natural");
	public static final BooleanProperty CUT = BooleanProperty.create("cut");

	public static final BooleanProperty HAS_FRUIT = BooleanProperty.create("has_fruit");

	public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 9);

	public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 3);
}
