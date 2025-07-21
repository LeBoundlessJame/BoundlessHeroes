package com.boundess_heroes.abilities;

import com.boundess_heroes.BoundlessHeroes;
import com.boundess_heroes.hero.RobotHero;
import com.boundless.ability.Ability;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;

public class ModAbilities {
    public static void grappleLogic(PlayerEntity player) {
        boolean isGrappling = HeroUtils.getHeroStack(player).getOrDefault(RobotHero.GRAPPLING, false);
        if (!isGrappling) {
            HeroUtils.getHeroStack(player).set(RobotHero.GRAPPLING, true);
        }
    }

    public static Ability GRAPPLE = Ability.builder()
            .abilityConsumer(ModAbilities::grappleLogic)
            .cooldown(5)
            .abilityID(BoundlessHeroes.identifier("grapple"))
            .abilityIcon(BoundlessHeroes.hudPNG("grapple")).build();
}
