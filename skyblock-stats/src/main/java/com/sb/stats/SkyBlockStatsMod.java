package com.sb.stats;

import net.fabricmc.api.ModInitializer;

public class SkyBlockStatsMod implements ModInitializer {
    // Ab yahan koi "PLAYER_STATS" ya "EntityComponentInitializer" nahi hai.
    // Ye file sirf mod ko start karegi.

    @Override
    public void onInitialize() {
        System.out.println("SkyBlock Stats Mod Initialized!");
    }
}
