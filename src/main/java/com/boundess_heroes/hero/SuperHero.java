package com.boundess_heroes.hero;

import com.boundess_heroes.BoundlessHeroes;
import com.boundess_heroes.abilities.ModAbilities;
import com.boundless.ability.AbilityLoadout;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;

public class SuperHero extends Hero {
    public SuperHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.boundless.ability_one", ModAbilities.LASER_VISION)
                .build();

        ABILITY_LOADOUTS.put("LOADOUT_1", loadout);

        this.heroData = HeroData
                .builder()
                .name("super_hero")
                .displayName("Super Hero")
                .textureIdentifier(BoundlessHeroes.textureID("super_hero"))
                .defaultAbilityLoadout(ABILITY_LOADOUTS.get("LOADOUT_1"))
                .build();

        this.registerHero();
    }
}
