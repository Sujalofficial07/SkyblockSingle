package com.sujal.skyblocksingle.world.island;

import com.sujal.skyblocksingle.data.SkyblockData;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IslandManager {

    private static final BlockPos ISLAND_CENTER = new BlockPos(0, 100, 0);

    public static void onPlayerJoin(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        SkyblockData data = SkyblockData.getServerState(world);

        if (!data.hasIsland(player)) {
            createIsland(world, ISLAND_CENTER);
            data.setHasIsland(player, true);
            
            // Teleport player
            player.teleport(world, 0.5, 101, 0.5, 0, 0);
            player.sendMessage(Text.of("§aWelcome to Skyblock Single!"), false);
        }
    }

    public static void resetIsland(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        createIsland(world, ISLAND_CENTER);
        player.teleport(world, 0.5, 101, 0.5, 0, 0);
        
        // Reset coins
        SkyblockData data = SkyblockData.getServerState(world);
        data.setCoins(player, 0);
        
        player.sendMessage(Text.of("§cIsland reset!"), false);
    }

    private static void createIsland(ServerWorld world, BlockPos center) {
        // Clear area (simple 10x10 cube clear)
        for (int x = -5; x <= 5; x++) {
            for (int y = -5; y <= 10; y++) {
                for (int z = -5; z <= 5; z++) {
                    world.setBlockState(center.add(x, y, z), Blocks.AIR.getDefaultState());
                }
            }
        }

        // Base
        world.setBlockState(center, Blocks.BEDROCK.getDefaultState());
        world.setBlockState(center.up(), Blocks.DIRT.getDefaultState());
        world.setBlockState(center.up().west(), Blocks.DIRT.getDefaultState());
        world.setBlockState(center.up().east(), Blocks.DIRT.getDefaultState());
        world.setBlockState(center.up().north(), Blocks.DIRT.getDefaultState());
        world.setBlockState(center.up().south(), Blocks.DIRT.getDefaultState());
        
        // Grass
        world.setBlockState(center.up(2), Blocks.GRASS_BLOCK.getDefaultState());
        world.setBlockState(center.up(2).west(), Blocks.GRASS_BLOCK.getDefaultState());
        world.setBlockState(center.up(2).east(), Blocks.GRASS_BLOCK.getDefaultState());
        world.setBlockState(center.up(2).north(), Blocks.GRASS_BLOCK.getDefaultState());
        world.setBlockState(center.up(2).south(), Blocks.GRASS_BLOCK.getDefaultState());

        // Tree (Oak)
        BlockPos treeBase = center.up(3).north(2);
        // Log
        for(int i=0; i<4; i++) {
            world.setBlockState(treeBase.up(i), Blocks.OAK_LOG.getDefaultState());
        }
        // Leaves (Simplified cube)
        for(int x=-1; x<=1; x++) {
            for(int z=-1; z<=1; z++) {
                 world.setBlockState(treeBase.up(3).add(x, 0, z), Blocks.OAK_LEAVES.getDefaultState());
                 world.setBlockState(treeBase.up(4).add(x, 0, z), Blocks.OAK_LEAVES.getDefaultState());
            }
        }
        world.setBlockState(treeBase.up(5), Blocks.OAK_LEAVES.getDefaultState());

        // Chest
        world.setBlockState(center.up(3).west(), Blocks.CHEST.getDefaultState());
    }
}
