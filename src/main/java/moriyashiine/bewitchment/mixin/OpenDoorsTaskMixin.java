/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin;

import com.mojang.datafixers.kinds.OptionalBox;
import moriyashiine.bewitchment.common.block.util.interfaces.SpecialDoor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;
import java.util.Set;

@Mixin(InteractWithDoor.class)
public abstract class OpenDoorsTaskMixin {
	@Shadow
	private static Optional<Set<GlobalPos>> storePos(MemoryAccessor<OptionalBox.Mu, Set<GlobalPos>> queryResult, Optional<Set<GlobalPos>> doors, ServerLevel world, BlockPos pos) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @author MoriyaShiine
	 * @reason help
	 */
	@Overwrite
	public static BehaviorControl<LivingEntity> create() {
		MutableObject<Node> mutableNode = new MutableObject<>(null);
		MutableInt mutableInt = new MutableInt(0);
		return BehaviorBuilder.create(context -> context.group(context.present(MemoryModuleType.PATH), context.registered(MemoryModuleType.DOORS_TO_CLOSE), context.registered(MemoryModuleType.NEAREST_LIVING_ENTITIES)).apply(context, (path, doorsToClose, mobs) -> (world, entity, time) -> {
			Optional<Set<GlobalPos>> optional = context.tryGet(doorsToClose);
			Path newPath = context.get(path);
			if (newPath.notStarted() || newPath.isDone()) {
				return false;
			}
			if (mutableNode.getValue() == newPath.getNextNode()) {
				mutableInt.setValue(20);
			} else if (mutableInt.decrementAndGet() > 0) {
				return false;
			}
			Node lastNode = newPath.getPreviousNode();
			Node currentNode = newPath.getNextNode();
			mutableNode.setValue(currentNode);
			BlockPos blockPos = lastNode.asBlockPos();
			BlockState blockState = world.getBlockState(blockPos);
			boolean failed = failed(world, entity, blockState, blockPos);
			if (blockState.is(BlockTags.WOODEN_DOORS, foundState -> foundState.getBlock() instanceof DoorBlock)) {
				DoorBlock doorBlock = (DoorBlock) blockState.getBlock();
				if (!doorBlock.isOpen(blockState) && !failed) {
					doorBlock.setOpen(entity, world, blockState, blockPos, true);
				}
				optional = storePos(doorsToClose, optional, world, blockPos);
			}
			blockPos = currentNode.asBlockPos();
			blockState = world.getBlockState(blockPos);
			if (blockState.is(BlockTags.WOODEN_DOORS, foundState -> foundState.getBlock() instanceof DoorBlock) && blockState.getBlock() instanceof DoorBlock doorBlock && !doorBlock.isOpen(blockState)) {
				if (!failed) {
					doorBlock.setOpen(entity, world, blockState, blockPos, true);
					optional = storePos(doorsToClose, optional, world, blockPos);
				}
			}
			optional.ifPresent(doors -> InteractWithDoor.closeDoorsThatIHaveOpenedOrPassedThrough(world, entity, lastNode, currentNode, doors, context.tryGet(mobs)));
			return true;
		}));
	}

	@Unique
	private static boolean failed(Level world, LivingEntity entity, BlockState blockState, BlockPos blockPos) {
		return blockState.getBlock() instanceof SpecialDoor specialDoor && specialDoor.onSpecialUse(blockState, world, blockPos, entity, InteractionHand.MAIN_HAND) == InteractionResult.FAIL;
	}
}
