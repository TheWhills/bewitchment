/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.misc;

import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

import static net.minecraft.client.renderer.Sheets.CHEST_SHEET;
import static net.minecraft.client.renderer.texture.TextureAtlas.LOCATION_BLOCKS;

public class SpriteIdentifiers {
	public static final Material JUNIPER_CHEST = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/juniper"));
	public static final Material TRAPPED_JUNIPER_CHEST = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/trapped_juniper"));
	public static final Material JUNIPER_CHEST_LEFT = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/juniper_left"));
	public static final Material TRAPPED_JUNIPER_CHEST_LEFT = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/trapped_juniper_left"));
	public static final Material JUNIPER_CHEST_RIGHT = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/juniper_right"));
	public static final Material TRAPPED_JUNIPER_CHEST_RIGHT = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/trapped_juniper_right"));

	public static final Material CYPRESS_CHEST = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/cypress"));
	public static final Material TRAPPED_CYPRESS_CHEST = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/trapped_cypress"));
	public static final Material CYPRESS_CHEST_LEFT = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/cypress_left"));
	public static final Material TRAPPED_CYPRESS_CHEST_LEFT = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/trapped_cypress_left"));
	public static final Material CYPRESS_CHEST_RIGHT = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/cypress_right"));
	public static final Material TRAPPED_CYPRESS_CHEST_RIGHT = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/trapped_cypress_right"));

	public static final Material ELDER_CHEST = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/elder"));
	public static final Material TRAPPED_ELDER_CHEST = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/trapped_elder"));
	public static final Material ELDER_CHEST_LEFT = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/elder_left"));
	public static final Material TRAPPED_ELDER_CHEST_LEFT = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/trapped_elder_left"));
	public static final Material ELDER_CHEST_RIGHT = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/elder_right"));
	public static final Material TRAPPED_ELDER_CHEST_RIGHT = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/trapped_elder_right"));

	public static final Material DRAGONS_BLOOD_CHEST = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/dragons_blood"));
	public static final Material TRAPPED_DRAGONS_BLOOD_CHEST = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/trapped_dragons_blood"));
	public static final Material DRAGONS_BLOOD_CHEST_LEFT = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/dragons_blood_left"));
	public static final Material TRAPPED_DRAGONS_BLOOD_CHEST_LEFT = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/trapped_dragons_blood_left"));
	public static final Material DRAGONS_BLOOD_CHEST_RIGHT = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/dragons_blood_right"));
	public static final Material TRAPPED_DRAGONS_BLOOD_CHEST_RIGHT = new Material(CHEST_SHEET, Bewitchment.id("entity/chest/trapped_dragons_blood_right"));

	public static final Material CAULDRON_WATER = new Material(LOCATION_BLOCKS, new ResourceLocation("block/water_still"));
}
