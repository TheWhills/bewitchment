/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.model.entity.living;

import com.google.common.collect.ImmutableList;
import moriyashiine.bewitchment.common.entity.living.RavenEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class RavenEntityModel<T extends RavenEntity> extends AgeableListModel<T> {
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart lLeg01;
	private final ModelPart rLeg01;
	private final ModelPart lWing01;
	private final ModelPart lWing02;
	private final ModelPart lWing03;
	private final ModelPart lWing04;
	private final ModelPart rWing01;
	private final ModelPart rWing02;
	private final ModelPart rWing03;
	private final ModelPart rWing04;

	public RavenEntityModel(ModelPart root) {
		body = root.getChild("body");
		head = body.getChild("neck").getChild("head");
		lLeg01 = body.getChild("lLeg01");
		rLeg01 = body.getChild("rLeg01");
		lWing01 = body.getChild("lWing01");
		lWing02 = lWing01.getChild("lWing02");
		lWing03 = lWing02.getChild("lWing03");
		lWing04 = lWing03.getChild("lWing04");
		rWing01 = body.getChild("rWing01");
		rWing02 = rWing01.getChild("rWing02");
		rWing03 = rWing02.getChild("rWing03");
		rWing04 = rWing03.getChild("rWing04");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition data = new MeshDefinition();
		PartDefinition root = data.getRoot();
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().addBox(-2.0F, -1.5F, -2.5F, 4.0F, 3.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 18.2F, -1.6F, -0.3142F, 0.0F, 0.0F));
		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(16, 0).addBox(-1.5F, -1.3F, -3.0F, 3.0F, 2.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -0.9F, -0.5236F, 0.0F, 0.0F));
		neck.addOrReplaceChild("neckFeathers01", CubeListBuilder.create().texOffs(13, 27).addBox(-1.49F, -0.5F, -0.2F, 3.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 1.1F, -1.7F, -0.4014F, 0.0F, 0.0F));
		neck.addOrReplaceChild("neckFeathers02", CubeListBuilder.create().texOffs(0, 27).addBox(-1.5F, -0.5F, -0.2F, 3.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.7F, -2.3F, -0.8727F, 0.0F, 0.0F));
		PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(40, 0).addBox(-1.49F, -1.5F, -2.7F, 3.0F, 3.0F, 3.0F), PartPose.offsetAndRotation(0.0F, -0.4F, -2.5F, 0.8727F, 0.0F, 0.0F));
		head.addOrReplaceChild("beakBottom", CubeListBuilder.create().texOffs(54, 0).addBox(-0.49F, -0.5F, -2.8F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.8F, -1.9F, -0.0873F, 0.0F, 0.0F));
		head.addOrReplaceChild("beakTop", CubeListBuilder.create().texOffs(54, 0).addBox(-0.5F, -0.5F, -2.5F, 1.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, -0.1F, -2.3F, 0.1396F, 0.0F, 0.0F));
		PartDefinition tail01 = body.addOrReplaceChild("tail01", CubeListBuilder.create().texOffs(0, 10).addBox(-1.5F, -0.5F, 0.0F, 3.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, -0.9F, 3.4F, 0.0524F, 0.0F, 0.0F));
		tail01.addOrReplaceChild("tail02", CubeListBuilder.create().texOffs(0, 15).addBox(-1.49F, -0.5F, -0.4F, 3.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.8F, 0.0F, 0.0F, 0.0F, 0.0F));
		tail01.addOrReplaceChild("middleTalePlume", CubeListBuilder.create().texOffs(49, 6).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 0.0F, 6.0F), PartPose.offsetAndRotation(0.0F, -0.2F, 1.7F, 0.0F, 0.0F, 0.0F));
		tail01.addOrReplaceChild("lTailPlume", CubeListBuilder.create().texOffs(51, 13).addBox(-1.5F, -0.7F, -0.2F, 3.0F, 0.0F, 4.0F), PartPose.offsetAndRotation(-0.5F, 0.7F, 1.2F, 0.0F, -0.5236F, 0.1745F));
		tail01.addOrReplaceChild("rTailPlume", CubeListBuilder.create().texOffs(51, 13).mirror(true).addBox(-1.5F, -0.7F, -0.2F, 3.0F, 0.0F, 4.0F), PartPose.offsetAndRotation(0.5F, 0.7F, 1.2F, 0.0F, 0.5236F, -0.1745F));
		PartDefinition lLeg01 = body.addOrReplaceChild("lLeg01", CubeListBuilder.create().texOffs(34, 0).addBox(-1.0F, 0.2F, -1.0F, 1.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(-1.2F, 0.1F, 2.3F, 0.3142F, 0.0F, 0.0F));
		PartDefinition lLeg02 = lLeg01.addOrReplaceChild("lLeg02", CubeListBuilder.create().texOffs(29, 0).addBox(-0.49F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(-0.5F, 2.0F, 0.0F, -0.1396F, 0.0F, 0.0F));
		lLeg02.addOrReplaceChild("lFoot", CubeListBuilder.create().texOffs(29, 5).addBox(-0.51F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 2.6F, 0.0F, 0.1396F, 0.0F, 0.0F));
		PartDefinition rLeg01 = body.addOrReplaceChild("rLeg01", CubeListBuilder.create().texOffs(34, 0).mirror(true).addBox(0.0F, 0.2F, -1.0F, 1.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(1.2F, 0.1F, 2.3F, 0.3142F, 0.0F, 0.0F));
		PartDefinition rLeg02 = rLeg01.addOrReplaceChild("rLeg02", CubeListBuilder.create().texOffs(29, 0).mirror(true).addBox(-0.51F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.5F, 2.0F, 0.0F, -0.1396F, 0.0F, 0.0F));
		rLeg02.addOrReplaceChild("rFoot", CubeListBuilder.create().texOffs(29, 5).mirror(true).addBox(-0.49F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 2.6F, 0.1F, 0.1396F, 0.0F, 0.0F));
		body.addOrReplaceChild("tail03", CubeListBuilder.create().texOffs(0, 20).addBox(-1.5F, 0.0F, -0.8F, 3.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 0.4F, 3.5F, 0.2094F, 0.0F, 0.0F));
		body.addOrReplaceChild("bodyFeathers", CubeListBuilder.create().texOffs(26, 26).addBox(-1.5F, 0.0F, -2.5F, 3.0F, 1.0F, 5.0F), PartPose.offsetAndRotation(0.0F, 1.1F, 1.1F, 0.1396F, 0.0F, 0.0F));
		PartDefinition lWing01 = body.addOrReplaceChild("lWing01", CubeListBuilder.create().texOffs(10, 10).mirror(true).addBox(-4.0F, -0.5F, -1.0F, 4.0F, 1.0F, 6.0F), PartPose.offsetAndRotation(-1.9F, -0.3F, -1.1F, -0.0873F, 1.2741F, 0.0F));
		PartDefinition lWing02 = lWing01.addOrReplaceChild("lWing02", CubeListBuilder.create().texOffs(12, 18).mirror(true).addBox(-4.0F, -0.51F, -1.0F, 4.0F, 1.0F, 7.0F), PartPose.offsetAndRotation(-3.5F, 0.0F, 0.0F, 0.0F, 0.4538F, 0.0F));
		PartDefinition lWing03 = lWing02.addOrReplaceChild("lWing03", CubeListBuilder.create().texOffs(31, 9).mirror(true).addBox(-4.0F, -0.5F, -1.0F, 4.0F, 1.0F, 7.0F), PartPose.offsetAndRotation(-3.5F, 0.0F, 0.0F, 0.0F, 0.4887F, 0.0F));
		lWing03.addOrReplaceChild("lWing04", CubeListBuilder.create().texOffs(29, 18).mirror(true).addBox(-8.0F, -0.5F, -1.0F, 8.0F, 0.0F, 6.0F), PartPose.offsetAndRotation(-1.8F, 0.4F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rWing01 = body.addOrReplaceChild("rWing01", CubeListBuilder.create().texOffs(10, 10).addBox(0.0F, -0.5F, -1.0F, 4.0F, 1.0F, 6.0F), PartPose.offsetAndRotation(1.9F, -0.3F, -1.1F, -0.0873F, -1.2741F, 0.0F));
		PartDefinition rWing02 = rWing01.addOrReplaceChild("rWing02", CubeListBuilder.create().texOffs(12, 18).addBox(0.0F, -0.51F, -1.0F, 4.0F, 1.0F, 7.0F), PartPose.offsetAndRotation(3.5F, 0.0F, 0.0F, 0.0F, -0.4538F, 0.0F));
		PartDefinition rWing03 = rWing02.addOrReplaceChild("rWing03", CubeListBuilder.create().texOffs(31, 9).addBox(0.0F, -0.51F, -1.0F, 4.0F, 1.0F, 7.0F), PartPose.offsetAndRotation(3.5F, 0.0F, 0.0F, 0.0F, -0.4887F, 0.0F));
		rWing03.addOrReplaceChild("rWing04", CubeListBuilder.create().texOffs(29, 18).addBox(0.0F, -0.5F, -1.0F, 8.0F, 0.0F, 6.0F), PartPose.offsetAndRotation(1.8F, 0.4F, 0.0F, 0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(data, 64, 32);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		head.xRot = (float) (headPitch * (Math.PI / 180f)) + 1;
		head.yRot = (float) (headYaw * (Math.PI / 180f)) * 2 / 3f;
		boolean flying = !entity.onGround();
		lWing03.visible = lWing04.visible = rWing03.visible = rWing04.visible = flying;
		if (flying) {
			head.xRot += -2 / 3f;
			body.xRot = 0;
			lWing01.yRot = 0;
			lWing01.zRot = (float) -(Mth.cos(animationProgress) + 2 * (Math.PI / 180f)) + 1 / 3f;
			lWing02.yRot = 0;
			lWing02.zRot = lWing01.zRot / 4;
			lWing03.yRot = 0;
			lWing03.zRot = lWing02.zRot / 4;
			lWing04.zRot = lWing03.zRot / 4;
			rWing01.yRot = 0;
			rWing01.zRot = -lWing01.zRot;
			rWing02.yRot = 0;
			rWing02.zRot = -lWing02.zRot;
			rWing03.yRot = 0;
			rWing03.zRot = -lWing03.zRot;
			rWing04.zRot = -lWing04.zRot;
			lLeg01.xRot = 1;
			rLeg01.xRot = 1;
		} else {
			body.xRot = -0.3142f;
			lWing01.yRot = 1.2741f;
			lWing01.zRot = 0;
			lWing02.yRot = 0.4538f;
			lWing02.zRot = 0;
			lWing03.yRot = 0.4887f;
			lWing03.zRot = 0;
			lWing04.zRot = 0;
			rWing01.yRot = -1.2741f;
			rWing01.zRot = 0;
			rWing02.yRot = -0.4538f;
			rWing02.zRot = 0;
			rWing03.yRot = -0.4887f;
			rWing03.zRot = 0;
			rWing04.zRot = 0;
			lLeg01.xRot = (Mth.cos((float) (limbAngle + Math.PI)) * limbDistance) + 0.3142f;
			rLeg01.xRot = (Mth.cos(limbAngle) * limbDistance) + 0.3142f;
		}
		boolean sitting = entity.isInSittingPose();
		lLeg01.visible = rLeg01.visible = !sitting;
		if (sitting) {
			body.y = 21.5f;
		} else {
			body.y = 18.2f;
		}
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(body);
	}
}