package com.boundess_heroes;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoundlessHeroes implements ModInitializer {
	public static final String MOD_ID = "boundless_heroes";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Boundless Heroes Initialized!");
	}
}