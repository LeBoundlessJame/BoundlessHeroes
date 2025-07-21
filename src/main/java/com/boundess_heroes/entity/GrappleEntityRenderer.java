package com.boundess_heroes.entity;

import mod.azure.azurelib.core.object.Axis;
import mod.azure.azurelib.rewrite.render.entity.AzEntityLeashRenderUtil;
import mod.azure.azurelib.rewrite.render.entity.AzEntityRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class GrappleEntityRenderer extends EntityRenderer<PersistentProjectileEntity> {
    public static final int ROPE_PIECE_COUNT = 96;

    // Todo: Replace with proper checks / conditions later
    @Override
    public boolean shouldRender(PersistentProjectileEntity mobEntity, Frustum frustum, double d, double e, double f) {
        return true;
    }

    @Override
    public void render(PersistentProjectileEntity grappleEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(grappleEntity, f, g, matrixStack, vertexConsumerProvider, i);
        Entity entity = grappleEntity.getOwner();
        if (entity != null) {
            renderRope(grappleEntity, g, matrixStack, vertexConsumerProvider, (LivingEntity) entity);
        }
    }

    public static <T extends Entity, E extends Entity, M extends Entity> void renderRope(M mob, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, E leashHolder) {
        double lerpBodyAngle = (double)(MathHelper.lerp(partialTick, mob.prevYaw, mob.prevYaw) * 0.017453292F + 1.5707964F);
        Vec3d leashOffset = mob.getLeashOffset(partialTick);
        double xAngleOffset = Math.cos(lerpBodyAngle) * leashOffset.z + Math.sin(lerpBodyAngle) * leashOffset.x;
        double zAngleOffset = Math.sin(lerpBodyAngle) * leashOffset.z - Math.cos(lerpBodyAngle) * leashOffset.x;
        double lerpOriginX = MathHelper.lerp((double)partialTick, mob.prevX, mob.getX()) + xAngleOffset;
        double lerpOriginY = MathHelper.lerp((double)partialTick, mob.prevY, mob.getY()) + leashOffset.y;
        double lerpOriginZ = MathHelper.lerp((double)partialTick, mob.prevZ, mob.getZ()) + zAngleOffset;
        Vec3d ropeGripPosition = leashHolder.getLeashPos(partialTick);
        float xDif = (float)(ropeGripPosition.x - lerpOriginX);
        float yDif = (float)(ropeGripPosition.y - lerpOriginY);
        float zDif = (float)(ropeGripPosition.z - lerpOriginZ);
        float offsetMod = MathHelper.inverseSqrt(xDif * xDif + zDif * zDif) * 0.025F / 2.0F;
        float xOffset = zDif * offsetMod;
        float zOffset = xDif * offsetMod;
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderLayer.getLeash());
        BlockPos entityEyePos = BlockPos.ofFloored(mob.getCameraPosVec(partialTick));
        BlockPos holderEyePos = BlockPos.ofFloored(leashHolder.getCameraPosVec(partialTick));
        int entityBlockLight = mob.getWorld().getLightLevel(entityEyePos);
        int holderBlockLight = leashHolder.isOnFire() ? 15 : leashHolder.getWorld().getLightLevel(LightType.BLOCK, holderEyePos);
        int entitySkyLight = mob.getWorld().getLightLevel(LightType.SKY, entityEyePos);
        int holderSkyLight = mob.getWorld().getLightLevel(LightType.SKY, holderEyePos);
        poseStack.push();
        poseStack.translate(xAngleOffset, leashOffset.y, zAngleOffset);
        Matrix4f posMatrix = new Matrix4f(poseStack.peek().getPositionMatrix());

        int segment;
        for(segment = 0; segment <= 24; ++segment) {
            renderLeashPiece(vertexConsumer, posMatrix, xDif, yDif, zDif, entityBlockLight, holderBlockLight, entitySkyLight, holderSkyLight, 0.025F, 0.025F, xOffset, zOffset, segment, false);
        }

        for(segment = 24; segment >= 0; --segment) {
            renderLeashPiece(vertexConsumer, posMatrix, xDif, yDif, zDif, entityBlockLight, holderBlockLight, entitySkyLight, holderSkyLight, 0.025F, 0.0F, xOffset, zOffset, segment, true);
        }

        poseStack.pop();
    }

    private static void renderLeashPiece(VertexConsumer buffer, Matrix4f positionMatrix, float xDif, float yDif, float zDif, int entityBlockLight, int holderBlockLight, int entitySkyLight, int holderSkyLight, float width, float yOffset, float xOffset, float zOffset, int segment, boolean isLeashKnot) {
        float piecePosPercent = (float)segment / 24.0F;
        int lerpBlockLight = (int)MathHelper.lerp(piecePosPercent, (float)entityBlockLight, (float)holderBlockLight);
        int lerpSkyLight = (int)MathHelper.lerp(piecePosPercent, (float)entitySkyLight, (float)holderSkyLight);
        int packedLight = LightmapTextureManager.pack(lerpBlockLight, lerpSkyLight);
        float knotColourMod = segment % 2 == (isLeashKnot ? 1 : 0) ? 0.7F : 1.0F;
        float red = 0.5F * knotColourMod;
        float green = 0.4F * knotColourMod;
        float blue = 0.3F * knotColourMod;
        float x = xDif * piecePosPercent;
        float y = yDif > 0.0F ? yDif * piecePosPercent * piecePosPercent : yDif - yDif * (1.0F - piecePosPercent) * (1.0F - piecePosPercent);
        float z = zDif * piecePosPercent;
        buffer.vertex(positionMatrix, x - xOffset, y + yOffset, z + zOffset).color(red, green, blue, 1.0F).light(packedLight);
        buffer.vertex(positionMatrix, x + xOffset, y + width - yOffset, z - zOffset).color(red, green, blue, 1.0F).light(packedLight);
    }

    public GrappleEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(PersistentProjectileEntity entity) {
        return Identifier.of("textures/entity/lead_knot.png");
    }
}
