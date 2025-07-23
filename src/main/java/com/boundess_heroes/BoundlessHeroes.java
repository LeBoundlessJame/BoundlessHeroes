package com.boundess_heroes;

import com.boundess_heroes.registry.EntityRegistry;
import com.boundess_heroes.registry.HeroRegistry;
import com.boundess_heroes.registry.PayloadRegistry;
import com.boundess_heroes.registry.SoundRegistry;
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
		EntityRegistry.initialize();
		PayloadRegistry.registerPayloads();
		SoundRegistry.initialize();
		LOGGER.info("Boundless Heroes Initialized!");
	}

	public static Identifier identifier(String name) {
		return Identifier.of(MOD_ID, name);
	}

	public static Identifier hudPNG(String name) {
		return identifier("textures/gui/sprites/hud/" + name + ".png");
	}

	public static Identifier textureID(String name) {
		return Identifier.of(BoundlessHeroes.MOD_ID, "textures/item/hero/" + name + ".png");
	}

	public static Identifier modelID(String name) {
		return Identifier.of(BoundlessHeroes.MOD_ID, "geo/item/" + name + ".geo.json");
	}
}