package com.sb.core.data;

// ✅ Yeh imports zaroori hain
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

public class SkyBlockWorldData extends PersistentState {

    // Example: Auction House ya Bazaar ka global data yahan store hoga
    // public int totalCoinsTraded = 0;

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        // Data save karne ka logic
        // nbt.putInt("totalCoinsTraded", totalCoinsTraded);
        return nbt;
    }

    // Data Load karne ka logic
    public static SkyBlockWorldData createFromNbt(NbtCompound tag) {
        SkyBlockWorldData state = new SkyBlockWorldData();
        // state.totalCoinsTraded = tag.getInt("totalCoinsTraded");
        return state;
    }
    
    public static SkyBlockWorldData getServerState(MinecraftServer server) {
        // Overworld ka state manager lo (Global data overworld mein save hota hai)
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
        
        // File load karo ya nayi banao agar nahi hai
        return persistentStateManager.getOrCreate(
                SkyBlockWorldData::createFromNbt,
                SkyBlockWorldData::new,
                "skyblock_global_data"
        );
    }
}
