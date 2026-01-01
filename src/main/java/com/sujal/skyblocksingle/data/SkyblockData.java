package com.sujal.skyblocksingle.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

public class SkyblockData extends PersistentState {
    private static final String DATA_ID = "skyblock_single_data";
    
    // Player Coins Map
    private final HashMap<UUID, Double> playerCoins = new HashMap<>();
    
    // Track if player has starter island
    private final HashMap<UUID, Boolean> hasIsland = new HashMap<>();

    public static SkyblockData getServerState(ServerWorld world) {
        ServerWorld serverWorld = world.getServer().getWorld(World.OVERWORLD);
        
        // FIX: Used PersistentState.Type explicitly to fix compilation error
        return serverWorld.getPersistentStateManager().getOrCreate(
                new PersistentState.Type<>(SkyblockData::new, SkyblockData::createFromNbt, null),
                DATA_ID
        );
    }

    // Creating from NBT
    public static SkyblockData createFromNbt(NbtCompound tag) {
        SkyblockData state = new SkyblockData();
        
        NbtCompound coinsTag = tag.getCompound("coins");
        coinsTag.getKeys().forEach(key -> {
            state.playerCoins.put(UUID.fromString(key), coinsTag.getDouble(key));
        });

        NbtCompound islandTag = tag.getCompound("islands");
        islandTag.getKeys().forEach(key -> {
            state.hasIsland.put(UUID.fromString(key), islandTag.getBoolean(key));
        });

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

        return tag;
    }

    // Logic Methods
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

    public boolean hasIsland(ServerPlayerEntity player) {
        return hasIsland.getOrDefault(player.getUuid(), false);
    }

    public void setHasIsland(ServerPlayerEntity player, boolean value) {
        hasIsland.put(player.getUuid(), value);
        markDirty();
    }
}
