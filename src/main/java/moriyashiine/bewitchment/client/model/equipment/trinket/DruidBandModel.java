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

public class DruidBandModel extends EntityModel<Entity> {
	private final ModelPart druidBand;

	public DruidBandModel(ModelPart root) {
		druidBand = root.getChild("druidBand");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition data = new MeshDefinition();
		PartDefinition root = data.getRoot();
		PartDefinition druidBand = root.addOrReplaceChild("druidBand", CubeListBuilder.create().addBox(0.0F, 11.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)).texOffs(0, 6).addBox(1.0F, 11.0F, -2.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F, -0.1F, -0.1F)), PartPose.offsetAndRotation(-2.0F, -15.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition woodDetails = druidBand.addOrReplaceChild("woodDetails", CubeListBuilder.create().texOffs(7, 6).addBox(3.5F, -5.5F, -2.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F, -0.25F, -0.25F)).texOffs(2, 11).addBox(3.25F, -6.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(-0.4F, -0.4F, -0.4F)).mirror(true).addBox(-1.25F, -6.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(-0.4F, -0.4F, -0.4F)).texOffs(9, 12).addBox(-1.25F, -3.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(-0.4F, -0.4F, -0.4F)).mirror(false).addBox(3.25F, -3.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(-0.4F, -0.4F, -0.4F)).texOffs(8, 6).addBox(-0.5F, -5.5F, -2.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F, -0.25F, -0.25F)).texOffs(9, 6).addBox(-0.5F, -5.5F, 1.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F, -0.25F, -0.25F)).texOffs(8, 6).addBox(3.5F, -5.5F, 1.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F, -0.25F, -0.25F)).texOffs(7, 6).addBox(1.5F, -5.5F, 1.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F, -0.25F, -0.25F)).addBox(-0.5F, -5.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F, -0.25F, -0.25F)).addBox(3.5F, -5.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.25F, -0.25F, -0.25F)), PartPose.offsetAndRotation(0.0F, 15.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		woodDetails.addOrReplaceChild("leaf04_r1", CubeListBuilder.create().texOffs(1, 12).addBox(-0.5F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(-0.4F, -0.4F, -0.4F)), PartPose.offsetAndRotation(3.75F, -4.0F, 1.75F, 0.0F, -0.3054F, 0.0F));
		woodDetails.addOrReplaceChild("leaf02_r1", CubeListBuilder.create().texOffs(9, 12).mirror(true).addBox(-1.5F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(-0.4F, -0.4F, -0.4F)), PartPose.offsetAndRotation(0.25F, -4.0F, -2.0F, 0.0F, -0.3054F, 0.0F));
		woodDetails.addOrReplaceChild("leaf03_r1", CubeListBuilder.create().texOffs(1, 12).mirror(true).addBox(-1.5F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(-0.4F, -0.4F, -0.4F)), PartPose.offsetAndRotation(0.25F, -4.0F, 1.75F, 0.0F, 0.3054F, 0.0F));
		woodDetails.addOrReplaceChild("leaf01_r1", CubeListBuilder.create().texOffs(9, 12).addBox(-0.5F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(-0.4F, -0.4F, -0.4F)), PartPose.offsetAndRotation(3.75F, -4.0F, -2.0F, 0.0F, 0.3054F, 0.0F));
		return LayerDefinition.create(data, 16, 16);
	}

	@Override
	public void setupAnim(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		druidBand.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
