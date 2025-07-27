package com.boundess_heroes.hero;

import com.boundess_heroes.BoundlessHeroes;
import com.boundess_heroes.abilities.LaserVisionAbility;
import com.boundess_heroes.abilities.ModAbilities;
import com.boundless.ability.AbilityLoadout;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;

public class RobotHero extends Hero {
    public static ComponentType<Boolean> GRAPPLING = DataComponentRegistry.registerComponent("grappling",builder -> ComponentType.<Boolean>builder().codec(Codec.BOOL));
    public static ComponentType<Integer> BOUND_GRAPPLE_HOOK_ID = DataComponentRegistry.registerComponent("bound_grapple_hook_id", builder -> ComponentType.<Integer>builder().codec(Codec.INT));

    public RobotHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.attack", ModAbilities.GRAPPLE)
                .build();

        ABILITY_LOADOUTS.put("LOADOUT_1", loadout);

        this.heroData = HeroData
                .builder()
                .name("robot_hero")
                .displayName("Robot Hero")
                .textureIdentifier(BoundlessHeroes.textureID("robot_hero"))
                .defaultAbilityLoadout(ABILITY_LOADOUTS.get("LOADOUT_1"))
                .build();

        this.registerHero();
    }
}
