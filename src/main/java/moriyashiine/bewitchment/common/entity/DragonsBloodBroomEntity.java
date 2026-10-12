/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.common.entity;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.api.item.SigilItem;
import moriyashiine.bewitchment.api.registry.Sigil;
import moriyashiine.bewitchment.common.Bewitchment;
import moriyashiine.bewitchment.common.item.AthameItem;
import moriyashiine.bewitchment.common.item.TaglockItem;
import moriyashiine.bewitchment.common.registry.BWEntityTypes;
import moriyashiine.bewitchment.common.registry.BWRegistries;
import moriyashiine.bewitchment.common.registry.BWSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class DragonsBloodBroomEntity extends BroomEntity {
	private final List<UUID> entities = new ArrayList<>();

	private Sigil sigil = null;
	private int uses = 0;

	private boolean modeOnWhitelist = false;

	public DragonsBloodBroomEntity(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Override
	public void tick() {
		super.tick();
		if (!level().isClientSide && sigil != null && uses <= 0) {
			entities.clear();
			sigil = null;
			uses = 0;
			modeOnWhitelist = false;
		}
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		boolean client = level().isClientSide;
		if (player.isShiftKeyDown()) {
			if (!client && player.getUUID().equals(getOwner())) {
				ItemStack stack = player.getItemInHand(hand);
				if (stack.getItem() instanceof SigilItem sigilItem && sigilItem.sigil.active) {
					sigil = sigilItem.sigil;
					uses = sigil.uses * (BewitchmentAPI.getFamiliar(player) == BWEntityTypes.SNAKE.get() ? 2 : 1);
					modeOnWhitelist = true;
					stack.shrink(1);
				} else if (sigil != null) {
					if (stack.getItem() instanceof TaglockItem && TaglockItem.hasTaglock(stack)) {
						entities.add(TaglockItem.getTaglockUUID(stack));
						stack.shrink(1);
					} else if (stack.getItem() instanceof AthameItem && !entities.isEmpty()) {
						level().playSound(null, blockPosition(), BWSoundEvents.BLOCK_SIGIL_PLING.get(), SoundSource.NEUTRAL, 1, modeOnWhitelist ? 0.5f : 1);
						player.displayClientMessage(Component.translatable(Bewitchment.MOD_ID + ".message.toggle_" + (!modeOnWhitelist ? "whitelist" : "blacklist")), true);
						modeOnWhitelist = !modeOnWhitelist;
					}
				}
			}
			return InteractionResult.sidedSuccess(client);
		}
		if (!level().isClientSide && hand == InteractionHand.MAIN_HAND && sigil != null) {
			if (sigil != null && sigil.active && test(player)) {
				InteractionResult result = sigil.use(level(), blockPosition(), player, hand);
				if (result == InteractionResult.SUCCESS) {
					uses--;
				}
			}
		}
		return super.interact(player, hand);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		readFromNbt(nbt);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		if (sigil != null) {
			writeToNbt(nbt);
		}
	}

	@Override
	public void init(ItemStack stack) {
		if (stack.hasTag()) {
			readFromNbt(stack.getTag());
		}
	}

	@Override
	protected ItemStack getDroppedStack() {
		ItemStack stack = super.getDroppedStack();
		if (sigil != null) {
			writeToNbt(stack.getOrCreateTag());
		} else if (stack.hasTag()) {
			stack.getTag().remove("Entities");
			stack.getTag().remove("Sigil");
			stack.getTag().remove("Uses");
			stack.getTag().remove("ModeOnWhitelist");
		}
		return stack;
	}

	private void readFromNbt(CompoundTag nbt) {
		if (nbt.contains("Sigil")) {
			ListTag entitiesList = nbt.getList("Entities", Tag.TAG_COMPOUND);
			for (int i = 0; i < entitiesList.size(); i++) {
				this.entities.add(UUID.fromString(entitiesList.getString(i)));
			}
			sigil = BWRegistries.SIGIL.get(new ResourceLocation(nbt.getString("Sigil")));
			uses = nbt.getInt("Uses");
			modeOnWhitelist = nbt.getBoolean("ModeOnWhitelist");
		}
	}

	private void writeToNbt(CompoundTag nbt) {
		if (sigil != null) {
			ListTag entitiesList = new ListTag();
			for (UUID entity : this.entities) {
				entitiesList.add(StringTag.valueOf(entity.toString()));
			}
			nbt.put("Entities", entitiesList);
			nbt.putString("Sigil", BWRegistries.SIGIL.getKey(sigil).toString());
			nbt.putInt("Uses", uses);
			nbt.putBoolean("ModeOnWhitelist", modeOnWhitelist);
		}
	}

	private boolean test(Entity entity) {
		if (!entities.isEmpty()) {
			if (modeOnWhitelist) {
				return entities.contains(entity.getUUID());
			}
			return !entities.contains(entity.getUUID());
		}
		return true;
	}
}
