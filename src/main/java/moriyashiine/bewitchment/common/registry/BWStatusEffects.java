/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.statuseffect.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import java.util.LinkedHashMap;
import java.util.Map;

public class BWStatusEffects {
	private static final Map<MobEffect, ResourceLocation> STATUS_EFFECTS = new LinkedHashMap<>();

	public static final MobEffect CLIMBING = create("climbing", new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0x605448));
	public static final MobEffect CORROSION = create("corrosion", new EmptyStatusEffect(MobEffectCategory.HARMFUL, 0x6fc536).addAttributeModifier(Attributes.ARMOR, "5b5df91d-001a-464f-a773-ab4ccd62e09f", -3, AttributeModifier.Operation.ADDITION));
	public static final MobEffect CORRUPTION = create("corruption", new CorruptionStatusEffect(MobEffectCategory.HARMFUL, 0x600000));
	public static final MobEffect DEAFENED = create("deafened", new EmptyStatusEffect(MobEffectCategory.HARMFUL, 0x676c77));
	public static final MobEffect DEFLECTION = create("deflection", new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0x969696));
	public static final MobEffect DISJUNCTION = create("disjunction", new DisjunctionStatusEffect(MobEffectCategory.NEUTRAL, 0xb154a9));
	public static final MobEffect ENCHANTED = create("enchanted", new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0xf5ce8c));
	public static final MobEffect ETHEREAL = create("ethereal", new EmptyStatusEffect(MobEffectCategory.NEUTRAL, 0x9a9ebf));
	public static final MobEffect GILLS = create("gills", new GillsStatusEffect(MobEffectCategory.HARMFUL, 0x2f5e7b));
	public static final MobEffect HARDENING = create("hardening", new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0x3fa442).addAttributeModifier(Attributes.ARMOR, "7f7f1ce3-6383-49a8-a9f1-af0f5c7ab38f", 3, AttributeModifier.Operation.ADDITION));
	public static final MobEffect IGNITION = create("ignition", new IgnitionStatusEffect(MobEffectCategory.HARMFUL, 0xd68a0c));
	public static final MobEffect INHIBITED = create("inhibited", new EmptyStatusEffect(MobEffectCategory.HARMFUL, 0x383a41));
	public static final MobEffect INVIGORATING = create("invigorating", new InvigoratingStatusEffect(MobEffectCategory.BENEFICIAL, 0xaa64b3));
	public static final MobEffect LEECHING = create("leeching", new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0x4a0000));
	public static final MobEffect MAGIC_SPONGE = create("magic_sponge", new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0xa264aa));
	public static final MobEffect MORTAL_COIL = create("mortal_coil", new MortalCoilStatusEffect(MobEffectCategory.HARMFUL, 0x2d2d2d));
	public static final MobEffect NOURISHING = create("nourishing", new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0x71bc78));
	public static final MobEffect PACT = create("pact", new EmptyStatusEffect(MobEffectCategory.HARMFUL, 0x7d0000).addAttributeModifier(Attributes.MAX_HEALTH, "be0cf4d3-6e4b-43e6-9831-9e7d14aa14a7", -1, AttributeModifier.Operation.ADDITION));
	public static final MobEffect POLYMORPH = create("polymorph", new PolymorphStatusEffect(MobEffectCategory.NEUTRAL, 0xb154a9));
	public static final MobEffect PURITY = create("purity", new PurityStatusEffect(MobEffectCategory.BENEFICIAL, 0xded3d5));
	public static final MobEffect SINKING = create("sinking", new SinkingStatusEffect(MobEffectCategory.HARMFUL, 0x6c6c6c));
	public static final MobEffect SYNCHRONIZED = create("synchronized", new SynchronizedStatusEffect(MobEffectCategory.NEUTRAL, 0x990404));
	public static final MobEffect THEFT = create("theft", new TheftStatusEffect(MobEffectCategory.BENEFICIAL, 0x773b89));
	public static final MobEffect THORNS = create("thorns", new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0x527d26));
	public static final MobEffect VOLATILITY = create("volatility", new EmptyStatusEffect(MobEffectCategory.HARMFUL, 0xb76b00));
	public static final MobEffect WEBBED = create("webbed", new WebbedStatusEffect(MobEffectCategory.HARMFUL, 0x2d312f));
	public static final MobEffect WEDNESDAY = create("wednesday", new WednesdayStatusEffect(MobEffectCategory.BENEFICIAL, 0x00ff00));

	private static <T extends MobEffect> T create(String name, T effect) {
		STATUS_EFFECTS.put(effect, Bewitchment.id(name));
		return effect;
	}

	public static void init() {
		STATUS_EFFECTS.keySet().forEach(effect -> Registry.register(BuiltInRegistries.MOB_EFFECT, STATUS_EFFECTS.get(effect), effect));
	}
}
