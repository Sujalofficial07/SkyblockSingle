package com.sujal.skyblocksingle.skills;

public enum SkillType {
    FARMING("Farming", "§a"),
    MINING("Mining", "§7"),
    COMBAT("Combat", "§c");

    private final String displayName;
    private final String color;

    SkillType(String displayName, String color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColor() {
        return color;
    }
}
