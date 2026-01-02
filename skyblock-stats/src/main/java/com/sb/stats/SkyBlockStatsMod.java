package com.sb.stats;

import com.sb.api.stats.IPlayerStats;
import com.sb.stats.component.PlayerStatsComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class SkyBlockStatsMod implements ModInitializer, EntityComponentInitializer {
    
    // Component Key (Is key se hum kahin bhi data access kar sakte hain)
    public static final ComponentKey<IPlayerStats> PLAYER_STATS = 
            ComponentRegistry.getOrCreate(new Identifier("skyblock-stats", "player_stats"), IPlayerStats.class);

    @Override
    public void onInitialize() {
        // General Init
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // Player ke saath stats attach karo
        registry.registerForPlayers(PLAYER_STATS, PlayerStatsComponent::new);
    }
}
