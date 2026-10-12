/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.model.equipment.trinket;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;

public class PricklyBeltModel extends EntityModel<Entity> {
	private final ModelPart armorBody;

	public PricklyBeltModel(ModelPart root) {
		armorBody = root.getChild("armorBody");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition data = new MeshDefinition();
		PartDefinition root = data.getRoot();
		PartDefinition armorBody = root.addOrReplaceChild("armorBody", CubeListBuilder.create().addBox(-4.5F, 22.0F, -2.5F, 9.0F, 3.0F, 5.0F, new CubeDeformation(-0.2F, -0.2F, -0.2F)), PartPose.offsetAndRotation(0.0F, -20.0F, 2.5F, 0.0F, 0.0F, 0.0F));
		armorBody.addOrReplaceChild("buckle", CubeListBuilder.create().texOffs(0, 9).addBox(-1.5F, -2.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F, -0.1F, -0.1F)).texOffs(0, 12).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F, -0.1F, -0.1F)).texOffs(10, 10).addBox(0.5F, -2.0F, -0.55F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F, -0.1F, -0.1F)).addBox(-1.5F, -2.0F, -0.55F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F, -0.1F, -0.1F)), PartPose.offsetAndRotation(0.0F, 24.5F, -2.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(data, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		armorBody.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
