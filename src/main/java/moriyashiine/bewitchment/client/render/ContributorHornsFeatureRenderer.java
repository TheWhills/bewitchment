/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.render;

import moriyashiine.bewitchment.client.BewitchmentClient;
import moriyashiine.bewitchment.client.model.ContributorHornsModel;
import moriyashiine.bewitchment.common.Bewitchment;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.apache.commons.io.IOUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

public final class ContributorHornsFeatureRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
	private static final ResourceLocation TEXTURE = Bewitchment.id("textures/entity/armor/contributor_horns.png");
	private static final Set<UUID> CONTRIBUTORS = new HashSet<>();
	private static final String CONTRIBUTORS_URL = "https://raw.githubusercontent.com/MoriyaShiine/bewitchment/master/contributors.properties";
	private static ContributorHornsModel MODEL;

	private static boolean init = false;

	public ContributorHornsFeatureRenderer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> context, EntityModelSet loader) {
		super(context);
		MODEL = new ContributorHornsModel(loader.bakeLayer(BewitchmentClient.CONTRIBUTOR_HORNS_MODEL_LAYER));
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, AbstractClientPlayer player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (!init) {
			Thread loader = new ContributorListLoaderThread();
			loader.start();
			init = true;
		} else if (!player.isInvisible() && player.isModelPartShown(PlayerModelPart.HAT) && CONTRIBUTORS.contains(player.getUUID())) {
			matrices.pushPose();
			getParentModel().head.translateAndRotate(matrices);
			MODEL.renderToBuffer(matrices, vertexConsumers.getBuffer(RenderType.entitySolid(TEXTURE)), light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
			matrices.popPose();
		}
	}

	private static class ContributorListLoaderThread extends Thread {
		public ContributorListLoaderThread() {
			setName("Bewitchment Contributor List Loader Thread");
			setDaemon(true);
		}

		@Override
		public void run() {
			try (BufferedInputStream stream = IOUtils.buffer(new URL(CONTRIBUTORS_URL).openStream())) {
				Properties properties = new Properties();
				properties.load(stream);
				synchronized (CONTRIBUTORS) {
					CONTRIBUTORS.clear();
					for (String key : properties.stringPropertyNames()) {
						CONTRIBUTORS.add(UUID.fromString(properties.getProperty(key)));
					}
				}
			} catch (IOException e) {
				System.out.println("Failed to load contributor list. Contributor horns will not be rendered.");
			}
		}
	}
}
