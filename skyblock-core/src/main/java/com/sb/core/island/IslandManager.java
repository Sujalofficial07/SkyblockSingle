package com.sb.core.island;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public class IslandManager {

    // Singleplayer ke liye fixed position. 
    // Multiplayer logic mein ye har player ke liye alag hoga (e.g., x += 1000).
    private static final BlockPos ISLAND_CENTER = new BlockPos(0, 100, 0);

    public static void createIslandFor(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();

        // 1. Generate Structure
        player.sendMessage(Text.of("§aGenerating your Island..."), false);
        IslandTemplates.generateBasicIsland(world, ISLAND_CENTER);

        // 2. Teleport Player
        // Thoda upar teleport karte hain taaki gir na jaye
        player.teleport(world, ISLAND_CENTER.getX() + 0.5, ISLAND_CENTER.getY() + 2, ISLAND_CENTER.getZ() + 0.5, 0, 0);

        // 3. Set Gamemode
        player.changeGameMode(GameMode.SURVIVAL);
        
        // 4. Set Spawn Point
        player.setSpawnPoint(world.getRegistryKey(), ISLAND_CENTER.up(), 0, true, false);
        
        player.sendMessage(Text.of("§6Welcome to SkyBlock!"), false);
    }
}
