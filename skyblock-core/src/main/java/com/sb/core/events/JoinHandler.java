package com.sb.core.events;

import com.sb.core.island.IslandManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class JoinHandler {
    
    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;
            
            // Check: Kya Island exist karta hai?
            // (Better way: Player NBT check karna. Simple way: Block check karna)
            BlockPos center = new BlockPos(0, 100, 0);
            
            if (player.getServerWorld().getBlockState(center).getBlock() == Blocks.AIR) {
                // Island nahi hai, naya banao!
                IslandManager.createIslandFor(player);
            }
        });
    }
}
