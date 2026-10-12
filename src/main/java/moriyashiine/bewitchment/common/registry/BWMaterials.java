/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;

public class BWMaterials {
	public static final ArmorMaterial HEDGEWITCH_ARMOR = new ArmorMaterial() {
		@Override
		public int getDurabilityForType(ArmorItem.Type type) {
			return ArmorMaterials.CHAIN.getDurabilityForType(type);
		}

		@Override
		public int getDefenseForType(ArmorItem.Type type) {
			return ArmorMaterials.CHAIN.getDefenseForType(type);
		}

		@Override
		public int getEnchantmentValue() {
			return ArmorMaterials.GOLD.getEnchantmentValue();
		}

		@Override
		public SoundEvent getEquipSound() {
			return ArmorMaterials.LEATHER.getEquipSound();
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.of(BWObjects.HEDGEWITCH_WOOL.get());
		}

		@Override
		public String getName() {
			return "hedgewitch";
		}

		@Override
		public float getToughness() {
			return ArmorMaterials.CHAIN.getToughness();
		}

		@Override
		public float getKnockbackResistance() {
			return ArmorMaterials.CHAIN.getKnockbackResistance();
		}
	};

	public static final ArmorMaterial ALCHEMIST_ARMOR = new ArmorMaterial() {
		@Override
		public int getDurabilityForType(ArmorItem.Type type) {
			return HEDGEWITCH_ARMOR.getDurabilityForType(type);
		}

		@Override
		public int getDefenseForType(ArmorItem.Type type) {
			return HEDGEWITCH_ARMOR.getDefenseForType(type);
		}

		@Override
		public int getEnchantmentValue() {
			return HEDGEWITCH_ARMOR.getEnchantmentValue();
		}

		@Override
		public SoundEvent getEquipSound() {
			return HEDGEWITCH_ARMOR.getEquipSound();
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.of(BWObjects.ALCHEMIST_WOOL.get());
		}

		@Override
		public String getName() {
			return "alchemist";
		}

		@Override
		public float getToughness() {
			return HEDGEWITCH_ARMOR.getToughness();
		}

		@Override
		public float getKnockbackResistance() {
			return HEDGEWITCH_ARMOR.getKnockbackResistance();
		}
	};

	public static final ArmorMaterial BESMIRCHED_ARMOR = new ArmorMaterial() {
		@Override
		public int getDurabilityForType(ArmorItem.Type type) {
			return HEDGEWITCH_ARMOR.getDurabilityForType(type);
		}

		@Override
		public int getDefenseForType(ArmorItem.Type type) {
			return HEDGEWITCH_ARMOR.getDefenseForType(type);
		}

		@Override
		public int getEnchantmentValue() {
			return HEDGEWITCH_ARMOR.getEnchantmentValue();
		}

		@Override
		public SoundEvent getEquipSound() {
			return HEDGEWITCH_ARMOR.getEquipSound();
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.of(BWObjects.BESMIRCHED_WOOL.get());
		}

		@Override
		public String getName() {
			return "besmirched";
		}

		@Override
		public float getToughness() {
			return HEDGEWITCH_ARMOR.getToughness();
		}

		@Override
		public float getKnockbackResistance() {
			return HEDGEWITCH_ARMOR.getKnockbackResistance();
		}
	};

	public static final ArmorMaterial HARBINGER_ARMOR = new ArmorMaterial() {
		@Override
		public int getDurabilityForType(ArmorItem.Type type) {
			return HEDGEWITCH_ARMOR.getDurabilityForType(type);
		}

		@Override
		public int getDefenseForType(ArmorItem.Type type) {
			return HEDGEWITCH_ARMOR.getDefenseForType(type);
		}

		@Override
		public int getEnchantmentValue() {
			return HEDGEWITCH_ARMOR.getEnchantmentValue();
		}

		@Override
		public SoundEvent getEquipSound() {
			return HEDGEWITCH_ARMOR.getEquipSound();
		}

		@Override
		public Ingredient getRepairIngredient() {
			return ArmorMaterials.NETHERITE.getRepairIngredient();
		}

		@Override
		public String getName() {
			return "harbinger";
		}

		@Override
		public float getToughness() {
			return ArmorMaterials.NETHERITE.getToughness();
		}

		@Override
		public float getKnockbackResistance() {
			return ArmorMaterials.NETHERITE.getKnockbackResistance();
		}
	};

	public static final Tier SILVER_TOOL = new Tier() {
		@Override
		public int getUses() {
			return Tiers.IRON.getUses();
		}

		@Override
		public float getSpeed() {
			return Tiers.GOLD.getSpeed();
		}

		@Override
		public float getAttackDamageBonus() {
			return Tiers.IRON.getAttackDamageBonus();
		}

		@Override
		public int getLevel() {
			return Tiers.IRON.getLevel();
		}

		@Override
		public int getEnchantmentValue() {
			return Tiers.GOLD.getEnchantmentValue();
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.of(BWObjects.SILVER_INGOT.get());
		}
	};
}
