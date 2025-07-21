package com.boundess_heroes.hero;

import com.boundless.ability.AbilityLoadout;
import com.boundless.ability.reusable_abilities.MeleeCombatAbilities;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;

public class RobotHero extends Hero {
    public RobotHero() {
        AbilityLoadout loadout = AbilityLoadout.builder().ability("key.boundless.ability_two", MeleeCombatAbilities.DODGE).build();
        ABILITY_LOADOUTS.put("LOADOUT_1", loadout);

        this.heroData = HeroData
                .builder()
                .name("robot_hero")
                .displayName("Robot Hero")
                .defaultAbilityLoadout(ABILITY_LOADOUTS.get("LOADOUT_1"))
                .build();

        this.registerHero();
    }
}
