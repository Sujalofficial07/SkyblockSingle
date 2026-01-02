package com.sb.api.stats;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;

// ServerTickingComponent add kiya taaki Mana regen ho sake
public interface IPlayerStats extends ComponentV3, ServerTickingComponent {
    double getBaseStat(SBStat stat);
    void setBaseStat(SBStat stat, double value);
    
    double getCurrentMana();
    void consumeMana(double amount);
    void regenMana();
}
