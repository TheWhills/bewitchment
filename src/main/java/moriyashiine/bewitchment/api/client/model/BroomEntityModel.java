/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.api.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import moriyashiine.bewitchment.api.entity.BroomEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class BroomEntityModel extends EntityModel<BroomEntity> {
	private final ModelPart broom;

	public BroomEntityModel(ModelPart root) {
		broom = root.getChild("broom");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition data = new MeshDefinition();
		PartDefinition root = data.getRoot();
		root.addOrReplaceChild("broom", CubeListBuilder.create().addBox(-0.5F, -1.0F, -8.0F, 1.0F, 1.0F, 16.0F).texOffs(0, 7).addBox(-1.5F, -2.0F, 8.0F, 3.0F, 3.0F, 1.0F).texOffs(0, 17).addBox(-2.5F, -3.0F, 9.0F, 5.0F, 5.0F, 6.0F).texOffs(0, 0).addBox(-1.5F, -2.0F, 13.0F, 3.0F, 3.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(data, 64, 64);
	}

	@Override
	public void setupAnim(BroomEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		broom.y = 24 + Mth.sin(animationProgress / 4);
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		broom.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
