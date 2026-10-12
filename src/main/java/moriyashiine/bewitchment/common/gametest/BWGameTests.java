/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.gametest;

import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.component.base.ComponentContainer;
import moriyashiine.bewitchment.common.registry.BWComponents;
import moriyashiine.bewitchment.common.registry.BWObjects;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Server-side smoke tests for the Forge port, run in CI with {@code ./gradlew runGameTestServer}.
 * Only active when the "bewitchment" GameTest namespace is enabled, so they never run in normal play.
 */
@GameTestHolder(Bewitchment.MOD_ID)
@PrefixGameTestTemplate(false)
public class BWGameTests {
	/** Annotated tests get the mod id prefixed by Forge (@GameTestHolder); generated tests need the full id. */
	private static final String PLATFORM_NAME = "gametest_platform";
	private static final String PLATFORM = Bewitchment.MOD_ID + ":" + PLATFORM_NAME;
	private static final BlockPos SPAWN = new BlockPos(2, 1, 2);

	/** One test per Bewitchment entity type: it must spawn, carry its components, and survive 20 ticks. */
	@GameTestGenerator
	public static Collection<TestFunction> entitiesSpawn() {
		List<TestFunction> tests = new ArrayList<>();
		for (EntityType<?> type : ForgeRegistries.ENTITY_TYPES.getValues()) {
			ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(type);
			if (id == null || !id.getNamespace().equals(Bewitchment.MOD_ID)) {
				continue;
			}
			tests.add(new TestFunction("entities", "spawn_" + id.getPath(), PLATFORM, 100, 0, true, helper -> {
				helper.getLevel().getServer().setDifficulty(Difficulty.NORMAL, true);
				Entity entity = helper.spawn(type, SPAWN);
				if (ComponentContainer.of(entity) == null) {
					helper.fail("no Bewitchment components attached", entity);
					return;
				}
				helper.runAfterDelay(20, () -> {
					if (entity.isRemoved()) {
						helper.fail("removed after spawning: " + entity.getRemovalReason(), SPAWN);
					} else {
						helper.succeed();
					}
				});
			}));
		}
		return tests;
	}

	/** Components attach by entity class, most specific registration first. */
	@GameTest(template = PLATFORM_NAME)
	public static void componentsAttachByClass(GameTestHelper helper) {
		Villager villager = helper.spawn(EntityType.VILLAGER, SPAWN);
		Pig pig = helper.spawn(EntityType.PIG, SPAWN);
		check(helper, BWComponents.WEREWOLF_VILLAGER_COMPONENT.isProvidedBy(villager), "villager should have werewolf_villager");
		check(helper, BWComponents.BLOOD_COMPONENT.isProvidedBy(villager), "villager should have blood");
		check(helper, BWComponents.MINION_COMPONENT.isProvidedBy(pig), "pig (a Mob) should have minion");
		check(helper, !BWComponents.WEREWOLF_VILLAGER_COMPONENT.isProvidedBy(pig), "pig must not have werewolf_villager");
		check(helper, !BWComponents.CONTRACTS_COMPONENT.isProvidedBy(pig), "pig must not have player-only contracts");
		helper.succeed();
	}

	/** Content registries and Bewitchment's own registries are populated. */
	@GameTest(template = PLATFORM_NAME)
	public static void registriesPopulated(GameTestHelper helper) {
		check(helper, BWObjects.BLOCKS.getEntries().size() > 200, "expected 200+ blocks, got " + BWObjects.BLOCKS.getEntries().size());
		check(helper, BWObjects.ITEMS.getEntries().size() > 200, "expected 200+ items, got " + BWObjects.ITEMS.getEntries().size());
		check(helper, BWRegistries.CONTRACT.size() == 11, "expected 11 contracts, got " + BWRegistries.CONTRACT.size());
		check(helper, BWRegistries.TRANSFORMATION.size() == 3, "expected 3 transformations, got " + BWRegistries.TRANSFORMATION.size());
		check(helper, BWRegistries.CURSE.size() > 0 && BWRegistries.SIGIL.size() > 0 && BWRegistries.FORTUNE.size() > 0 && BWRegistries.RITUAL_FUNCTION.size() > 0, "a custom registry is empty");
		helper.succeed();
	}

	private static void check(GameTestHelper helper, boolean condition, String message) {
		if (!condition) {
			helper.fail(message);
		}
	}
}
