package com.sb.core;

import com.sb.api.SkyBlockAPI;
import com.sb.core.events.JoinHandler; // Import this
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkyBlockCore implements ModInitializer {
    public static final String MOD_ID = "skyblock-core";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("SkyBlock Core Loading...");
        
        // Register the Join Listener
        JoinHandler.register();
    }
}
