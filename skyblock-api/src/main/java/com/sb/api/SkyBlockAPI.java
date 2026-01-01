package com.sb.api;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkyBlockAPI implements ModInitializer {
    public static final String MOD_ID = "skyblock-api";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("SkyBlock API Initialized");
    }
    
    // Future mein yahan Events register karenge
    // e.g., public static final Event<PlayerStatsUpdate> ...
}
