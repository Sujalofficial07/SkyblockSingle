package com.sb.stats;

import com.sb.api.skills.ISkillData; // Import Interface
import com.sb.stats.component.PlayerStatsComponent;
import com.sb.stats.component.SkillComponent; // Import Implementation
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

public class SkyBlockStatsComponents implements EntityComponentInitializer {

    // Existing Stats
    public static final ComponentKey<PlayerStatsComponent> PLAYER_STATS =
            ComponentRegistry.getOrCreate(new Identifier("skyblock-stats", "player_stats"), PlayerStatsComponent.class);

    // ✅ NEW: Skills Component Key
    public static final ComponentKey<ISkillData> SKILLS =
            ComponentRegistry.getOrCreate(new Identifier("skyblock-stats", "skills"), ISkillData.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // Register Stats
        registry.registerForPlayers(PLAYER_STATS, PlayerStatsComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        
        // ✅ Register Skills
        registry.registerForPlayers(SKILLS, SkillComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
