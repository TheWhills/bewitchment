/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import moriyashiine.bewitchment.client.misc.SpriteIdentifiers;
import moriyashiine.bewitchment.common.block.entity.WitchCauldronBlockEntity;
import moriyashiine.bewitchment.common.registry.BWParticleTypes;
import moriyashiine.bewitchment.common.registry.BWProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class WitchCauldronBlockEntityRenderer implements BlockEntityRenderer<WitchCauldronBlockEntity> {
	private static final float[] HEIGHT = {0, 0.25f, 0.4375f, 0.625f};

	@Override
	public void render(WitchCauldronBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		Level world = entity.getLevel();
		if (world != null) {
			BlockPos pos = entity.getBlockPos();
			renderName(entity.name, pos, matrices, vertexConsumers, light);
			int level = entity.getBlockState().getValue(BWProperties.LEVEL);
			if (level > 0) {
				matrices.pushPose();
				matrices.translate(0, HEIGHT[level], 0);
				if (entity.mode == WitchCauldronBlockEntity.Mode.TELEPORTATION) {
					renderPortal(matrices, vertexConsumers);
				}
				renderWater(entity.color, matrices, vertexConsumers, light, overlay, SpriteIdentifiers.CAULDRON_WATER.sprite());
				matrices.popPose();
				if (entity.heatTimer >= 60 && !Minecraft.getInstance().isPaused()) {
					float fluidHeight = 0;
					float width = 0.35f;
					switch (entity.getBlockState().getValue(BWProperties.LEVEL)) {
						case 1 -> fluidHeight = 0.225f;
						case 2 -> {
							fluidHeight = 0.425f;
							width = 0.3f;
						}
						case 3 -> fluidHeight = 0.625f;
					}
					if (fluidHeight > 0) {
						if (entity.mode == WitchCauldronBlockEntity.Mode.TELEPORTATION) {
							world.addParticle(new DustParticleOptions(new Vector3f(((entity.color >> 16) & 0xff) / 255f, ((entity.color >> 8) & 0xff) / 255f, (entity.color & 0xff) / 255f), 1), pos.getX() + 0.5 + Mth.nextDouble(world.random, -width, width), pos.getY() + fluidHeight, pos.getZ() + 0.5 + Mth.nextDouble(world.random, -width, width), 0, 0, 0);
						}
						if (entity.mode == WitchCauldronBlockEntity.Mode.OIL_CRAFTING && entity.color != 0xfc00fc) {
							world.addParticle(ParticleTypes.ENCHANTED_HIT, pos.getX() + 0.5 + Mth.nextDouble(world.random, -width, width), pos.getY() + fluidHeight, pos.getZ() + 0.5 + Mth.nextDouble(world.random, -width, width), 0, 0, 0);
						}
						if (entity.mode == WitchCauldronBlockEntity.Mode.BREWING) {
							world.addParticle(ParticleTypes.ENTITY_EFFECT, pos.getX() + 0.5 + Mth.nextDouble(world.random, -width, width), pos.getY() + fluidHeight, pos.getZ() + 0.5 + Mth.nextDouble(world.random, -width, width), ((entity.color >> 16) & 0xff) / 255f, ((entity.color >> 8) & 0xff) / 255f, (entity.color & 0xff) / 255f);
						}
						world.addParticle((ParticleOptions) BWParticleTypes.CAULDRON_BUBBLE.get(), pos.getX() + 0.5 + Mth.nextDouble(world.random, -width, width), pos.getY() + fluidHeight, pos.getZ() + 0.5 + Mth.nextDouble(world.random, -width, width), ((entity.color >> 16) & 0xff) / 255f, ((entity.color >> 8) & 0xff) / 255f, (entity.color & 0xff) / 255f);
					}
				}
			}
		}
	}

	private void renderName(String name, BlockPos pos, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
		if (name != null && pos.distToCenterSqr(Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition()) <= 256) {
			int x = -Minecraft.getInstance().font.width(name) / 2;
			matrices.pushPose();
			Matrix4f mat = matrices.last().pose();
			matrices.translate(0.5, 1, 0.5);
			matrices.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().camera.rotation());
			matrices.scale(-0.025f, -0.025f, 0.025f);
			Minecraft.getInstance().font.drawInBatch(name, x, 0, 0x20FFFFFF, false, mat, vertexConsumers, Font.DisplayMode.SEE_THROUGH, (int) (Minecraft.getInstance().options.getBackgroundOpacity(0.25f) * 255f) << 24, light);
			Minecraft.getInstance().font.drawInBatch(name, x, 0, -1, false, mat, vertexConsumers, Font.DisplayMode.NORMAL, 0, light);
			matrices.popPose();
		}
	}

	private void renderPortal(PoseStack matrices, MultiBufferSource vertexConsumers) {
		matrices.pushPose();
		Matrix4f matrix4f = matrices.last().pose();
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderType.endPortal());
		vertexConsumer.vertex(matrix4f, 0.125f, 0, 0.875f).endVertex();
		vertexConsumer.vertex(matrix4f, 0.875f, 0, 0.875f).endVertex();
		vertexConsumer.vertex(matrix4f, 0.875f, 0, 0.125f).endVertex();
		vertexConsumer.vertex(matrix4f, 0.125f, 0, 0.125f).endVertex();
		matrices.popPose();
	}

	private void renderWater(int color, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, TextureAtlasSprite sprite) {
		float sizeFactor = 0.125F;
		float maxV = (sprite.getV1() - sprite.getV0()) * sizeFactor;
		float minV = (sprite.getV1() - sprite.getV0()) * (1 - sizeFactor);
		int red = (color >> 16) & 0xFF;
		int green = (color >> 8) & 0xFF;
		int blue = color & 0xFF;
		matrices.pushPose();
		Matrix4f mat = matrices.last().pose();
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderType.translucent());
		vertexConsumer.vertex(mat, sizeFactor, 0, 1 - sizeFactor).color(red, green, blue, 255).uv(sprite.getU0(), sprite.getV0() + maxV).uv2(light).overlayCoords(overlay).normal(1, 1, 1).endVertex();
		vertexConsumer.vertex(mat, 1 - sizeFactor, 0, 1 - sizeFactor).color(red, green, blue, 255).uv(sprite.getU1(), sprite.getV0() + maxV).uv2(light).overlayCoords(overlay).normal(1, 1, 1).endVertex();
		vertexConsumer.vertex(mat, 1 - sizeFactor, 0, sizeFactor).color(red, green, blue, 255).uv(sprite.getU1(), sprite.getV0() + minV).uv2(light).overlayCoords(overlay).normal(1, 1, 1).endVertex();
		vertexConsumer.vertex(mat, sizeFactor, 0, sizeFactor).color(red, green, blue, 255).uv(sprite.getU0(), sprite.getV0() + minV).uv2(light).overlayCoords(overlay).normal(1, 1, 1).endVertex();
		matrices.popPose();
	}
}
