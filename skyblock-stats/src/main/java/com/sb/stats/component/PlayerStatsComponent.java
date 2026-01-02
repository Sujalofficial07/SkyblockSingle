// 1. Interface Change
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent; // Add Import

public class PlayerStatsComponent implements IPlayerStats, AutoSyncedComponent { 
    // ... baaki code same ...
    
    // 2. Add this logic in consumeMana/regenMana
    // Jab bhi mana change ho, sync karo
    @Override
    public void consumeMana(double amount) {
        this.currentMana = Math.max(0, currentMana - amount);
        SkyBlockStatsMod.PLAYER_STATS.sync(player); // 🔄 SYNC TO CLIENT
    }

    @Override
    public void regenMana() {
        // ... calculation ...
        if (changed) {
            SkyBlockStatsMod.PLAYER_STATS.sync(player); // 🔄 SYNC TO CLIENT
        }
    }
}
