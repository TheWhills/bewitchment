/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.event;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.component.BloodComponent;
import moriyashiine.bewitchment.api.event.BloodSuckEvent;
import moriyashiine.bewitchment.common.block.CoffinBlock;
import moriyashiine.bewitchment.common.block.entity.BrazierBlockEntity;
import moriyashiine.bewitchment.common.block.entity.GlyphBlockEntity;
import moriyashiine.bewitchment.common.block.entity.SigilBlockEntity;
import moriyashiine.bewitchment.common.item.AthameItem;
import moriyashiine.bewitchment.common.misc.BWUtil;
import moriyashiine.bewitchment.common.packet.TransformationAbilityPacket;
import moriyashiine.bewitchment.common.recipe.AthameDropRecipe;
import moriyashiine.bewitchment.common.recipe.IncenseRecipe;
import moriyashiine.bewitchment.common.registry.*;
import moriyashiine.bewitchment.common.world.BWWorldState;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Bewitchment's gameplay hooks on {@code MinecraftForge.EVENT_BUS}. These were Fabric API
 * callbacks registered in the mod initializer; bodies are unchanged apart from reading their
 * inputs from the Forge event.
 */
public final class BWCommonEvents {
	private BWCommonEvents() {
	}

	public static void register(IEventBus forgeBus) {
		forgeBus.addListener(BWCommonEvents::onRegisterCommands);
		forgeBus.addListener(BWCommonEvents::onServerStopped);
		forgeBus.addListener(BWCommonEvents::onRespawn);
		forgeBus.addListener(EventPriority.LOW, BWCommonEvents::onClone);
		forgeBus.addListener(BWCommonEvents::onLivingDeath);
		forgeBus.addListener(BWCommonEvents::onEntityInteract);
		forgeBus.addListener(BWCommonEvents::onSleepingTimeCheck);
		forgeBus.addListener(BWCommonEvents::onSleepInBed);
		forgeBus.addListener(BWCommonEvents::onNameFormat);
		forgeBus.addListener(BWCommonEvents::onTabListNameFormat);
	}

	private static void onRegisterCommands(RegisterCommandsEvent event) {
		BWCommands.init(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
	}

	private static void onServerStopped(ServerStoppedEvent event) {
		BewitchmentAPI.fakePlayer = null;
	}

	private static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
		Player newPlayer = event.getEntity();
		BWComponents.TRANSFORMATION_COMPONENT.get(newPlayer).setAlternateForm(false);
		BWComponents.ADDITIONAL_WEREWOLF_DATA_COMPONENT.get(newPlayer).setForcedTransformation(false);
	}

	/** Leaving the End recreates the player: keep them in their alternate form. Runs after components are copied. */
	private static void onClone(PlayerEvent.Clone event) {
		if (event.isWasDeath()) {
			return;
		}
		Player oldPlayer = event.getOriginal();
		oldPlayer.reviveCaps();
		try {
			if (BWComponents.TRANSFORMATION_COMPONENT.get(oldPlayer).isAlternateForm()) {
				TransformationAbilityPacket.useAbility(event.getEntity(), true);
			}
		} finally {
			oldPlayer.invalidateCaps();
		}
	}

