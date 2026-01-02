package com.sb.stats;

import com.sb.stats.component.PlayerStatsComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

public class SkyBlockStatsComponents implements EntityComponentInitializer {

    // ✅ Key ko yahan move kar diya (Static initialization issue fix)
    public static final ComponentKey<PlayerStatsComponent> PLAYER_STATS =
            ComponentRegistry.getOrCreate(
                    new Identifier("skyblock-stats", "player_stats"),
                    PlayerStatsComponent.class
            );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // Factory Registration
        registry.registerForPlayers(
                PLAYER_STATS,
                PlayerStatsComponent::new,
                RespawnCopyStrategy.ALWAYS_COPY
        );
    }
}
