/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.registry;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.statuseffect.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;
import net.minecraftforge.registries.ForgeRegistries;

public class BWStatusEffects {
	public static final DeferredRegister<MobEffect> STATUS_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Bewitchment.MOD_ID);

	public static final RegistryObject<MobEffect> CLIMBING = create("climbing", () -> new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0x605448));
	public static final RegistryObject<MobEffect> CORROSION = create("corrosion", () -> new EmptyStatusEffect(MobEffectCategory.HARMFUL, 0x6fc536).addAttributeModifier(Attributes.ARMOR, "5b5df91d-001a-464f-a773-ab4ccd62e09f", -3, AttributeModifier.Operation.ADDITION));
	public static final RegistryObject<MobEffect> CORRUPTION = create("corruption", () -> new CorruptionStatusEffect(MobEffectCategory.HARMFUL, 0x600000));
	public static final RegistryObject<MobEffect> DEAFENED = create("deafened", () -> new EmptyStatusEffect(MobEffectCategory.HARMFUL, 0x676c77));
	public static final RegistryObject<MobEffect> DEFLECTION = create("deflection", () -> new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0x969696));
	public static final RegistryObject<MobEffect> DISJUNCTION = create("disjunction", () -> new DisjunctionStatusEffect(MobEffectCategory.NEUTRAL, 0xb154a9));
	public static final RegistryObject<MobEffect> ENCHANTED = create("enchanted", () -> new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0xf5ce8c));
	public static final RegistryObject<MobEffect> ETHEREAL = create("ethereal", () -> new EmptyStatusEffect(MobEffectCategory.NEUTRAL, 0x9a9ebf));
	public static final RegistryObject<MobEffect> GILLS = create("gills", () -> new GillsStatusEffect(MobEffectCategory.HARMFUL, 0x2f5e7b));
	public static final RegistryObject<MobEffect> HARDENING = create("hardening", () -> new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0x3fa442).addAttributeModifier(Attributes.ARMOR, "7f7f1ce3-6383-49a8-a9f1-af0f5c7ab38f", 3, AttributeModifier.Operation.ADDITION));
	public static final RegistryObject<MobEffect> IGNITION = create("ignition", () -> new IgnitionStatusEffect(MobEffectCategory.HARMFUL, 0xd68a0c));
	public static final RegistryObject<MobEffect> INHIBITED = create("inhibited", () -> new EmptyStatusEffect(MobEffectCategory.HARMFUL, 0x383a41));
	public static final RegistryObject<MobEffect> INVIGORATING = create("invigorating", () -> new InvigoratingStatusEffect(MobEffectCategory.BENEFICIAL, 0xaa64b3));
	public static final RegistryObject<MobEffect> LEECHING = create("leeching", () -> new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0x4a0000));
	public static final RegistryObject<MobEffect> MAGIC_SPONGE = create("magic_sponge", () -> new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0xa264aa));
	public static final RegistryObject<MobEffect> MORTAL_COIL = create("mortal_coil", () -> new MortalCoilStatusEffect(MobEffectCategory.HARMFUL, 0x2d2d2d));
	public static final RegistryObject<MobEffect> NOURISHING = create("nourishing", () -> new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0x71bc78));
	public static final RegistryObject<MobEffect> PACT = create("pact", () -> new EmptyStatusEffect(MobEffectCategory.HARMFUL, 0x7d0000).addAttributeModifier(Attributes.MAX_HEALTH, "be0cf4d3-6e4b-43e6-9831-9e7d14aa14a7", -1, AttributeModifier.Operation.ADDITION));
	public static final RegistryObject<MobEffect> POLYMORPH = create("polymorph", () -> new PolymorphStatusEffect(MobEffectCategory.NEUTRAL, 0xb154a9));
	public static final RegistryObject<MobEffect> PURITY = create("purity", () -> new PurityStatusEffect(MobEffectCategory.BENEFICIAL, 0xded3d5));
	public static final RegistryObject<MobEffect> SINKING = create("sinking", () -> new SinkingStatusEffect(MobEffectCategory.HARMFUL, 0x6c6c6c));
	public static final RegistryObject<MobEffect> SYNCHRONIZED = create("synchronized", () -> new SynchronizedStatusEffect(MobEffectCategory.NEUTRAL, 0x990404));
	public static final RegistryObject<MobEffect> THEFT = create("theft", () -> new TheftStatusEffect(MobEffectCategory.BENEFICIAL, 0x773b89));
	public static final RegistryObject<MobEffect> THORNS = create("thorns", () -> new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0x527d26));
	public static final RegistryObject<MobEffect> VOLATILITY = create("volatility", () -> new EmptyStatusEffect(MobEffectCategory.HARMFUL, 0xb76b00));
	public static final RegistryObject<MobEffect> WEBBED = create("webbed", () -> new WebbedStatusEffect(MobEffectCategory.HARMFUL, 0x2d312f));
	public static final RegistryObject<MobEffect> WEDNESDAY = create("wednesday", () -> new WednesdayStatusEffect(MobEffectCategory.BENEFICIAL, 0x00ff00));

	private static <T extends MobEffect> RegistryObject<T> create(String name, Supplier<T> effect) {
		return STATUS_EFFECTS.register(name, effect);
	}
}
