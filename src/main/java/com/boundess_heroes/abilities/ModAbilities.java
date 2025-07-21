package com.boundess_heroes.abilities;

import com.boundess_heroes.BoundlessHeroes;
import com.boundess_heroes.entity.GrappleEntity;
import com.boundess_heroes.hero.RobotHero;
import com.boundless.ability.Ability;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.util.ActionUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.RaycastUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class ModAbilities {
    public static void grappleLogic(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        ItemStack heroStack = HeroUtils.getHeroStack(player);
        boolean isGrappling = heroStack.getOrDefault(RobotHero.GRAPPLING, false);

        if (!isGrappling) {
            BlockHitResult blockHitResult = RaycastUtils.blockRaycast(player, 128);
            if (blockHitResult == null) return;

            GrappleEntity grappleEntity = new GrappleEntity(player, player.getWorld());
            grappleEntity.setVelocity(0, 0, 0);
            grappleEntity.setPosition(Vec3d.of(blockHitResult.getBlockPos()));
            grappleEntity.setNoGravity(true);
            player.getWorld().spawnEntity(grappleEntity);

            heroStack.set(RobotHero.BOUND_GRAPPLE_HOOK_ID, grappleEntity.getId());
            player.playSound(SoundEvents.ITEM_TOTEM_USE, 0.5f, 1.0f);
        } else {
            LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
            tasks.put(10, (user, heroAction) -> {
                System.out.println(user.getWorld());
                user.setNoDrag(false);
                System.out.println(user.hasNoDrag());
            });

            int boundHook = heroStack.getOrDefault(RobotHero.BOUND_GRAPPLE_HOOK_ID, -1);
            if (boundHook == -1) return;

            GrappleEntity grappleEntity = (GrappleEntity) player.getWorld().getEntityById(boundHook);
            if (grappleEntity == null) return;
            grappleEntity.swingBoost(player);
            grappleEntity.discard();
            Action action = Action.builder().scheduledTasks(tasks).hitboxWidthZ(0).hitboxWidthX(0).hitboxHeight(0).build();
            ActionUtils.performAction(player, action);

            heroStack.set(RobotHero.BOUND_GRAPPLE_HOOK_ID, -1);
            player.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0f, 1.0f);
        }

        heroStack.set(RobotHero.GRAPPLING, !isGrappling);
    }

    public static Ability GRAPPLE = Ability.builder()
            .abilityConsumer(ModAbilities::grappleLogic)
            .cooldown(5)
            .abilityID(BoundlessHeroes.identifier("grapple"))
            .abilityIcon(BoundlessHeroes.hudPNG("grapple")).build();
}
