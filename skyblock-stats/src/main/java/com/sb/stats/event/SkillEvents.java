package com.sb.stats.event;

import com.sb.api.skills.SBSkill;
import com.sb.stats.SkyBlockStatsComponents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Blocks;
import net.minecraft.text.Text;

public class SkillEvents {
    public static void register() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.isClient) return; // Server side only

            // Check block type
            if (state.isOf(Blocks.STONE) || state.isOf(Blocks.COBBLESTONE)) {
                // Give 1 XP
                SkyBlockStatsComponents.SKILLS.get(player).addXp(SBSkill.MINING, 1.0);
                
                // Temporary Action Bar Message
                player.sendMessage(Text.of("§7+1 Mining XP"), true);
            }
            else if (state.isOf(Blocks.COAL_ORE)) {
                SkyBlockStatsComponents.SKILLS.get(player).addXp(SBSkill.MINING, 5.0);
                player.sendMessage(Text.of("§7+5 Mining XP"), true);
            }
        });
    }
}
