package com.boundess_heroes.registry;

import com.boundess_heroes.abilities.LaserVisionAbility;
import com.boundless.registry.RenderLogicRegistry;

public class CustomRenderLogicRegistry {
    public static void initialize() {
        RenderLogicRegistry.addRenderEntry("laser_vision", LaserVisionAbility::laserRenderLogic);
    }
}
