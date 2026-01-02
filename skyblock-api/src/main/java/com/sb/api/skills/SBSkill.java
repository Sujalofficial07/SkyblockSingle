package com.sb.api.skills;

public enum SBSkill {
    FARMING("Farming", "§a"), // Green
    MINING("Mining", "§7"),   // Gray
    COMBAT("Combat", "§c"),   // Red
    FORAGING("Foraging", "§6"), // Gold
    FISHING("Fishing", "§b"), // Aqua
    ENCHANTING("Enchanting", "§d"), // Light Purple
    ALCHEMY("Alchemy", "§5"); // Dark Purple

    public final String name;
    public final String color;

    SBSkill(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
