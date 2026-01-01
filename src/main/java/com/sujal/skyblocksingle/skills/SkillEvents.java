package com.sujal.skyblocksingle.skills;

import com.sujal.skyblocksingle.world.island.IslandManager;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class SkillEvents {

    private static final Map<Block, Double> MINING_XP = new HashMap<>();
    private static final Map<Block, Double> FARMING_XP = new HashMap<>();

    static {
        // Mining XP Values
        MINING_XP.put(Blocks.STONE, 1.0);
        MINING_XP.put(Blocks.COBBLESTONE, 1.0);
        MINING_XP.put(Blocks.COAL_ORE, 5.0);
        MINING_XP.put(Blocks.IRON_ORE, 10.0);
        MINING_XP.put(Blocks.DIAMOND_ORE, 20.0);
        MINING_XP.put(Blocks.DEEPSLATE, 2.0);

        // Farming XP Values
        FARMING_XP.put(Blocks.WHEAT, 4.0);
        FARMING_XP.put(Blocks.CARROTS, 4.0);
        FARMING_XP.put(Blocks.POTATOES, 4.0);
        FARMING_XP.put(Blocks.PUMPKIN, 5.0);
        FARMING_XP.put(Blocks.MELON, 5.0);
        FARMING_XP.put(Blocks.HAY_BLOCK, 10.0);
    }

    public static void register() {
        // Block Break Event
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.isClient || !(player instanceof ServerPlayerEntity)) return;
            
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            Block block = state.getBlock();

            if (MINING_XP.containsKey(block)) {
                SkillManager.addXp(serverPlayer, SkillType.MINING, MINING_XP.get(block));
            } else if (FARMING_XP.containsKey(block)) {
                SkillManager.addXp(serverPlayer, SkillType.FARMING, FARMING_XP.get(block));
            }
        });

        // Mob Kill Event (Combat XP)
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
            if (!world.isClient && entity instanceof ServerPlayerEntity) {
                SkillManager.addXp((ServerPlayerEntity) entity, SkillType.COMBAT, 10.0); // 10 XP per kill for now
            }
        });
    }
}
