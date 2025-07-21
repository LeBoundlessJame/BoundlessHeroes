package com.boundess_heroes;

import com.boundess_heroes.registry.HeroRegistry;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoundlessHeroes implements ModInitializer {
	public static final String MOD_ID = "boundless_heroes";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		HeroRegistry.initialize();
		LOGGER.info("Boundless Heroes Initialized!");
	}

	public static Identifier identifier(String name) {
		return Identifier.of(MOD_ID, name);
	}
}