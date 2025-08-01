package com.boundess_heroes.abilities;

import com.boundess_heroes.BoundlessHeroes;
import com.boundess_heroes.rendering.LaserRenderingLogic;
import com.boundless.action.Action;
import com.boundless.client.RenderParameters;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.util.ActionUtils;
import com.boundless.util.RaycastUtils;
import mod.chloeprime.aaaparticles.api.client.effekseer.ParticleEmitter;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import mod.chloeprime.aaaparticles.client.registry.EffectRegistry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class LaserVisionAbility {
    public static void laserVisionLogic(PlayerEntity player) {
        ParticleEmitterInfo laserParticle = ParticleEmitterInfo.create(player.getWorld(), BoundlessHeroes.identifier("laser"), BoundlessHeroes.identifier("laser_" + player.getName()));
        laserParticle.scale(1.0f);
        laserParticle.bindOnEntity(player);
        laserParticle.useEntityHeadSpace(true);
        laserParticle.entitySpaceRelativePosition(-0.1f, -0.025f * -6, 0f);
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
        //Vec3d offset = new Vec3d(-0.1f, -0.025f * -6, 0f);
        Vec3d offset = new Vec3d(-0.1f, -0.025f, 0f);

        float tickDelta = renderParameters.tickDelta;

        if (entity != null) {
            var effect = EffectRegistry.get(BoundlessHeroes.identifier("laser"));
            if (effect == null) return;

            effect.getNamedEmitter(ParticleEmitter.Type.WORLD, BoundlessHeroes.identifier("laser_" + entity.getName()))
                    .ifPresent((particleEmitter -> {
                        LaserRenderingLogic.renderBoundEffect(entity,particleEmitter, offset, new Vec3d(0, 0, 0), tickDelta, true);
                    }));
        }
    }
}
