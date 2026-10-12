/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class BWFoodComponents {
	public static final FoodProperties DEMON_HEART = new FoodProperties.Builder().nutrition(8).saturationMod(0.6f).meat().alwaysEat().effect(() -> new MobEffectInstance(BWStatusEffects.CORRUPTION.get(), 1), 1).effect(new MobEffectInstance(MobEffects.WITHER, 800, 2), 1).effect(new MobEffectInstance(MobEffects.CONFUSION, 800, 2), 1).effect(new MobEffectInstance(MobEffects.HUNGER, 800, 2), 1).build();

	public static final FoodProperties GARLIC_BREAD = new FoodProperties.Builder().nutrition(10).saturationMod(0.5f).build();

	public static final FoodProperties WITCHBERRY = new FoodProperties.Builder().nutrition(3).saturationMod(0.6f).alwaysEat().build();
	public static final FoodProperties WITCHBERRY_PIE = new FoodProperties.Builder().nutrition(8).saturationMod(0.3f).alwaysEat().build();
	public static final FoodProperties WITCHBERRY_COOKIE = new FoodProperties.Builder().nutrition(2).saturationMod(0.1f).alwaysEat().build();
}
