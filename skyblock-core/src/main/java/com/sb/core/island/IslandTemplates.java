package com.sb.core.island;

import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class IslandTemplates {

    // Basic SkyBlock Island (L-Shape usually, or simple square for start)
    public static void generateBasicIsland(ServerWorld world, BlockPos center) {
        // 1. Bedrock at Center
        world.setBlockState(center, Blocks.BEDROCK.getDefaultState());

        // 2. Dirt Platform (3x3 around center)
        // Layer 1 (Bottom Dirt) & Layer 2 (Grass)
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos pos = center.add(x, 0, z);
                
                // Niche dirt, upar grass, except center (Bedrock)
                if (x == 0 && z == 0) continue; 
                
                world.setBlockState(pos, Blocks.DIRT.getDefaultState());
                world.setBlockState(pos.up(), Blocks.GRASS_BLOCK.getDefaultState());
            }
        }
        
        // Center ke upar Sand (Jerry spawn point simulation) ya kuch aur
        world.setBlockState(center.up(), Blocks.GRASS_BLOCK.getDefaultState());

        // 3. The Tree (Simple Oak Tree)
        // Tree coordinates relative to center
        BlockPos treeBase = center.add(2, 1, 0); 
        generateTree(world, treeBase);

        // 4. The Chest (Start loot)
        BlockPos chestPos = center.add(0, 1, 1);
        world.setBlockState(chestPos, Blocks.CHEST.getDefaultState());
        // (Future: Yahan LootTable inject karenge)
    }

    private static void generateTree(ServerWorld world, BlockPos pos) {
        // Simple Trunk
        for (int i = 0; i < 5; i++) {
            world.setBlockState(pos.up(i), Blocks.OAK_LOG.getDefaultState());
        }
        // Leaves (Simplified)
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                world.setBlockState(pos.add(x, 3, z), Blocks.OAK_LEAVES.getDefaultState());
                world.setBlockState(pos.add(x, 4, z), Blocks.OAK_LEAVES.getDefaultState());
            }
        }
        world.setBlockState(pos.up(5), Blocks.OAK_LEAVES.getDefaultState());
    }
}
