package com.sb.stats.component;

import com.sb.api.skills.ISkillData;
import com.sb.api.skills.SBSkill;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import com.sb.stats.SkyBlockStatsComponents; // Ensure this import matches your file
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class SkillComponent implements ISkillData, AutoSyncedComponent {
    private final PlayerEntity player;
    private final Map<SBSkill, Double> xpMap = new HashMap<>();
    private final Map<SBSkill, Integer> levelMap = new HashMap<>();

    public SkillComponent(PlayerEntity player) {
        this.player = player;
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
        // Simple Formula: 50 * Level^2 + 100 (Hypixel style is complex table, this is basic)
        if (level >= 50) return 1_000_000; // Max Level cap logic
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
            levelMap.put(skill, currentLevel + 1);
            xpMap.put(skill, currentXp - reqXp); // Reset XP or Keep? Hypixel keeps total, but let's reset for bar
            
            // Level Up Effects
            player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
            player.sendMessage(Text.of(skill.color + "§lSKILL LEVEL UP! " + skill.name + " " + (currentLevel + 1)), false);
        }

        // Sync to Client
        SkyBlockStatsComponents.SKILLS.sync(player);
    }

    @Override
    public void setXp(SBSkill skill, double amount) {
        xpMap.put(skill, amount);
        SkyBlockStatsComponents.SKILLS.sync(player);
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
