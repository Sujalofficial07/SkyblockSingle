package com.sb.stats.component;

import com.sb.api.skills.ISkillData;
import com.sb.api.skills.SBSkill;
import com.sb.api.stats.SBStat; // Reward logic ke liye zaroori hai
import com.sb.stats.SkyBlockStatsComponents; 
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class SkillComponent implements ISkillData, AutoSyncedComponent {
    private final PlayerEntity player;
    private final Map<SBSkill, Double> xpMap = new HashMap<>();
    private final Map<SBSkill, Integer> levelMap = new HashMap<>();

    public SkillComponent(PlayerEntity player) {
        this.player = player;
        // NullPointerException se bachne ke liye default values set karte hain
        for (SBSkill skill : SBSkill.values()) {
            xpMap.put(skill, 0.0);
            levelMap.put(skill, 0);
        }
    }

    @Override
    public double getXp(SBSkill skill) {
        return xpMap.getOrDefault(skill, 0.0);
    }

    @Override
    public int getLevel(SBSkill skill) {
        return levelMap.getOrDefault(skill, 0);
    }

    @Override
    public double getXpForNextLevel(int level) {
        // Simple Progression Formula:
        // Level 0->1: 100 XP
        // Level 1->2: 600 XP
        // Level 2->3: 1100 XP...
        if (level >= 50) return 1_000_000_000; // Lvl 50 cap logic
        return 100 + (level * 500); 
    }

    @Override
    public void addXp(SBSkill skill, double amount) {
        double currentXp = getXp(skill);
        int currentLevel = getLevel(skill);
        
        currentXp += amount;
        xpMap.put(skill, currentXp);

        // Check for Level Up
        double reqXp = getXpForNextLevel(currentLevel);
        if (currentXp >= reqXp) {
            // Level Up Logic
            int newLevel = currentLevel + 1;
            levelMap.put(skill, newLevel);
            xpMap.put(skill, currentXp - reqXp); // XP bar reset logic (Hypixel style)
            
            // 1. Apply Stat Rewards
            applyRewards(skill, newLevel);

            // 2. Play Sound & Message
            player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
            player.sendMessage(Text.of(skill.color + "§lSKILL LEVEL UP! " + skill.name + " ➜ " + newLevel), false);
        }

        // Sync changes to Client
        SkyBlockStatsComponents.SKILLS.sync(player);
    }

    @Override
    public void setXp(SBSkill skill, double amount) {
        xpMap.put(skill, amount);
        SkyBlockStatsComponents.SKILLS.sync(player);
    }

    // ✅ Reward Logic Implementation
    private void applyRewards(SBSkill skill, int newLevel) {
        // Player stats component ko access karke base stats badhate hain
        var stats = SkyBlockStatsComponents.PLAYER_STATS.get(player);

        switch (skill) {
            case FARMING -> {
                // Reward: +2 Health per level
                double currentHp = stats.getBaseStat(SBStat.HEALTH);
                stats.setBaseStat(SBStat.HEALTH, currentHp + 2);
                player.sendMessage(Text.of("§aReward: +2 Health ❤"), false);
            }
            case MINING -> {
                // Reward: +1 Defense per level
                double currentDef = stats.getBaseStat(SBStat.DEFENSE);
                stats.setBaseStat(SBStat.DEFENSE, currentDef + 1);
                player.sendMessage(Text.of("§aReward: +1 Defense ❈"), false);
            }
            case COMBAT -> {
                // Reward: +0.5 Crit Chance per level
                double currentCc = stats.getBaseStat(SBStat.CRIT_CHANCE);
                stats.setBaseStat(SBStat.CRIT_CHANCE, currentCc + 0.5);
                player.sendMessage(Text.of("§aReward: +0.5% Crit Chance ☣"), false);
            }
            case FORAGING -> {
                // Reward: +1 Strength per level
                double currentStr = stats.getBaseStat(SBStat.STRENGTH);
                stats.setBaseStat(SBStat.STRENGTH, currentStr + 1);
                player.sendMessage(Text.of("§aReward: +1 Strength ❁"), false);
            }
            case ENCHANTING, ALCHEMY -> {
                // Reward: +1 Intelligence (Mana) per level
                double currentInt = stats.getBaseStat(SBStat.INTELLIGENCE);
                stats.setBaseStat(SBStat.INTELLIGENCE, currentInt + 1);
                player.sendMessage(Text.of("§aReward: +1 Intelligence ✎"), false);
            }
            case FISHING -> {
                // Reward: +0.5 Health (example)
                double currentHp = stats.getBaseStat(SBStat.HEALTH);
                stats.setBaseStat(SBStat.HEALTH, currentHp + 0.5);
                player.sendMessage(Text.of("§aReward: +0.5 Health ❤"), false);
            }
        }
        
        // Stats update ko turant sync karo taaki health bar update ho jaye
        SkyBlockStatsComponents.PLAYER_STATS.sync(player);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        NbtCompound skillsTag = tag.getCompound("SBSkills");
        for (SBSkill skill : SBSkill.values()) {
            if (skillsTag.contains(skill.name() + "_XP")) {
                xpMap.put(skill, skillsTag.getDouble(skill.name() + "_XP"));
                levelMap.put(skill, skillsTag.getInt(skill.name() + "_LVL"));
            }
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtCompound skillsTag = new NbtCompound();
        for (SBSkill skill : SBSkill.values()) {
            skillsTag.putDouble(skill.name() + "_XP", xpMap.get(skill));
            skillsTag.putInt(skill.name() + "_LVL", levelMap.get(skill));
        }
        tag.put("SBSkills", skillsTag);
    }
}
