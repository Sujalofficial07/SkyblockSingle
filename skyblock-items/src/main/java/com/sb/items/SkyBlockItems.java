package com.sb.items;

import com.sb.items.commands.TestItemCommand; // Import the command
import com.sb.items.events.TooltipHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback; // Fabric API for commands

public class SkyBlockItems implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {
        // ✅ Register Command Here
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            TestItemCommand.register(dispatcher);
        });
    }

    @Override
    public void onInitializeClient() {
        TooltipHandler.register();
    }
}
