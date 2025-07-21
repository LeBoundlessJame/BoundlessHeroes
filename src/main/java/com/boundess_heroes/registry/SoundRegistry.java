package com.boundess_heroes.registry;

import com.boundess_heroes.BoundlessHeroes;
import com.boundless.BoundlessAPI;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundRegistry {
    public static SoundEvent GRAPPLE_AMBIENT = registerSoundEvent(BoundlessHeroes.identifier("grapple_ambient"));
    public static SoundEvent GRAPPLE_INITIATE = registerSoundEvent(BoundlessHeroes.identifier("grapple_initiate"));
    public static SoundEvent GRAPPLE_IMPACT = registerSoundEvent(BoundlessHeroes.identifier("grapple_impact"));

    public static SoundEvent registerSoundEvent(Identifier identifier) {
        Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
        return SoundEvent.of(identifier);
    }
    public static void initialize() {}
}
