package com.sujal.skyblocksingle.items;

import net.minecraft.util.Formatting;

public enum Rarity {
    COMMON(Formatting.WHITE),
    UNCOMMON(Formatting.GREEN),
    RARE(Formatting.BLUE),
    EPIC(Formatting.DARK_PURPLE),
    LEGENDARY(Formatting.GOLD),
    MYTHIC(Formatting.LIGHT_PURPLE),
    SPECIAL(Formatting.RED);

    private final Formatting color;

    Rarity(Formatting color) {
        this.color = color;
    }

    public Formatting getColor() {
        return color;
    }
}
