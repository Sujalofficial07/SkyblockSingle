package com.sujal.skyblocksingle.skills;

import com.sujal.skyblocksingle.data.SkyblockData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SkillManager {

    // Simple XP Curve: Level * 100 + 50 (e.g., Lvl 1=50, Lvl 2=150, Lvl 3=350...)
    public static int getLevel(double xp) {
        int level = 0;
        double xpRequired = 50;
        
        while (xp >= xpRequired && level < 50) {
            xp -= xpRequired;
            level++;
            xpRequired += (level * 50) + 50;
        }
        return level;
    }

    public static void addXp(ServerPlayerEntity player, SkillType type, double amount) {
        SkyblockData data = SkyblockData.getServerState(player.getServerWorld());
        
        int oldLevel = getLevel(data.getSkillXp(player, type));
        data.addSkillXp(player, type, amount);
        double newXp = data.getSkillXp(player, type);
        int newLevel = getLevel(newXp);

        // Action Bar Notification
        player.sendMessage(Text.of(type.getColor() + "+ " + String.format("%.1f", amount) + " " + type.getDisplayName() + " XP"), true);

        // Level Up Message
        if (newLevel > oldLevel) {
            player.sendMessage(Text.of("§6§l--------------------------------"), false);
            player.sendMessage(Text.of("§b§lSKILL LEVEL UP §3" + type.getDisplayName() + " §8" + oldLevel + "➜§a" + newLevel), false);
            player.sendMessage(Text.of("§6§l--------------------------------"), false);
            
            // Give coin reward for leveling up
            data.addCoins(player, newLevel * 100); 
            player.sendMessage(Text.of("§6+ " + (newLevel * 100) + " Coins"), false);
        }
    }
}
