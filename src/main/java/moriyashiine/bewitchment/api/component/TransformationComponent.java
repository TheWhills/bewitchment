/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.component;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import dev.emi.stepheightentityattribute.StepHeightEntityAttributeMain;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.event.AllowVampireBurn;
import moriyashiine.bewitchment.api.event.AllowVampireHeal;
import moriyashiine.bewitchment.api.event.OnTransformationSet;
import moriyashiine.bewitchment.api.registry.Transformation;
import moriyashiine.bewitchment.common.component.entity.AdditionalWerewolfDataComponent;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.packet.TransformationAbilityPacket;
import moriyashiine.bewitchment.common.registry.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TransformationComponent implements AutoSyncedComponent, ServerTickingComponent {
	private static final AttributeModifier VAMPIRE_ATTACK_DAMAGE_MODIFIER_0 = new AttributeModifier(UUID.fromString("066862f6-989c-4f35-ac6d-2696b91a1a5b"), "Transformation modifier", 2, AttributeModifier.Operation.ADDITION);
	private static final AttributeModifier VAMPIRE_ATTACK_DAMAGE_MODIFIER_1 = new AttributeModifier(UUID.fromString("d2be3564-97e7-42c9-88c5-6b753472e37f"), "Transformation modifier", 4, AttributeModifier.Operation.ADDITION);
	private static final AttributeModifier VAMPIRE_MOVEMENT_SPEED_MODIFIER_0 = new AttributeModifier(UUID.fromString("a782c03d-af7b-4eb7-b997-dd396bfdc7a0"), "Transformation modifier", 0.04, AttributeModifier.Operation.ADDITION);
	private static final AttributeModifier VAMPIRE_MOVEMENT_SPEED_MODIFIER_1 = new AttributeModifier(UUID.fromString("7c7a61eb-83e8-4e85-94d6-a410a4153d6d"), "Transformation modifier", 0.08, AttributeModifier.Operation.ADDITION);

	private static final AttributeModifier WEREWOLF_ATTACK_SPEED_MODIFIER = new AttributeModifier(UUID.fromString("db2512a4-655d-4843-8b06-619748a33954"), "Transformation modifier", -2, AttributeModifier.Operation.ADDITION);
	private static final AttributeModifier WEREWOLF_ARMOR_MODIFIER = new AttributeModifier(UUID.fromString("f00b0b0f-8ad6-4a2f-bdf5-6c337ffee56c"), "Transformation modifier", 16, AttributeModifier.Operation.ADDITION);
	private static final AttributeModifier WEREWOLF_ATTACK_RANGE_MODIFIER = new AttributeModifier(UUID.fromString("ae0e4c0a-971f-4629-99ad-60c115112c1d"), "Transformation modifier", 1, AttributeModifier.Operation.ADDITION);
	private static final AttributeModifier WEREWOLF_REACH_MODIFIER = new AttributeModifier(UUID.fromString("4c6d90ab-41ad-4d8a-b77a-7329361d3a7b"), "Transformation modifier", 1, AttributeModifier.Operation.ADDITION);
	private static final AttributeModifier WEREWOLF_STEP_HEIGHT_MODIFIER = new AttributeModifier(UUID.fromString("af386c1c-b4fc-429d-97b6-b2559826fa9d"), "Transformation modifier", 0.4, AttributeModifier.Operation.ADDITION);

	private static final AttributeModifier WEREWOLF_ATTACK_DAMAGE_MODIFIER_0 = new AttributeModifier(UUID.fromString("06861902-ebc4-4e6e-956c-59c1ae3085c7"), "Transformation modifier", 15, AttributeModifier.Operation.ADDITION);
	private static final AttributeModifier WEREWOLF_ATTACK_DAMAGE_MODIFIER_1 = new AttributeModifier(UUID.fromString("10c0bedf-bde5-4cae-8acc-90b1204731dd"), "Transformation modifier", 30, AttributeModifier.Operation.ADDITION);
	private static final AttributeModifier WEREWOLF_ARMOR_TOUGHNESS_MODIFIER_0 = new AttributeModifier(UUID.fromString("44f17821-1e30-426f-81d7-cd1da88fa584"), "Transformation modifier", 8, AttributeModifier.Operation.ADDITION);
	private static final AttributeModifier WEREWOLF_ARMOR_TOUGHNESS_MODIFIER_1 = new AttributeModifier(UUID.fromString("edfd078d-e25c-4e27-ad91-c2b32037c8be"), "Transformation modifier", 16, AttributeModifier.Operation.ADDITION);
	private static final AttributeModifier WEREWOLF_MOVEMENT_SPEED_MODIFIER_0 = new AttributeModifier(UUID.fromString("e26a276a-86cd-44db-9091-acd42fc00d95"), "Transformation modifier", 0.08, AttributeModifier.Operation.ADDITION);
	private static final AttributeModifier WEREWOLF_MOVEMENT_SPEED_MODIFIER_1 = new AttributeModifier(UUID.fromString("718104a6-aa19-4b53-bad9-1f9edd46d38a"), "Transformation modifier", 0.16, AttributeModifier.Operation.ADDITION);

	private final Player obj;
	private Transformation transformation = BWTransformations.HUMAN.get();
	private boolean alternateForm = false;

	public TransformationComponent(Player obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		if (tag.contains("Transformation")) {
			setTransformation(BWRegistries.TRANSFORMATION.get(new ResourceLocation(tag.getString("Transformation"))));
		}
		setAlternateForm(tag.getBoolean("AlternateForm"));
	}

	@SuppressWarnings({"ConstantConditions"})
	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putString("Transformation", BWRegistries.TRANSFORMATION.getKey(getTransformation()).toString());
		tag.putBoolean("AlternateForm", isAlternateForm());
	}

	@Override
	public void serverTick() {
		boolean vampire = BewitchmentAPI.isVampire(obj, true);
		if (vampire) {
			boolean pledgedToLilith = BewitchmentAPI.isPledged(obj, BWPledges.LILITH);
			if (obj.getItemBySlot(EquipmentSlot.HEAD).is(BWTags.SUN_GLASSES)) {
				obj.removeEffect(MobEffects.NIGHT_VISION);
			} else {
				obj.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, -1, 0, true, false));
			}
			if (BWComponents.RESPAWN_TIMER_COMPONENT.get(obj).getRespawnTimer() <= 0 && obj.level().isDay() && !obj.level().isRaining() && obj.level().canSeeSky(obj.blockPosition()) && AllowVampireBurn.EVENT.invoker().allowBurn(obj)) {
				obj.setSecondsOnFire(8);
			}
			FoodData hungerManager = obj.getFoodData();
			if (BWComponents.BLOOD_COMPONENT.get(obj).getBlood() > 0) {
				if (AllowVampireHeal.EVENT.invoker().allowHeal(obj, pledgedToLilith)) {
					if (obj.tickCount % (pledgedToLilith ? 30 : 40) == 0) {
						if (obj.getHealth() < obj.getMaxHealth()) {
							obj.heal(1);
							hungerManager.addExhaustion(3);
						}
						if ((hungerManager.needsFood() || hungerManager.getSaturationLevel() < 10) && BWComponents.BLOOD_COMPONENT.get(obj).drainBlood(1, false)) {
							hungerManager.eat(1, 20);
						}
					}
				}
			} else {
				if (isAlternateForm()) {
					TransformationAbilityPacket.useAbility(obj, true);
				}
				hungerManager.addExhaustion(Float.MAX_VALUE);
			}
			if (isAlternateForm()) {
				hungerManager.addExhaustion(0.5f);
				if (!pledgedToLilith) {
					TransformationAbilityPacket.useAbility(obj, true);
				}
			}
		}
		if (BewitchmentAPI.isWerewolf(obj, true)) {
			AdditionalWerewolfDataComponent additionalWerewolfDataComponent = BWComponents.ADDITIONAL_WEREWOLF_DATA_COMPONENT.get(obj);
			boolean forced = additionalWerewolfDataComponent.isForcedTransformation();
			if (!obj.isCreative() && !obj.isSpectator() && !isAlternateForm() && obj.level().isNight() && BewitchmentAPI.getMoonPhase(obj.level()) == 0 && obj.level().canSeeSky(obj.blockPosition())) {
				TransformationAbilityPacket.useAbility(obj, true);
				additionalWerewolfDataComponent.setForcedTransformation(true);
			} else if (isAlternateForm() && forced && (obj.level().isDay() || BewitchmentAPI.getMoonPhase(obj.level()) != 0)) {
				TransformationAbilityPacket.useAbility(obj, true);
				additionalWerewolfDataComponent.setForcedTransformation(false);
			}
			if (isAlternateForm()) {
				obj.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, -1, 0, true, false));
				Set<ItemStack> toDrop = new HashSet<>();
				obj.getArmorSlots().forEach(stack -> {
					if (!stack.isEmpty()) {
						toDrop.add(stack);
					}
				});
				if (BWUtil.isTool(obj.getMainHandItem())) {
					toDrop.add(obj.getMainHandItem());
				}
				if (BWUtil.isTool(obj.getOffhandItem())) {
					toDrop.add(obj.getOffhandItem());
				}
				toDrop.forEach(stack -> {
					int firstEmptyInventorySlot = getFirstEmptyInventorySlot();
					if (firstEmptyInventorySlot != -1) {
						obj.getInventory().setItem(firstEmptyInventorySlot, stack.split(stack.getCount()));
					} else {
						obj.spawnAtLocation(stack.split(stack.getCount()));
					}
				});
				if (!forced && !BewitchmentAPI.isPledged(obj, BWPledges.HERNE)) {
					TransformationAbilityPacket.useAbility(obj, true);
				}
			}
		}
	}

	public Transformation getTransformation() {
		return transformation;
	}

	public void setTransformation(Transformation transformation) {
		OnTransformationSet.EVENT.invoker().onTransformationSet(obj, transformation);
		this.transformation = transformation;
		BWComponents.TRANSFORMATION_COMPONENT.sync(obj);
		updateAttributes();
	}

	public boolean isAlternateForm() {
		return alternateForm;
	}

	public void setAlternateForm(boolean alternateForm) {
		this.alternateForm = alternateForm;
		BWComponents.TRANSFORMATION_COMPONENT.sync(obj);
		updateAttributes();
	}

	@SuppressWarnings("ConstantConditions")
	public void updateAttributes() {
		boolean vampire = BewitchmentAPI.isVampire(obj, true);
		boolean werewolfBeast = BewitchmentAPI.isWerewolf(obj, false);
		AttributeInstance attackDamageAttribute = obj.getAttribute(Attributes.ATTACK_DAMAGE);
		AttributeInstance attackSpeedAttribute = obj.getAttribute(Attributes.ATTACK_SPEED);
		AttributeInstance armorAttribute = obj.getAttribute(Attributes.ARMOR);
		AttributeInstance armorToughnessAttribute = obj.getAttribute(Attributes.ARMOR_TOUGHNESS);
		AttributeInstance movementSpeedAttribute = obj.getAttribute(Attributes.MOVEMENT_SPEED);
		AttributeInstance attackRange = obj.getAttribute(ReachEntityAttributes.ATTACK_RANGE);
		AttributeInstance reach = obj.getAttribute(ReachEntityAttributes.REACH);
		AttributeInstance stepHeight = obj.getAttribute(StepHeightEntityAttributeMain.STEP_HEIGHT);
		boolean shouldHave = vampire && !BewitchmentAPI.isPledged(obj, BWPledges.LILITH);
		if (shouldHave && !attackDamageAttribute.hasModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_0)) {
			attackDamageAttribute.addPermanentModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_0);
			movementSpeedAttribute.addPermanentModifier(VAMPIRE_MOVEMENT_SPEED_MODIFIER_0);
		} else if (!shouldHave && attackDamageAttribute.hasModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_0)) {
			attackDamageAttribute.removeModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_0);
			movementSpeedAttribute.removeModifier(VAMPIRE_MOVEMENT_SPEED_MODIFIER_0);
		}
		shouldHave = vampire && BewitchmentAPI.isPledged(obj, BWPledges.LILITH);
		if (shouldHave && !attackDamageAttribute.hasModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_1)) {
			attackDamageAttribute.addPermanentModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_1);
			movementSpeedAttribute.addPermanentModifier(VAMPIRE_MOVEMENT_SPEED_MODIFIER_1);
		} else if (!shouldHave && attackDamageAttribute.hasModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_1)) {
			attackDamageAttribute.removeModifier(VAMPIRE_ATTACK_DAMAGE_MODIFIER_1);
			movementSpeedAttribute.removeModifier(VAMPIRE_MOVEMENT_SPEED_MODIFIER_1);
		}
		shouldHave = werewolfBeast;
		if (shouldHave && !attackSpeedAttribute.hasModifier(WEREWOLF_ATTACK_SPEED_MODIFIER)) {
			attackSpeedAttribute.addPermanentModifier(WEREWOLF_ATTACK_SPEED_MODIFIER);
			armorAttribute.addPermanentModifier(WEREWOLF_ARMOR_MODIFIER);
			attackRange.addPermanentModifier(WEREWOLF_ATTACK_RANGE_MODIFIER);
			reach.addPermanentModifier(WEREWOLF_REACH_MODIFIER);
			stepHeight.addPermanentModifier(WEREWOLF_STEP_HEIGHT_MODIFIER);
		} else if (!shouldHave && attackSpeedAttribute.hasModifier(WEREWOLF_ATTACK_SPEED_MODIFIER)) {
			attackSpeedAttribute.removeModifier(WEREWOLF_ATTACK_SPEED_MODIFIER);
			armorAttribute.removeModifier(WEREWOLF_ARMOR_MODIFIER);
			attackRange.removeModifier(WEREWOLF_ATTACK_RANGE_MODIFIER);
			reach.removeModifier(WEREWOLF_REACH_MODIFIER);
			stepHeight.removeModifier(WEREWOLF_STEP_HEIGHT_MODIFIER);
		}
		shouldHave = werewolfBeast && !BewitchmentAPI.isPledged(obj, BWPledges.HERNE);
		if (shouldHave && !attackDamageAttribute.hasModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_0)) {
			attackDamageAttribute.addPermanentModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_0);
			armorToughnessAttribute.addPermanentModifier(WEREWOLF_ARMOR_TOUGHNESS_MODIFIER_0);
			movementSpeedAttribute.addPermanentModifier(WEREWOLF_MOVEMENT_SPEED_MODIFIER_0);
		} else if (!shouldHave && attackDamageAttribute.hasModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_0)) {
			attackDamageAttribute.removeModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_0);
			armorToughnessAttribute.removeModifier(WEREWOLF_ARMOR_TOUGHNESS_MODIFIER_0);
			movementSpeedAttribute.removeModifier(WEREWOLF_MOVEMENT_SPEED_MODIFIER_0);
		}
		shouldHave = werewolfBeast && BewitchmentAPI.isPledged(obj, BWPledges.HERNE);
		if (shouldHave && !attackDamageAttribute.hasModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_1)) {
			attackDamageAttribute.addPermanentModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_1);
			armorToughnessAttribute.addPermanentModifier(WEREWOLF_ARMOR_TOUGHNESS_MODIFIER_1);
			movementSpeedAttribute.addPermanentModifier(WEREWOLF_MOVEMENT_SPEED_MODIFIER_1);
		} else if (!shouldHave && attackDamageAttribute.hasModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_1)) {
			attackDamageAttribute.removeModifier(WEREWOLF_ATTACK_DAMAGE_MODIFIER_1);
			armorToughnessAttribute.removeModifier(WEREWOLF_ARMOR_TOUGHNESS_MODIFIER_1);
			movementSpeedAttribute.removeModifier(WEREWOLF_MOVEMENT_SPEED_MODIFIER_1);
		}
	}

	private int getFirstEmptyInventorySlot() {
		for (int i = 0; i < obj.getInventory().items.size(); i++) {
			if (!Inventory.isHotbarSlot(i) && obj.getInventory().items.get(i).isEmpty()) {
				return i;
			}
		}
		return -1;
	}
}
