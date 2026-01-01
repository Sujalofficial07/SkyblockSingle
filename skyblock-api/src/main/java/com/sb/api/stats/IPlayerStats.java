// Path: skyblock-api/src/main/java/com/sb/api/stats/IPlayerStats.java
package com.sb.api.stats;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;

public interface IPlayerStats extends ComponentV3 {
    double getStrength();
    void setStrength(double value);
    
    double getCritChance();
    double getCritDamage();
    
    double getIntelligence();
    void setIntelligence(double value);
    
    // Mana logic
    double getCurrentMana();
    void consumeMana(double amount);
}