	/** Effects of killing something (familiar/contract heals, athame drops, boss summoning, blood bottles). */
	private static void onLivingDeath(LivingDeathEvent event) {
		LivingEntity killedEntity = event.getEntity();
		if (!(killedEntity.level() instanceof ServerLevel world) || !(event.getSource().getEntity() instanceof LivingEntity entity)) {
			return;
		}
		if (entity instanceof LivingEntity livingEntity) {
			if (livingEntity instanceof Player player && killedEntity.getMobType() == MobType.ARTHROPOD && BewitchmentAPI.getFamiliar(player) == BWEntityTypes.TOAD.get()) {
				livingEntity.heal(livingEntity.getMaxHealth() * 1 / 4f);
			}
			if (livingEntity instanceof Player player && BWComponents.CONTRACTS_COMPONENT.get(player).hasContract(BWContracts.DEATH.get())) {
				livingEntity.heal(livingEntity.getMaxHealth() / 4f);
			}
			if (livingEntity.getMainHandItem().getItem() instanceof AthameItem) {
				BlockPos glyphPos = BWUtil.getClosestBlockPos(killedEntity.blockPosition(), 6, currentPos -> world.getBlockEntity(currentPos) instanceof GlyphBlockEntity);
				if (glyphPos != null) {
					((GlyphBlockEntity) world.getBlockEntity(glyphPos)).onUse(world, glyphPos, livingEntity, InteractionHand.MAIN_HAND, killedEntity);
				}
				if (world.isNight()) {
					boolean chicken = killedEntity instanceof Chicken, wolf = killedEntity instanceof Wolf;
					if (chicken || wolf) {
						Holder<Biome> biome = world.getBiome(killedEntity.blockPosition());
						Mob boss = null;
						if (chicken) {
							if (biome.is(BiomeTags.IS_HILL) || biome.is(Tags.Biomes.IS_SNOWY) || biome.is(BiomeTags.IS_MOUNTAIN)) {
								boss = BWEntityTypes.LILITH.get().create(world);
							}
						}
						if (wolf) {
							if (biome.is(BiomeTags.IS_TAIGA) || biome.is(BiomeTags.IS_FOREST)) {
								boss = BWEntityTypes.HERNE.get().create(world);
							}
						}
						if (boss != null) {
							BlockPos brazierPos = BWUtil.getClosestBlockPos(killedEntity.blockPosition(), 8, currentPos -> world.getBlockEntity(currentPos) instanceof BrazierBlockEntity brazier && brazier.incenseRecipe.effect == BWStatusEffects.MORTAL_COIL.get());
							if (brazierPos != null) {
								world.destroyBlock(brazierPos, false);
								world.explode(null, brazierPos.getX() + 0.5, brazierPos.getY() + 0.5, brazierPos.getZ() + 0.5, 3, Level.ExplosionInteraction.TNT);
								boss.finalizeSpawn(world, world.getCurrentDifficultyAt(brazierPos), MobSpawnType.EVENT, null, null);
								boss.absMoveTo(brazierPos.getX() + 0.5, brazierPos.getY(), brazierPos.getZ() + 0.5, world.random.nextFloat() * 360, 0);
								world.addFreshEntity(boss);
							}
						}
					}
				}
				for (AthameDropRecipe recipe : world.getRecipeManager().getAllRecipesFor(BWRecipeTypes.ATHAME_DROP_RECIPE_TYPE.get())) {
					if (recipe.entity_type.equals(killedEntity.getType()) && world.random.nextFloat() < recipe.chance * (EnchantmentHelper.getMobLooting(livingEntity) + 1)) {
						ItemStack drop = recipe.getResultItem(entity.level().registryAccess()).copy();
						if (recipe.entity_type == EntityType.PLAYER) {
							drop.getOrCreateTag().putString("SkullOwner", killedEntity.getName().getString());
						}
						Containers.dropItemStack(world, killedEntity.getX() + 0.5, killedEntity.getY() + 0.5, killedEntity.getZ() + 0.5, drop);
					}
				}
				if (livingEntity instanceof Player player && livingEntity.getOffhandItem().getItem() == Items.GLASS_BOTTLE && BWComponents.BLOOD_COMPONENT.get(killedEntity).getBlood() > 20 && killedEntity.getType().is(BWTags.HAS_BLOOD)) {
					world.playSound(null, livingEntity.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1, 0.5f);
					BWUtil.addItemToInventoryAndConsume(player, InteractionHand.OFF_HAND, new ItemStack(BWObjects.BOTTLE_OF_BLOOD.get()));
				}
			}
		}
	}

