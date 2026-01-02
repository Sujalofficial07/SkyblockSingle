package com.sb.items;

import com.sb.items.events.TooltipHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class SkyBlockItems implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {
        // Server side logic (Items register karna)
    }

    @Override
    public void onInitializeClient() {
        // Tooltip sirf client side par hota hai
        TooltipHandler.register();
    }
}
