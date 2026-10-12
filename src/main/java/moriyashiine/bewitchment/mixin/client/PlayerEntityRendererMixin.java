/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.mixin.client;

import moriyashiine.bewitchment.api.BewitchmentAPI;
import moriyashiine.bewitchment.api.entity.BroomEntity;
import moriyashiine.bewitchment.client.render.ContributorHornsFeatureRenderer;
import moriyashiine.bewitchment.common.block.CoffinBlock;
import moriyashiine.bewitchment.common.registry.BWComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Optional;

@Mixin(PlayerRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
	public PlayerEntityRendererMixin(EntityRendererProvider.Context ctx, PlayerModel<AbstractClientPlayer> model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void PlayerEntityRenderer(EntityRendererProvider.Context ctx, boolean slim, CallbackInfo callbackInfo) {
		addLayer(new ContributorHornsFeatureRenderer(this, ctx.getModelSet()));
	}

	@Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"), cancellable = true)
	private void render(AbstractClientPlayer player, float yaw, float tickDelta, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light, CallbackInfo callbackInfo) {
		Optional<BlockPos> pos = player.getSleepingPos();
		if (pos.isPresent() && player.level().getBlockState(pos.get()).getBlock() instanceof CoffinBlock) {
			callbackInfo.cancel();
			return;
		}
		if (BWComponents.FULL_INVISIBILITY_COMPONENT.get(player).isFullInvisible()) {
			callbackInfo.cancel();
			return;
		}
		if (player.getVehicle() instanceof BroomEntity) {
			matrixStack.translate(0, Mth.sin((player.getVehicle().tickCount + player.getVehicle().getId()) / 4f) / 16f, 0);
		}
		LivingEntity entity = BewitchmentAPI.getTransformedPlayerEntity(player);
		if (entity != null) {
			entity.tickCount = player.tickCount;
			entity.hurtTime = player.hurtTime;
			entity.hurtDuration = Integer.MAX_VALUE;
			entity.walkAnimation = player.walkAnimation;
			entity.yHeadRot = player.yHeadRot;
			entity.yHeadRotO = player.yHeadRotO;
			entity.yBodyRot = player.yBodyRot;
			entity.yBodyRotO = player.yBodyRotO;
			entity.swinging = player.swinging;
			entity.swingTime = player.swingTime;
			entity.attackAnim = player.attackAnim;
			entity.oAttackAnim = player.oAttackAnim;
			entity.setXRot(player.getXRot());
			entity.xRotO = player.xRotO;
			entity.swingingArm = player.swingingArm;
			entity.setItemInHand(InteractionHand.MAIN_HAND, player.getMainHandItem());
			entity.setItemInHand(InteractionHand.OFF_HAND, player.getOffhandItem());
			entity.startUsingItem(player.getUsedItemHand() == null ? InteractionHand.MAIN_HAND : player.getUsedItemHand());
			entity.setShiftKeyDown(player.isShiftKeyDown());
			entity.setPose(player.getPose());
			if (player.isPassenger()) {
				entity.startRiding(player.getVehicle(), true);
			}
			float width = 1 / (entity.getType().getWidth() / EntityType.PLAYER.getWidth());
			matrixStack.scale(width, 1 / (entity.getType().getHeight() / EntityType.PLAYER.getHeight()), width);
			Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity).render(entity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
			callbackInfo.cancel();
		}
	}
}
