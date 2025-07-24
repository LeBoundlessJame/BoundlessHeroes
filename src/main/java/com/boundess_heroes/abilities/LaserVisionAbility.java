package com.boundess_heroes.abilities;

import com.boundess_heroes.BoundlessHeroes;
import com.boundless.action.Action;
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

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class LaserVisionAbility {
    public static void laserVisionLogic(PlayerEntity player) {
        ParticleEmitterInfo laserParticle = ParticleEmitterInfo.create(player.getWorld(), BoundlessHeroes.identifier("laser"), BoundlessHeroes.identifier("laser_" + player.getName()));
        laserParticle.scale(1.0f);
        laserParticle.position(player.getEyePos().add(0.1f, -0.05f, 0));
        laserParticle.parameter(0, 0.3f);
        AAALevel.addParticle(player.getWorld(), laserParticle);

        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        tasks.put(1000, (user, heroAction) -> heroAction.discard());

        BiConsumer<PlayerEntity, HeroActionEntity> tickLogic = (user, heroAction) -> {
            if (player.age % 2 != 0) return;
            EntityHitResult entityRaycast = RaycastUtils.raycast(player, 64);
            if (entityRaycast != null && entityRaycast.getEntity() instanceof LivingEntity livingEntity) {
                livingEntity.timeUntilRegen = 0;
                livingEntity.damage(livingEntity.getDamageSources().generic(), 1.0f);
            }
        };

        Action laserAction = Action.builder().customTickLogic(tickLogic).scheduledTasks(tasks).hitboxHeight(0).hitboxWidthX(0).hitboxWidthZ(0).build();
        ActionUtils.performAction(player, laserAction);
    }
}
