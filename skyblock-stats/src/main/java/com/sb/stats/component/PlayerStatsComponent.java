package com.sb.stats.component;

// ✅ Ensure API Imports exist
import com.sb.api.stats.IPlayerStats; 
import com.sb.api.stats.SBStat; 
import com.sb.stats.SkyBlockStatsMod;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Map;

// Interface implementation check karo
public class PlayerStatsComponent implements IPlayerStats, AutoSyncedComponent {
    private final PlayerEntity player;
    private final Map<SBStat, Double> stats = new HashMap<>();
    private double currentMana = 100;

    public PlayerStatsComponent(PlayerEntity player) {
        this.player = player;
        // Default Stats
        stats.put(SBStat.HEALTH, 100.0);
        stats.put(SBStat.DEFENSE, 0.0);
        stats.put(SBStat.STRENGTH, 0.0);
        stats.put(SBStat.CRIT_CHANCE, 30.0);
        stats.put(SBStat.CRIT_DAMAGE, 50.0);
        stats.put(SBStat.INTELLIGENCE, 100.0);
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
        SkyBlockStatsMod.PLAYER_STATS.sync(player);
    }

    @Override
    public void serverTick() {
        if (player.age % 20 == 0) regenMana();
    }

    @Override
    public void regenMana() {
        double maxMana = getBaseStat(SBStat.INTELLIGENCE);
        double regenAmount = maxMana * 0.02;
        if (currentMana < maxMana) {
            currentMana = Math.min(maxMana, currentMana + regenAmount);
            SkyBlockStatsMod.PLAYER_STATS.sync(player);
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (tag.contains("SBStats")) {
            NbtCompound statsTag = tag.getCompound("SBStats");
            for (SBStat stat : SBStat.values()) {
                if (statsTag.contains(stat.name())) {
                    stats.put(stat, statsTag.getDouble(stat.name()));
                }
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
