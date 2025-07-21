package com.boundess_heroes.registry;

import com.boundess_heroes.BoundlessHeroes;
import com.boundess_heroes.entity.GrappleEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EntityRegistry {

    public static final EntityType<GrappleEntity> GRAPPLE_ENTITY = registerEntityType("grapple_entity", GrappleEntity::new, 1.0F, 1.0F);

    public static <T extends Entity> EntityType<T> registerEntityType(String name, EntityType.EntityFactory<T> factory, float width, float height) {
        return Registry.register(Registries.ENTITY_TYPE, BoundlessHeroes.identifier(name), EntityType.Builder.create(factory, SpawnGroup.MISC).dimensions(width, height).build(name));
    }

    public static void initialize() {}
}
