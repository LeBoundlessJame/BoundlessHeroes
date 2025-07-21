package com.boundess_heroes.abilities;

import com.boundess_heroes.BoundlessHeroes;
import com.boundess_heroes.entity.GrappleEntity;
import com.boundess_heroes.hero.RobotHero;
import com.boundless.ability.Ability;
import com.boundless.util.HeroUtils;
import com.boundless.util.RaycastUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

public class ModAbilities {
    public static void grappleLogic(PlayerEntity player) {
        BlockHitResult blockHitResult = RaycastUtils.blockRaycast(player, 64);
        if (blockHitResult == null) return;

        if (!player.getWorld().isClient) {
            GrappleEntity grappleEntity = new GrappleEntity(player, player.getWorld());
            grappleEntity.setVelocity(0, 0, 0);
            grappleEntity.setPosition(Vec3d.of(blockHitResult.getBlockPos()));
            grappleEntity.setNoGravity(true);
            player.getWorld().spawnEntity(grappleEntity);
            player.sendMessage(Text.of("THWIP"), true);
        }

        /*
        boolean isGrappling = HeroUtils.getHeroStack(player).getOrDefault(RobotHero.GRAPPLING, false);
        if (!isGrappling) {
            BlockHitResult blockHitResult = RaycastUtils.blockRaycast(player, 64);
            if (blockHitResult == null) return;

            GrappleEntity grappleEntity = new GrappleEntity(player, player.getWorld());
            grappleEntity.setVelocity(0, 0, 0);
            grappleEntity.setPosition(Vec3d.of(blockHitResult.getBlockPos()));
            grappleEntity.setNoGravity(true);
            player.getWorld().spawnEntity(grappleEntity);
            System.out.println("Boink!");
        }
        HeroUtils.getHeroStack(player).set(RobotHero.GRAPPLING, !isGrappling);

         */
    }

    public static Ability GRAPPLE = Ability.builder()
            .abilityConsumer(ModAbilities::grappleLogic)
            .cooldown(5)
            .abilityID(BoundlessHeroes.identifier("grapple"))
            .abilityIcon(BoundlessHeroes.hudPNG("grapple")).build();
}
