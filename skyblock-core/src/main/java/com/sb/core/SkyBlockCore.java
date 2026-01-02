package com.sb.core;

import com.sb.api.SkyBlockAPI;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkyBlockCore implements ModInitializer {
    public static final String MOD_ID = "skyblock-core";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("SkyBlock Core Loading...");

        // Example: Event Listener using Fabric API
        // Jab player join kare, welcome message bhejo (Server-like feel)
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            handler.player.sendMessage(Text.of("§eWelcome to §bSkyBlock Singleplayer!"), false);
            
            // Future: Load Player Profile from NBT here
        });
    }
}
