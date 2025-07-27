package com.boundess_heroes.abilities;

import com.boundess_heroes.BoundlessHeroes;
import com.boundless.ability.Ability;

public class ModAbilities {
    public static Ability GRAPPLE = Ability.builder()
            .abilityConsumer(GrappleAbility::grappleLogic)
            .cooldown(5)
            .abilityID(BoundlessHeroes.identifier("grapple"))
            .abilityIcon(BoundlessHeroes.hudPNG("grapple")).build();

    /*
    public static Ability LASER_VISION = Ability.builder()
            .abilityConsumer(LaserVisionAbility::laserVisionLogic)
            .cooldown(10)
            .abilityID(BoundlessHeroes.identifier("laser_vision"))
            .abilityIcon(BoundlessHeroes.hudPNG("red")).build();

     */
}
