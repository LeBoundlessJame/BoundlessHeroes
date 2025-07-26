package com.boundess_heroes.rendering;

import mod.chloeprime.aaaparticles.api.client.effekseer.ParticleEmitter;
import mod.chloeprime.aaaparticles.common.util.Basis;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class LaserRenderingLogic {

    public static void renderBoundEffect(Entity boundEntity, ParticleEmitter emitter, Vec3d offset, Vec3d rotation, float tickDelta, boolean useHeadSpace) {
        if (boundEntity == null || !boundEntity.isAlive()) return;

        float entityYaw = (float) Math.toRadians(boundEntity.getYaw(tickDelta));
        float entityPitch = (float) Math.toRadians(boundEntity.getPitch(tickDelta));

        Basis basis = useHeadSpace ? Basis.fromEuler(new Vec3d(-entityPitch, (float) Math.PI - entityYaw, 0)) : Basis.fromEntityBody(boundEntity);
        Vec3d relativeOffset = basis.toGlobal(offset);

        float finalX = (float) (boundEntity.getX() + relativeOffset.x);
        float finalY = (float) (boundEntity.getY() + relativeOffset.y + (useHeadSpace ? boundEntity.getStandingEyeHeight() : 0.0F));
        float finalZ = (float) (boundEntity.getZ() + relativeOffset.z);

        emitter.setPosition(finalX, finalY, finalZ);
        emitter.setRotation((float) (rotation.x + entityPitch), (float) (rotation.y - entityYaw), (float) rotation.z);
    }

}
