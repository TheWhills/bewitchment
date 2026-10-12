/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.model.entity.living;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import moriyashiine.bewitchment.common.entity.living.HellhoundEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class HellhoundEntityModel<T extends HellhoundEntity> extends EntityModel<T> {
	private final ModelPart body;
	private final ModelPart lForeleg;
	private final ModelPart tail00;
	private final ModelPart lHindleg;
	private final ModelPart rHindleg;
	private final ModelPart rForeleg;
	private final ModelPart neck;

	public HellhoundEntityModel(ModelPart root) {
		body = root.getChild("body");
		lForeleg = root.getChild("lForeleg");
		tail00 = body.getChild("tail00");
		lHindleg = root.getChild("lHindleg");
		rHindleg = root.getChild("rHindleg");
		rForeleg = root.getChild("rForeleg");
		neck = root.getChild("neck");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition data = new MeshDefinition();
		PartDefinition root = data.getRoot();
		root.addOrReplaceChild("lHindleg", CubeListBuilder.create().texOffs(0, 18).mirror(true).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F), PartPose.offsetAndRotation(1.5F, 16.0F, 6.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(18, 14).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 14.0F, 1.0F, 1.5708F, 0.0F, 0.0F));
		PartDefinition mane00 = body.addOrReplaceChild("mane00", CubeListBuilder.create().texOffs(21, 0).addBox(-4.0F, -3.5F, -3.01F, 8.0F, 7.0F, 7.0F), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		mane00.addOrReplaceChild("mane02", CubeListBuilder.create().texOffs(28, 48).addBox(-3.5F, -1.0F, 0.0F, 7.0F, 2.0F, 7.0F), PartPose.offsetAndRotation(0.0F, -1.0F, 2.7F, -1.2915F, 0.0F, 0.0F));
		PartDefinition tail00 = body.addOrReplaceChild("tail00", CubeListBuilder.create().texOffs(20, 33).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 6.8F, 2.2F, 0.0F, 0.0F, 0.0F));
		PartDefinition tail01 = tail00.addOrReplaceChild("tail01", CubeListBuilder.create().texOffs(26, 33).addBox(-0.5F, -0.1F, -0.5F, 1.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 3.3F, 0.0F, 0.2731F, 0.0F, 0.0F));
		PartDefinition tail02 = tail01.addOrReplaceChild("tail02", CubeListBuilder.create().texOffs(32, 33).addBox(-0.5F, -0.1F, -0.5F, 1.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 4.4F, 0.0F, 0.2094F, 0.0F, 0.0F));
		PartDefinition tail03 = tail02.addOrReplaceChild("tail03", CubeListBuilder.create().texOffs(37, 33).addBox(-1.0F, -0.1F, -0.5F, 2.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 4.3F, 0.0F, 0.2094F, 0.0F, 0.0F));
		tail03.addOrReplaceChild("tail04", CubeListBuilder.create().texOffs(44, 33).addBox(-1.5F, -0.5F, -0.59F, 2.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 1.3F, 0.1F, 0.0F, 0.0F, -0.7854F));
		body.addOrReplaceChild("mane01", CubeListBuilder.create().texOffs(0, 48).addBox(-3.0F, -1.0F, 0.0F, 6.0F, 2.0F, 7.0F), PartPose.offsetAndRotation(0.0F, -8.2F, 2.5F, -1.1781F, 0.0F, 0.0F));
		root.addOrReplaceChild("lForeleg", CubeListBuilder.create().texOffs(0, 18).mirror(true).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F), PartPose.offsetAndRotation(1.5F, 16.0F, -4.0F, 0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("rHindleg", CubeListBuilder.create().texOffs(0, 18).mirror(true).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F), PartPose.offsetAndRotation(-1.5F, 16.0F, 6.0F, 0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("rForeleg", CubeListBuilder.create().texOffs(0, 18).mirror(true).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F), PartPose.offsetAndRotation(-1.5F, 16.0F, -4.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition neck = root.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 32).addBox(-2.5F, -2.5F, -4.0F, 5.0F, 5.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 13.6F, -4.5F, 0.0F, 0.0F, 0.0F));
		PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().addBox(-3.0F, -3.0F, -4.0F, 6.0F, 6.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -2.9F, 0.0F, 0.0F, 0.0F));
		head.addOrReplaceChild("leftEar", CubeListBuilder.create().texOffs(16, 14).addBox(-1.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(-2.0F, -3.0F, -2.0F, 0.0F, 0.0F, 0.0F));
		head.addOrReplaceChild("rightEar", CubeListBuilder.create().texOffs(16, 14).addBox(-1.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(2.0F, -3.0F, -2.0F, 0.0F, 0.0F, 0.0F));
		head.addOrReplaceChild("muzzle", CubeListBuilder.create().texOffs(0, 10).addBox(-1.5F, -1.0F, -3.0F, 3.0F, 2.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.7F, -3.9F, 0.0F, 0.0F, 0.0F));
		head.addOrReplaceChild("lowerJaw", CubeListBuilder.create().texOffs(0, 43).addBox(-1.5F, -0.3F, -3.0F, 3.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 2.0F, -3.8F, 0.0F, 0.0F, 0.0F));
		PartDefinition rHorn01 = head.addOrReplaceChild("rHorn01", CubeListBuilder.create().texOffs(46, 0).mirror(true).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(-1.3F, -2.1F, -1.4F, -1.0016F, -0.4554F, 0.0F));
		PartDefinition rHorn02 = rHorn01.addOrReplaceChild("rHorn02", CubeListBuilder.create().texOffs(55, 0).mirror(true).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -2.5F, -0.1F, -0.5236F, -0.1745F, 0.0F));
		PartDefinition rHorn03 = rHorn02.addOrReplaceChild("rHorn03", CubeListBuilder.create().texOffs(52, 7).mirror(true).addBox(-0.8F, -2.0F, -0.8F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.2F, 0.2F, 0.2F)), PartPose.offsetAndRotation(0.0F, -1.5F, -0.1F, -0.6981F, -0.2094F, 0.0F));
		PartDefinition rHorn04 = rHorn03.addOrReplaceChild("rHorn04", CubeListBuilder.create().texOffs(57, 6).mirror(true).addBox(-0.7F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.15F, 0.15F, 0.15F)), PartPose.offsetAndRotation(0.0F, -1.5F, -0.1F, -0.6981F, 0.0F, 0.0F));
		PartDefinition rHorn05 = rHorn04.addOrReplaceChild("rHorn05", CubeListBuilder.create().texOffs(52, 12).mirror(true).addBox(-0.5F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.offsetAndRotation(0.0F, -2.5F, -0.1F, -0.6981F, -0.3491F, 0.0F));
		rHorn05.addOrReplaceChild("rHorn06", CubeListBuilder.create().texOffs(58, 13).mirror(true).addBox(-0.51F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -2.7F, 0.0F, -0.6283F, 0.0F, 0.0F));
		PartDefinition lHorn01 = head.addOrReplaceChild("lHorn01", CubeListBuilder.create().texOffs(46, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(1.3F, -2.1F, -1.4F, -1.0016F, 0.4554F, 0.0F));
		PartDefinition lHorn02 = lHorn01.addOrReplaceChild("lHorn02", CubeListBuilder.create().texOffs(55, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.0F, -2.5F, -0.1F, -0.5236F, 0.1745F, 0.0F));
		PartDefinition lHorn03 = lHorn02.addOrReplaceChild("lHorn03", CubeListBuilder.create().texOffs(52, 7).addBox(-0.2F, -2.0F, -0.8F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.2F, 0.2F, 0.2F)), PartPose.offsetAndRotation(0.0F, -1.5F, -0.1F, -0.6981F, 0.2094F, 0.0F));
		PartDefinition lHorn04 = lHorn03.addOrReplaceChild("lHorn04", CubeListBuilder.create().texOffs(57, 6).addBox(-0.3F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.15F, 0.15F, 0.15F)), PartPose.offsetAndRotation(0.0F, -1.5F, -0.1F, -0.6981F, 0.0F, 0.0F));
		PartDefinition lHorn05 = lHorn04.addOrReplaceChild("lHorn05", CubeListBuilder.create().texOffs(52, 12).addBox(-0.5F, -3.0F, -0.7F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.offsetAndRotation(0.0F, -2.5F, -0.1F, -0.6981F, 0.3491F, 0.0F));
		lHorn05.addOrReplaceChild("lHorn06", CubeListBuilder.create().texOffs(58, 13).addBox(-0.49F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -2.7F, 0.0F, -0.6283F, 0.0F, 0.0F));
		return LayerDefinition.create(data, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		neck.xRot = (float) (headPitch * (Math.PI / 180f));
		neck.yRot = (float) (headYaw * (Math.PI / 180f)) * 2 / 3f;
		lForeleg.xRot = Mth.cos(limbAngle * 2 / 3f) * 1.4f * limbDistance;
		rForeleg.xRot = -lForeleg.xRot;
		rHindleg.xRot = Mth.cos(limbAngle * 2 / 3f) * 1.4f * limbDistance;
		lHindleg.xRot = -rHindleg.xRot;
		tail00.zRot = (Mth.cos(limbAngle * 2 / 3f) * limbDistance) + Mth.sin((animationProgress + entity.getId()) * 1 / 8f) * 0.25f;
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		neck.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		body.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		lForeleg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		rForeleg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		lHindleg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		rHindleg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
