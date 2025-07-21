package com.boundess_heroes.hero;

import com.boundess_heroes.abilities.ModAbilities;
import com.boundless.ability.AbilityLoadout;
import com.boundless.ability.reusable_abilities.MeleeCombatAbilities;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;

public class RobotHero extends Hero {
    public static ComponentType<Boolean> GRAPPLING = DataComponentRegistry.registerComponent("grappling",builder -> ComponentType.<Boolean>builder().codec(Codec.BOOL));

    public RobotHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.boundless.ability_two", MeleeCombatAbilities.DODGE)
                .ability("key.attack", ModAbilities.GRAPPLE)
                .build();

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
