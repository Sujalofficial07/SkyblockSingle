package com.sb.stats;

import com.sb.stats.component.PlayerStatsComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

// ✅ Ek hi class mein ModInitializer aur EntityComponentInitializer dono
public class SkyBlockStatsMod implements ModInitializer, EntityComponentInitializer {

    // 1. Component Key Register (Yehi ChatGPT ne bataya tha)
    public static final ComponentKey<PlayerStatsComponent> PLAYER_STATS =
            ComponentRegistry.getOrCreate(
                    new Identifier("skyblock-stats", "player_stats"),
                    PlayerStatsComponent.class
            );

    @Override
    public void onInitialize() {
        // Console log taaki pata chale mod load hua
        System.out.println("SkyBlock Stats Mod Initialized!");
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // 2. Player ke liye Stats attach karna
        registry.registerForPlayers(
                PLAYER_STATS,
                PlayerStatsComponent::new,
                RespawnCopyStrategy.ALWAYS_COPY // Marne par stats save rahenge
        );
    }
}
