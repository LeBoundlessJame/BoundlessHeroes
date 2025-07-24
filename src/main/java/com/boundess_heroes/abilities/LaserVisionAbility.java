package com.boundess_heroes.abilities;

import com.boundess_heroes.BoundlessHeroes;
import com.boundless.action.Action;
import com.boundless.client.RenderParameters;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.util.ActionUtils;
import com.boundless.util.RaycastUtils;
import mod.chloeprime.aaaparticles.api.client.effekseer.ParticleEmitter;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import mod.chloeprime.aaaparticles.client.registry.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class LaserVisionAbility {
    public static void laserVisionLogic(PlayerEntity player) {
        ParticleEmitterInfo laserParticle = ParticleEmitterInfo.create(player.getWorld(), BoundlessHeroes.identifier("laser"), BoundlessHeroes.identifier("laser_" + player.getName()));
        laserParticle.scale(1.0f);
        //laserParticle.position(player.getEyePos().add(0.1f, -0.05f, 0));
        laserParticle.parameter(0, 0.3f);
        AAALevel.addParticle(player.getWorld(), laserParticle);

        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        tasks.put(1000, (user, heroAction) -> {
            var effect = EffectRegistry.get(BoundlessHeroes.identifier("laser"));
            if (effect == null) return;
            effect.getNamedEmitter(ParticleEmitter.Type.WORLD, BoundlessHeroes.identifier("laser_" + user.getName())).ifPresent(
                    particleEmitter -> particleEmitter.sendTrigger(0));
            heroAction.discard();
        });

        BiConsumer<PlayerEntity, HeroActionEntity> tickLogic = (user, heroAction) -> {
            if (player.age % 2 != 0) return;
            EntityHitResult entityRaycast = RaycastUtils.raycast(player, 64);
            if (entityRaycast != null && entityRaycast.getEntity() instanceof LivingEntity livingEntity) {
                livingEntity.timeUntilRegen = 0;
                livingEntity.damage(livingEntity.getDamageSources().generic(), 1.0f);
            }
        };

        Action laserAction = Action.builder().customTickLogic(tickLogic).renderLogicID("laser_vision").scheduledTasks(tasks).hitboxHeight(1).hitboxWidthX(1).hitboxWidthZ(1).build();
        ActionUtils.performAction(player, laserAction);
    }

    public static void laserRenderLogic(HeroActionEntity heroAction, RenderParameters renderParameters) {
        PlayerEntity entity = (PlayerEntity) heroAction.getOwner();
        float tickDelta = renderParameters.tickDelta;

        if (entity != null) {
            var effect = EffectRegistry.get(BoundlessHeroes.identifier("laser"));
            if (effect == null) return;

            Vec3d eyePos = entity.getCameraPosVec(tickDelta).add(-0.15f, -0.05f, 0);
            float lerpedX = (float) eyePos.x;
            float lerpedY = (float) eyePos.y;
            float lerpedZ = (float) eyePos.z;

            float pitch = (float) Math.toRadians(MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch()));
            float yaw = (float) Math.toRadians(-MathHelper.lerp(tickDelta, entity.prevHeadYaw, entity.getHeadYaw()));

            effect.getNamedEmitter(ParticleEmitter.Type.WORLD, BoundlessHeroes.identifier("laser_" + entity.getName()))
                    .ifPresent((particleEmitter -> {
                        particleEmitter.setPosition(lerpedX, lerpedY, lerpedZ);
                        particleEmitter.setRotation(pitch, yaw, 0);
                    }));
        }
    }
}
