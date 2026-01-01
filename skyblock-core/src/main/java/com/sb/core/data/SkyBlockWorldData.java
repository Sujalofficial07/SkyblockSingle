// Path: skyblock-core/src/main/java/com/sb/core/data/SkyBlockWorldData.java
public class SkyBlockWorldData extends PersistentState {
    // Auction house items list, Bazaar prices etc.
    
    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        // Save logic
        return nbt;
    }
    
    public static SkyBlockWorldData getServerState(MinecraftServer server) {
        // Load logic from Overworld persistent state manager
        PersistentStateManager persistentStateManager = server.getOverworld().getPersistentStateManager();
        return persistentStateManager.getOrCreate(SkyBlockWorldData::new, SkyBlockWorldData::new, "skyblock_global_data");
    }
}
