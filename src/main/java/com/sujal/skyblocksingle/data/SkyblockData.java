package com.sujal.skyblocksingle.data;

import com.sujal.skyblocksingle.skills.SkillType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkyblockData extends PersistentState {
    private static final String DATA_ID = "skyblock_single_data";

    private final HashMap<UUID, Double> playerCoins = new HashMap<>();
    private final HashMap<UUID, Boolean> hasIsland = new HashMap<>();
    
    // Stores total XP for each skill per player
    private final HashMap<UUID, HashMap<SkillType, Double>> playerSkills = new HashMap<>();

    public static SkyblockData getServerState(ServerWorld world) {
        ServerWorld serverWorld = world.getServer().getWorld(World.OVERWORLD);
        return serverWorld.getPersistentStateManager().getOrCreate(
                SkyblockData::createFromNbt,
                SkyblockData::new,
                DATA_ID
        );
    }

    public static SkyblockData createFromNbt(NbtCompound tag) {
        SkyblockData state = new SkyblockData();

        // Load Coins
        if (tag.contains("coins")) {
            NbtCompound coinsTag = tag.getCompound("coins");
            coinsTag.getKeys().forEach(key -> 
                state.playerCoins.put(UUID.fromString(key), coinsTag.getDouble(key)));
        }

        // Load Islands
        if (tag.contains("islands")) {
            NbtCompound islandTag = tag.getCompound("islands");
            islandTag.getKeys().forEach(key -> 
                state.hasIsland.put(UUID.fromString(key), islandTag.getBoolean(key)));
        }

        // Load Skills
        if (tag.contains("skills")) {
            NbtCompound skillsTag = tag.getCompound("skills");
            skillsTag.getKeys().forEach(uuidKey -> {
                UUID uuid = UUID.fromString(uuidKey);
                NbtCompound playerSkillTag = skillsTag.getCompound(uuidKey);
                
                HashMap<SkillType, Double> skillMap = new HashMap<>();
                for (SkillType type : SkillType.values()) {
                    if (playerSkillTag.contains(type.name())) {
                        skillMap.put(type, playerSkillTag.getDouble(type.name()));
                    }
                }
                state.playerSkills.put(uuid, skillMap);
            });
        }

        return state;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        NbtCompound coinsTag = new NbtCompound();
        playerCoins.forEach((uuid, amount) -> coinsTag.putDouble(uuid.toString(), amount));
        tag.put("coins", coinsTag);

        NbtCompound islandTag = new NbtCompound();
        hasIsland.forEach((uuid, has) -> islandTag.putBoolean(uuid.toString(), has));
        tag.put("islands", islandTag);

        NbtCompound skillsTag = new NbtCompound();
        playerSkills.forEach((uuid, map) -> {
            NbtCompound playerSkillTag = new NbtCompound();
            map.forEach((type, xp) -> playerSkillTag.putDouble(type.name(), xp));
            skillsTag.put(uuid.toString(), playerSkillTag);
        });
        tag.put("skills", skillsTag);

        return tag;
    }

    // --- Coin Methods ---
    public double getCoins(ServerPlayerEntity player) {
        return playerCoins.getOrDefault(player.getUuid(), 0.0);
    }

    public void addCoins(ServerPlayerEntity player, double amount) {
        UUID uuid = player.getUuid();
        playerCoins.put(uuid, getCoins(player) + amount);
        markDirty();
    }
    
    public void setCoins(ServerPlayerEntity player, double amount) {
        playerCoins.put(player.getUuid(), amount);
        markDirty();
    }

    // --- Island Methods ---
    public boolean hasIsland(ServerPlayerEntity player) {
        return hasIsland.getOrDefault(player.getUuid(), false);
    }

    public void setHasIsland(ServerPlayerEntity player, boolean value) {
        hasIsland.put(player.getUuid(), value);
        markDirty();
    }

    // --- Skill Methods ---
    public double getSkillXp(ServerPlayerEntity player, SkillType type) {
        return playerSkills.computeIfAbsent(player.getUuid(), k -> new HashMap<>())
                           .getOrDefault(type, 0.0);
    }

    public void addSkillXp(ServerPlayerEntity player, SkillType type, double amount) {
        double current = getSkillXp(player, type);
        playerSkills.computeIfAbsent(player.getUuid(), k -> new HashMap<>())
                    .put(type, current + amount);
        markDirty();
    }
}
