package com.sb.api.skills;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public enum SBSkill {
    FARMING("Farming", "§a", Items.GOLDEN_HOE),
    MINING("Mining", "§7", Items.STONE_PICKAXE),
    COMBAT("Combat", "§c", Items.STONE_SWORD),
    FORAGING("Foraging", "§6", Items.JUNGLE_SAPLING),
    FISHING("Fishing", "§b", Items.FISHING_ROD),
    ENCHANTING("Enchanting", "§d", Items.ENCHANTING_TABLE),
    ALCHEMY("Alchemy", "§5", Items.BREWING_STAND);

    public final String name;
    public final String color;
    public final Item icon; // ✅ New Icon field

    SBSkill(String name, String color, Item icon) {
        this.name = name;
        this.color = color;
        this.icon = icon;
    }
}
