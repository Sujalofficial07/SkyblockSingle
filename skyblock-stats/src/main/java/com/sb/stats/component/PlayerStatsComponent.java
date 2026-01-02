package com.sb.stats.component;

import com.sb.api.stats.IPlayerStats;
import com.sb.api.stats.SBStat;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Map;

public class PlayerStatsComponent implements IPlayerStats {
    private final PlayerEntity player;
    private final Map<SBStat, Double> stats = new HashMap<>();
    private double currentMana = 100;

    public PlayerStatsComponent(PlayerEntity player) {
        this.player = player;
        // Default Stats (Hypixel Starter)
        stats.put(SBStat.HEALTH, 100.0);
        stats.put(SBStat.DEFENSE, 0.0);
        stats.put(SBStat.STRENGTH, 0.0);
        stats.put(SBStat.CRIT_CHANCE, 30.0);
        stats.put(SBStat.CRIT_DAMAGE, 50.0);
        stats.put(SBStat.INTELLIGENCE, 100.0); // 100 Intel = 100 Max Mana
        stats.put(SBStat.SPEED, 100.0);
    }

    @Override
    public double getBaseStat(SBStat stat) {
        return stats.getOrDefault(stat, 0.0);
    }

    @Override
    public void setBaseStat(SBStat stat, double value) {
        stats.put(stat, value);
    }

    @Override
    public double getCurrentMana() {
        return currentMana;
    }

    @Override
    public void consumeMana(double amount) {
        this.currentMana = Math.max(0, currentMana - amount);
    }

    @Override
    public void serverTick() {
        // Mana Regeneration Logic (2% per second aka every 20 ticks?)
        // Hypixel: 2% of Max Mana per second.
        if (player.age % 20 == 0) { 
            regenMana();
        }
    }

    @Override
    public void regenMana() {
        double maxMana = getBaseStat(SBStat.INTELLIGENCE); // Intel = Max Mana
        double regenAmount = maxMana * 0.02; // 2% regen
        
        if (currentMana < maxMana) {
            currentMana = Math.min(maxMana, currentMana + regenAmount);
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        NbtCompound statsTag = tag.getCompound("SBStats");
        for (SBStat stat : SBStat.values()) {
            if (statsTag.contains(stat.name())) {
                stats.put(stat, statsTag.getDouble(stat.name()));
            }
        }
        currentMana = tag.getDouble("CurrentMana");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtCompound statsTag = new NbtCompound();
        stats.forEach((key, value) -> statsTag.putDouble(key.name(), value));
        
        tag.put("SBStats", statsTag);
        tag.putDouble("CurrentMana", currentMana);
    }
}
