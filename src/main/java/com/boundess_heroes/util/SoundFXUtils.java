package com.boundess_heroes.util;

import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public class SoundFXUtils {
    public static void playSound(Entity soundOrigin, SoundEvent soundEvent, float volume, float pitch) {
        soundOrigin.getWorld().playSound(null, soundOrigin.getX(), soundOrigin.getY(), soundOrigin.getZ(), soundEvent, SoundCategory.PLAYERS, volume, pitch);
    }
}