	/** Vampires drink from entities by sneak-using them with an empty main hand. */
	private static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
		Player player = event.getEntity();
		Level world = event.getLevel();
		InteractionHand hand = event.getHand();
		Entity entity = event.getTarget();
		if (entity instanceof LivingEntity livingEntity && hand == InteractionHand.MAIN_HAND && player.isShiftKeyDown() && entity.isAlive() && BewitchmentAPI.isVampire(player, true) && player.getItemInHand(hand).isEmpty()) {
			int toGive = entity.getType().is(BWTags.HAS_BLOOD) ? 5 : entity instanceof Animal ? 1 : 0;
			BloodSuckEvent.Amount amountEvent = new BloodSuckEvent.Amount(player, livingEntity, toGive);
			MinecraftForge.EVENT_BUS.post(amountEvent);
			toGive = amountEvent.getBloodToGive();
			if (toGive > 0) {
				BloodComponent playerBloodComponent = BWComponents.BLOOD_COMPONENT.get(player);
				BloodComponent entityBloodComponent = BWComponents.BLOOD_COMPONENT.get(livingEntity);
				if (playerBloodComponent.fillBlood(toGive, true) && entityBloodComponent.drainBlood(10, true)) {
					if (!world.isClientSide && livingEntity.hurtTime == 0) {
						MinecraftForge.EVENT_BUS.post(new BloodSuckEvent.Post(player, livingEntity, toGive));
						world.playSound(null, player.blockPosition(), SoundEvents.HONEY_DRINK, player.getSoundSource(), 1, 0.5f);
						if (!livingEntity.isSleeping() || entityBloodComponent.getBlood() < BloodComponent.MAX_BLOOD / 2) {
							entity.hurt(BWDamageSources.create(entity.level(), BWDamageSources.VAMPIRE), 2);
						}
						playerBloodComponent.fillBlood(toGive, false);
						entityBloodComponent.drainBlood(10, false);
					}
					event.setCancellationResult(InteractionResult.sidedSuccess(world.isClientSide));
					event.setCanceled(true);
					return;
				}
			}
		}
	}

	/** Coffins can be slept in during the day. */
	private static void onSleepingTimeCheck(SleepingTimeCheckEvent event) {
		Player player = event.getEntity();
		event.getSleepingLocation().ifPresent(sleepingPos -> {
			if (player.level().getBlockState(sleepingPos).getBlock() instanceof CoffinBlock && player.level().isDay()) {
				event.setResult(Event.Result.ALLOW);
			}
	});
	}

	/** No sleeping while transformed, and coffins only by day. */
	private static void onSleepInBed(PlayerSleepInBedEvent event) {
		Player player = event.getEntity();
		if (BWComponents.TRANSFORMATION_COMPONENT.get(player).isAlternateForm()) {
			player.displayClientMessage(Component.translatable("block.minecraft.bed.transformation"), true);
			event.setResult(Player.BedSleepingProblem.OTHER_PROBLEM);
			return;
		}
		if (player.level().getBlockState(event.getPos()).getBlock() instanceof CoffinBlock) {
			if (player.level().isNight()) {
				player.displayClientMessage(Component.translatable("block.minecraft.bed.coffin"), true);
				event.setResult(Player.BedSleepingProblem.OTHER_PROBLEM);
				return;
			}
		}
	}

	/** Polymorphed players show their disguise's name over their head and in chat. */
	private static void onNameFormat(PlayerEvent.NameFormat event) {
		BWComponents.POLYMORPH_COMPONENT.maybeGet(event.getEntity()).ifPresent(polymorph -> {
			if (polymorph.getUuid() != null && polymorph.getName() != null) {
				event.setDisplayname(Component.literal(polymorph.getName()));
			}
		});
	}

	/** ...and in the tab list. */
	private static void onTabListNameFormat(PlayerEvent.TabListNameFormat event) {
		BWComponents.POLYMORPH_COMPONENT.maybeGet(event.getEntity()).ifPresent(polymorph -> {
			if (polymorph.getUuid() != null && polymorph.getName() != null) {
				event.setDisplayName(Component.literal(polymorph.getName()));
			}
		});
	}

	/** Called from {@code LivingEntityWakeMixin} when any living entity stops sleeping: apply nearby incense. */
	public static void onStopSleeping(LivingEntity entity) {
		if (!entity.level().isClientSide) {
			BWWorldState worldState = BWWorldState.get(entity.level());
			for (BlockPos foundPos : BWUtil.getBlockPoses(entity.blockPosition(), 12)) {
				if (entity.level().getWorldBorder().isWithinBounds(foundPos) && entity.level().getBlockEntity(foundPos) instanceof BrazierBlockEntity brazier && brazier.incenseRecipe != null) {
					IncenseRecipe recipe = brazier.incenseRecipe;
					int durationMultiplier = 1;
					for (BlockPos sigilPos : BWUtil.getBlockPoses(foundPos, 16)) {
						if (entity.level().getWorldBorder().isWithinBounds(foundPos) && worldState.potentialSigils.contains(sigilPos.asLong()) && entity.level().getBlockEntity(sigilPos) instanceof SigilBlockEntity sigil && sigil.getSigil() == BWSigils.EXTENDING.get()) {
							if (sigil.test(entity)) {
								sigil.setUses(sigil.getUses() - 1);
								sigil.setChanged();
								durationMultiplier = 2;
								break;
							}
						}
					}
					entity.addEffect(new MobEffectInstance(recipe.effect, 24000 * durationMultiplier, recipe.amplifier, true, false));
				}
			}
		}
	}
}
