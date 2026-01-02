package com.sb.stats;

import com.sb.api.stats.IPlayerStats;
import com.sb.stats.component.PlayerStatsComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy; // ✅ IMPORT THIS
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class SkyBlockStatsMod implements ModInitializer, EntityComponentInitializer {
    
    public static final ComponentKey<IPlayerStats> PLAYER_STATS = 
            ComponentRegistry.getOrCreate(new Identifier("skyblock-stats", "player_stats"), IPlayerStats.class);

    @Override
    public void onInitialize() {
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // ✅ FIX: Added 'RespawnCopyStrategy.ALWAYS_COPY'
        // Iska matlab marne par stats gayab nahi honge.
        registry.registerForPlayers(PLAYER_STATS, PlayerStatsComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
