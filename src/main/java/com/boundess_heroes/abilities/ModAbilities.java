package com.boundess_heroes.abilities;

import com.boundess_heroes.BoundlessHeroes;
import com.boundess_heroes.entity.GrappleEntity;
import com.boundess_heroes.hero.RobotHero;
import com.boundess_heroes.networking.UpdateDragPayload;
import com.boundess_heroes.registry.SoundRegistry;
import com.boundess_heroes.util.SoundFXUtils;
import com.boundless.ability.Ability;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.util.ActionUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.RaycastUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class ModAbilities {
    public static void grappleLogic(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        ItemStack heroStack = HeroUtils.getHeroStack(player);
        boolean isGrappling = heroStack.getOrDefault(RobotHero.GRAPPLING, false);
        float randomPitch = MathHelper.nextBetween(player.getRandom(), 0.9f, 1.2f);

        if (!isGrappling) {
            BlockHitResult blockHitResult = RaycastUtils.blockRaycast(player, 128);
            if (blockHitResult == null) return;
            SoundFXUtils.playSound(player, SoundRegistry.GRAPPLE_INITIATE, 1.0f, randomPitch);
            SoundFXUtils.playSound(player, SoundRegistry.GRAPPLE_IMPACT, 1.0f, randomPitch);
            SoundFXUtils.playSound(player, SoundRegistry.GRAPPLE_AMBIENT, 1.0f, randomPitch);

            GrappleEntity grappleEntity = new GrappleEntity(player, player.getWorld());
            grappleEntity.setVelocity(0, 0, 0);
            grappleEntity.setPosition(Vec3d.of(blockHitResult.getBlockPos()));
            grappleEntity.setNoGravity(true);
            player.getWorld().spawnEntity(grappleEntity);

            heroStack.set(RobotHero.BOUND_GRAPPLE_HOOK_ID, grappleEntity.getId());
        } else {
            int boundHook = heroStack.getOrDefault(RobotHero.BOUND_GRAPPLE_HOOK_ID, -1);
            if (boundHook == -1) return;

            GrappleEntity grappleEntity = (GrappleEntity) player.getWorld().getEntityById(boundHook);
            if (grappleEntity == null) return;
            grappleEntity.swingBoost(player);
            grappleEntity.discard();
            SoundFXUtils.playSound(player, SoundRegistry.GRAPPLE_AMBIENT, 1.0f, MathHelper.nextBetween(player.getRandom(), 1.1f, 1.3f));

            // Todo: maybe add a ticklogic that makes the entity persist until the player hits the ground, in which case
            // Todo: Send the packet for updating drag and discard the entity in the consumer
            LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
            int maxLifetime = 10 * 20;

            BiConsumer<PlayerEntity, HeroActionEntity> customTickLogic = (user, heroAction) -> {
                if (user.getWorld().isClient) return;
                if (user.isOnGround()) {
                    ServerPlayNetworking.send((ServerPlayerEntity) user, new UpdateDragPayload(user.getUuid()));
                    heroAction.discard();
                }
            };

            tasks.put(maxLifetime, (user, heroAction) -> {
                if (user.getWorld().isClient) return;
                ServerPlayNetworking.send((ServerPlayerEntity) user, new UpdateDragPayload(user.getUuid()));
                heroAction.discard();
            });

            Action action = Action.builder().scheduledTasks(tasks).customTickLogic(customTickLogic).hitboxWidthZ(0).hitboxWidthX(0).hitboxHeight(0).build();
            ActionUtils.performAction(player, action);

            heroStack.set(RobotHero.BOUND_GRAPPLE_HOOK_ID, -1);
        }

        heroStack.set(RobotHero.GRAPPLING, !isGrappling);
    }

    public static Ability GRAPPLE = Ability.builder()
            .abilityConsumer(ModAbilities::grappleLogic)
            .cooldown(5)
            .abilityID(BoundlessHeroes.identifier("grapple"))
            .abilityIcon(BoundlessHeroes.hudPNG("grapple")).build();
}
