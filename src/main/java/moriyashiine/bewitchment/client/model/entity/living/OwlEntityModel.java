/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.bewitchment.client.model.entity.living;

import com.google.common.collect.ImmutableList;
import moriyashiine.bewitchment.common.entity.living.OwlEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class OwlEntityModel<T extends OwlEntity> extends AgeableListModel<T> {
	private final ModelPart body;
	private final ModelPart rFoot;
	private final ModelPart lFoot;
	private final ModelPart rWing01;
	private final ModelPart rWing02;
	private final ModelPart rWing03;
	private final ModelPart rWing04;
	private final ModelPart lWing01;
	private final ModelPart lWing02;
	private final ModelPart lWing03;
	private final ModelPart lWing04;
	private final ModelPart tail01;
	private final ModelPart head;

	public OwlEntityModel(ModelPart root) {
		body = root.getChild("body");
		rFoot = body.getChild("rFoot");
		lFoot = body.getChild("lFoot");
		rWing01 = body.getChild("rWing01");
		rWing02 = rWing01.getChild("rWing02");
		rWing03 = rWing02.getChild("rWing03");
		rWing04 = rWing03.getChild("rWing04");
		lWing01 = body.getChild("lWing01");
		lWing02 = lWing01.getChild("lWing02");
		lWing03 = lWing02.getChild("lWing03");
		lWing04 = lWing03.getChild("lWing04");
		tail01 = body.getChild("tail01");
		head = body.getChild("head");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition data = new MeshDefinition();
		PartDefinition root = data.getRoot();
		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 14).addBox(-4.0F, 0.0F, -3.5F, 8.0F, 9.0F, 7.0F), PartPose.offsetAndRotation(0.0F, 13.5F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition lFoot = body.addOrReplaceChild("lFoot", CubeListBuilder.create().texOffs(32, 22).addBox(-1.0F, 0.0F, -3.4F, 2.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(-1.8F, 8.9F, -0.4F, 0.0873F, 0.0873F, 0.0F));
		lFoot.addOrReplaceChild("lBackTalon01", CubeListBuilder.create().texOffs(39, 28).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(-0.5F, 1.2F, 0.2F, -0.1745F, -0.0698F, 0.0F));
		lFoot.addOrReplaceChild("lBackTalon02", CubeListBuilder.create().texOffs(39, 28).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.5F, 1.2F, 0.2F, -0.1745F, 0.0698F, 0.0F));
		lFoot.addOrReplaceChild("lFrontTalon02", CubeListBuilder.create().texOffs(32, 28).addBox(-0.5F, -1.01F, -2.5F, 1.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.6F, 1.0F, -2.5F, 0.0F, -0.0873F, 0.0F));
		lFoot.addOrReplaceChild("lFrontTalon01", CubeListBuilder.create().texOffs(32, 28).addBox(-0.5F, -1.01F, -2.5F, 1.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(-0.6F, 1.0F, -2.5F, 0.0F, 0.0873F, 0.0F));
		PartDefinition rFoot = body.addOrReplaceChild("rFoot", CubeListBuilder.create().texOffs(32, 22).addBox(-1.0F, 0.0F, -3.4F, 2.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(1.8F, 8.9F, -0.4F, 0.0873F, -0.0873F, 0.0F));
		rFoot.addOrReplaceChild("rBackTalon01", CubeListBuilder.create().texOffs(39, 28).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.5F, 1.2F, 0.2F, -0.1745F, 0.0698F, 0.0F));
		rFoot.addOrReplaceChild("rBackTalon02", CubeListBuilder.create().texOffs(39, 28).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(-0.5F, 1.2F, 0.2F, -0.1745F, -0.0698F, 0.0F));
		rFoot.addOrReplaceChild("rFrontTalon02", CubeListBuilder.create().texOffs(32, 28).addBox(-0.5F, -1.01F, -2.5F, 1.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(-0.6F, 1.0F, -2.5F, 0.0F, 0.0873F, 0.0F));
		rFoot.addOrReplaceChild("rFrontTalon01", CubeListBuilder.create().texOffs(32, 28).addBox(-0.5F, -1.01F, -2.5F, 1.0F, 2.0F, 2.0F), PartPose.offsetAndRotation(0.6F, 1.0F, -2.5F, 0.0F, -0.0873F, 0.0F));
		PartDefinition lWing01 = body.addOrReplaceChild("lWing01", CubeListBuilder.create().texOffs(34, 0).mirror(true).addBox(-5.0F, -0.7F, -0.5F, 5.0F, 8.0F, 1.0F), PartPose.offsetAndRotation(-3.9F, 1.0F, -0.2F, 0.0F, 0.9599F, -0.8727F));
		PartDefinition lWing02 = lWing01.addOrReplaceChild("lWing02", CubeListBuilder.create().texOffs(47, 0).mirror(true).addBox(-6.0F, -0.7F, -0.5F, 6.0F, 9.0F, 1.0F), PartPose.offsetAndRotation(-4.5F, 0.0F, 0.1F, 0.0F, 0.0F, -0.6981F));
		PartDefinition lWing03 = lWing02.addOrReplaceChild("lWing03", CubeListBuilder.create().texOffs(32, 10).mirror(true).addBox(-6.0F, -0.6F, -0.5F, 6.0F, 10.0F, 1.0F), PartPose.offsetAndRotation(-4.2F, -0.1F, 0.1F, 0.0F, 0.0F, -0.6109F));
		lWing03.addOrReplaceChild("lWing04", CubeListBuilder.create().texOffs(47, 26).mirror(true).addBox(-8.0F, -0.6F, 0.0F, 8.0F, 6.0F, 0.0F), PartPose.offsetAndRotation(-2.8F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition rWing01 = body.addOrReplaceChild("rWing01", CubeListBuilder.create().texOffs(34, 0).addBox(0.0F, -0.7F, -0.5F, 5.0F, 8.0F, 1.0F), PartPose.offsetAndRotation(3.9F, 1.0F, -0.2F, 0.0F, -0.9599F, 0.8727F));
		PartDefinition rWing02 = rWing01.addOrReplaceChild("rWing02", CubeListBuilder.create().texOffs(47, 0).addBox(0.0F, -0.7F, -0.5F, 6.0F, 9.0F, 1.0F), PartPose.offsetAndRotation(4.5F, 0.0F, 0.1F, 0.0F, 0.0F, 1.0F));
		PartDefinition rWing03 = rWing02.addOrReplaceChild("rWing03", CubeListBuilder.create().texOffs(32, 10).addBox(0.0F, -0.6F, -0.5F, 6.0F, 10.0F, 1.0F), PartPose.offsetAndRotation(4.2F, -0.1F, 0.1F, 0.0F, 0.0F, 0.6109F));
		rWing03.addOrReplaceChild("rWing04", CubeListBuilder.create().texOffs(47, 26).addBox(1.0F, -0.6F, 0.0F, 8.0F, 6.0F, 0.0F), PartPose.offsetAndRotation(2.8F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition tail01 = body.addOrReplaceChild("tail01", CubeListBuilder.create().texOffs(49, 10).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 1.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 8.1F, 2.9F, -0.5236F, 0.0F, 0.0F));
		tail01.addOrReplaceChild("mTailPlume", CubeListBuilder.create().texOffs(44, 14).addBox(-2.5F, -0.4F, -0.9F, 5.0F, 0.0F, 6.0F), PartPose.offsetAndRotation(0.0F, -0.5F, 1.9F, 0.0F, 0.0F, 0.0F));
		tail01.addOrReplaceChild("lTailPlume", CubeListBuilder.create().texOffs(44, 20).addBox(-4.0F, -0.7F, -0.2F, 4.0F, 0.0F, 6.0F), PartPose.offsetAndRotation(0.9F, -0.3F, 1.2F, 0.0F, -0.3142F, -0.1745F));
		tail01.addOrReplaceChild("rTailPlume", CubeListBuilder.create().texOffs(44, 20).addBox(0.0F, -0.7F, -0.2F, 4.0F, 0.0F, 6.0F), PartPose.offsetAndRotation(-0.9F, -0.3F, 1.2F, 0.0F, 0.3142F, 0.1745F));
		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().addBox(-3.5F, -5.5F, -3.7F, 7.0F, 6.0F, 7.0F), PartPose.offsetAndRotation(0.0F, -0.3F, 0.0F, -0.0698F, 0.0F, 0.0F));
		head.addOrReplaceChild("lEar", CubeListBuilder.create().texOffs(25, 1).mirror(true).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F), PartPose.offsetAndRotation(-2.3F, -4.1F, 0.0F, -0.2618F, 0.0F, -0.4014F));
		head.addOrReplaceChild("rEar", CubeListBuilder.create().texOffs(25, 1).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F), PartPose.offsetAndRotation(2.3F, -4.1F, 0.0F, -0.2618F, 0.0F, 0.4014F));
		PartDefinition beak = head.addOrReplaceChild("beak", CubeListBuilder.create().addBox(-0.5F, -0.5F, -0.7F, 1.0F, 3.0F, 1.0F), PartPose.offsetAndRotation(0.0F, -2.0F, -3.3F, -0.0873F, 0.0F, 0.0F));
		beak.addOrReplaceChild("lBeak", CubeListBuilder.create().addBox(-0.5F, -0.5F, -0.55F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(-0.6F, 0.1F, 0.0F, 0.0F, 0.0F, -0.3491F));
		beak.addOrReplaceChild("rBeak", CubeListBuilder.create().addBox(-0.5F, -0.5F, -0.55F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(0.6F, 0.1F, 0.0F, 0.0F, 0.0F, 0.3491F));
		return LayerDefinition.create(data, 64, 32);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		head.xRot = (float) (headPitch * (Math.PI / 180f));
		head.yRot = (float) (headYaw * (Math.PI / 180f)) * 2 / 3f;
		if (!entity.onGround()) {
			head.xRot -= 1;
			body.y = 19;
			body.z = -4;
			body.xRot = 1.5f;
			tail01.xRot = -1.5f;
			lWing01.yRot = (float) -(Mth.cos(animationProgress) + 2 * (Math.PI / 180f)) + 1 / 3f;
			lWing01.zRot = 0;
			lWing02.yRot = lWing01.yRot / 4;
			lWing02.zRot = 0;
			lWing03.yRot = lWing01.yRot / 4;
			lWing03.zRot = 0;
			lWing04.visible = true;
			rWing01.yRot = -lWing01.yRot;
			rWing01.zRot = 0;
			rWing02.yRot = -lWing02.yRot;
			rWing02.zRot = 0;
			rWing03.yRot = -lWing03.yRot;
			rWing03.zRot = 0;
			rWing04.visible = true;
			lFoot.xRot = 1;
			rFoot.xRot = 1;
		} else {
			body.y = 13.5f;
			body.z = 0;
			body.xRot = 0;
			tail01.xRot = -0.5236f;
			lWing01.yRot = 0.9599f;
			lWing01.zRot = -0.8727f;
			lWing02.yRot = 0;
			lWing02.zRot = -0.6981f;
			lWing03.yRot = 0;
			lWing03.zRot = -0.6109f;
			lWing04.visible = false;
			rWing01.yRot = -0.9599f;
			rWing01.zRot = 0.8727f;
			rWing02.yRot = 0;
			rWing02.zRot = 0.6981f;
			rWing03.yRot = 0;
			rWing03.zRot = 0.6109f;
			rWing04.visible = false;
			lFoot.xRot = (Mth.cos((float) (limbAngle / 4f + Math.PI)) * limbDistance) + 0.0873f;
			rFoot.xRot = (Mth.cos(limbAngle / 4f) * limbDistance) + 0.0873f;
		}
		if (entity.isInSittingPose()) {
			lFoot.yRot = 2 / 3f;
		} else {
			lFoot.yRot = 0.0873f;
		}
		rFoot.yRot = -lFoot.yRot;
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
