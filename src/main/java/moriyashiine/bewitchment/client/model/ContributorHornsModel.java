/*
 * All Rights Reserved (c) MoriyaShiine
 */

// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package moriyashiine.bewitchment.client.model;

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
import net.minecraft.client.renderer.RenderType;

public class ContributorHornsModel extends Model {
	private final ModelPart armorHead;

	public ContributorHornsModel(ModelPart root) {
		super(RenderType::entityTranslucent);
		armorHead = root.getChild("armorHead");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition data = new MeshDefinition();
		PartDefinition root = data.getRoot();
		PartDefinition armorHead = root.addOrReplaceChild("armorHead", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition lUpperHorn01 = armorHead.addOrReplaceChild("lUpperHorn01", CubeListBuilder.create().texOffs(6, 5).mirror(true).addBox(-1.0F, -1.0F, -1.8F, 2.0F, 2.0F, 3.0F), PartPose.offsetAndRotation(2.0F, -8.7F, -0.5F, 0.8727F, 0.1745F, 0.0F));
		PartDefinition lUpperHorn02 = lUpperHorn01.addOrReplaceChild("lUpperHorn02", CubeListBuilder.create().texOffs(0, 12).mirror(true).addBox(-0.5F, -0.6F, -0.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.2F, 0.2F, 0.2F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.9F, -0.3142F, 0.2618F, 0.0F));
		lUpperHorn02.addOrReplaceChild("lUpperHorn03", CubeListBuilder.create().texOffs(8, 12).mirror(true).addBox(-0.5F, -0.5F, -0.4F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 2.4F, -0.1745F, 0.1745F, 0.0F));
		PartDefinition rUpperHorn01 = armorHead.addOrReplaceChild("rUpperHorn01", CubeListBuilder.create().texOffs(6, 5).mirror(true).addBox(-1.0F, -1.0F, -1.8F, 2.0F, 2.0F, 3.0F), PartPose.offsetAndRotation(-2.0F, -8.7F, -0.5F, 0.8727F, -0.1745F, 0.0F));
		PartDefinition rUpperHorn02 = rUpperHorn01.addOrReplaceChild("rUpperHorn02", CubeListBuilder.create().texOffs(0, 12).mirror(true).addBox(-0.5F, -0.6F, -0.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.2F, 0.2F, 0.2F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.9F, -0.3142F, -0.2618F, 0.0F));
		rUpperHorn02.addOrReplaceChild("rUpperHorn03", CubeListBuilder.create().texOffs(8, 12).mirror(true).addBox(-0.5F, -0.5F, -0.4F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 2.4F, -0.1745F, -0.1745F, 0.0F));
		PartDefinition lHorn01 = armorHead.addOrReplaceChild("lHorn01", CubeListBuilder.create().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(2.9F, -7.3F, 1.6F, -0.6109F, 0.0F, 1.309F));
		PartDefinition lHorn02 = lHorn01.addOrReplaceChild("lHorn02", CubeListBuilder.create().texOffs(0, 4).addBox(-0.6F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.2F, 0.2F, 0.2F)), PartPose.offsetAndRotation(-0.1F, -1.5F, -0.1F, -0.2618F, 0.0F, 0.4014F));
		PartDefinition lHorn03 = lHorn02.addOrReplaceChild("lHorn03", CubeListBuilder.create().texOffs(0, 4).addBox(-0.8F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.offsetAndRotation(0.0F, -1.6F, 0.0F, -0.1745F, 0.0F, 0.4363F));
		PartDefinition lHorn04 = lHorn03.addOrReplaceChild("lHorn04", CubeListBuilder.create().texOffs(12, 0).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -1.7F, 0.0F, 0.0524F, 0.0F, 0.1396F));
		lHorn04.addOrReplaceChild("lHorn05", CubeListBuilder.create().texOffs(12, 0).addBox(-0.5F, -2.1F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -2.7F, 0.0F, 0.0524F, 0.0F, -0.3142F));
		PartDefinition rHorn01 = armorHead.addOrReplaceChild("rHorn01", CubeListBuilder.create().mirror(true).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(-2.9F, -7.3F, 1.6F, -0.6109F, 0.0F, -1.309F));
		PartDefinition rHorn02 = rHorn01.addOrReplaceChild("rHorn02", CubeListBuilder.create().texOffs(0, 4).mirror(true).addBox(-0.4F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.2F, 0.2F, 0.2F)), PartPose.offsetAndRotation(0.1F, -1.5F, -0.1F, -0.2618F, 0.0F, -0.4014F));
		PartDefinition rHorn03 = rHorn02.addOrReplaceChild("rHorn03", CubeListBuilder.create().texOffs(0, 4).mirror(true).addBox(-0.2F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.offsetAndRotation(0.0F, -1.6F, 0.0F, -0.1745F, 0.0F, -0.4363F));
		PartDefinition rHorn04 = rHorn03.addOrReplaceChild("rHorn04", CubeListBuilder.create().texOffs(12, 0).mirror(true).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -1.7F, 0.0F, 0.0524F, 0.0F, -0.1396F));
		rHorn04.addOrReplaceChild("rHorn05", CubeListBuilder.create().texOffs(12, 0).mirror(true).addBox(-0.5F, -2.1F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -2.7F, 0.0F, 0.0524F, 0.0F, 0.3142F));
		return LayerDefinition.create(data, 16, 16);
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		armorHead.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
