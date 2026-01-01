package com.sujal.skyblocksingle;

import com.sujal.skyblocksingle.commands.ModCommands;
import com.sujal.skyblocksingle.world.VoidChunkGenerator;
import com.sujal.skyblocksingle.world.island.IslandManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkyblockSingle implements ModInitializer {
    public static final String MOD_ID = "skyblocksingle";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("SkyblockSingle initializing...");

        // Register Void Chunk Generator
        Registry.register(Registries.CHUNK_GENERATOR, new Identifier(MOD_ID, "void"), VoidChunkGenerator.CODEC);

        // Register Commands
        ModCommands.register();

        // Handle Player Join (First time island generation)
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            IslandManager.onPlayerJoin(handler.getPlayer());
        });

        LOGGER.info("SkyblockSingle initialized successfully!");
    }
}
