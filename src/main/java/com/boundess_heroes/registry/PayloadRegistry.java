package com.boundess_heroes.registry;

import com.boundess_heroes.networking.UpdateDragPayload;
import com.boundless.BoundlessAPI;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;

public class PayloadRegistry {
    public static final Identifier UPGRADE_DRAG = BoundlessAPI.identifier("update_drag");
    public static void registerPayloads() {
        PayloadTypeRegistry.playS2C().register(UpdateDragPayload.ID, UpdateDragPayload.CODEC);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(UpdateDragPayload.ID, UpdateDragPayload::receive);
    }
}
