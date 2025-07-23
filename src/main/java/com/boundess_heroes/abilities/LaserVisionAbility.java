package com.boundess_heroes.abilities;

import com.boundess_heroes.BoundlessHeroes;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.entity.player.PlayerEntity;

public class LaserVisionAbility {
    public static void laserVisionLogic(PlayerEntity player) {
        ParticleEmitterInfo laserParticle = ParticleEmitterInfo.create(player.getWorld(), BoundlessHeroes.identifier("laser"), BoundlessHeroes.identifier("laser_" + player.getName()));
        laserParticle.entitySpaceRelativePosition(-0.15f, player.getEyeHeight(player.getPose()), 0);
        laserParticle.bindOnEntity(player);
        //laserParticle.rotation((float) Math.toRadians(player.getPitch() * -1), (float) Math.toRadians(player.getYaw() -1), 0);
        laserParticle.scale(0.1f);
        laserParticle.parameter(0, 5);
        AAALevel.addParticle(player.getWorld(), laserParticle);

        /*
        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        tasks.put(1000, (user, heroAction) -> heroAction.discard());

        BiConsumer<PlayerEntity, HeroActionEntity> tickLogic = (user, heroAction) -> {

        };


        Action laserAction = Action.builder().customTickLogic().build();

         */
    }
}
