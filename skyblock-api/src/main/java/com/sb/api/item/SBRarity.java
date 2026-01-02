package com.sb.api.item;

import net.minecraft.util.Formatting;

public enum SBRarity {
    COMMON(Formatting.WHITE, "COMMON"),
    UNCOMMON(Formatting.GREEN, "UNCOMMON"),
    RARE(Formatting.BLUE, "RARE"),
    EPIC(Formatting.DARK_PURPLE, "EPIC"),
    LEGENDARY(Formatting.GOLD, "LEGENDARY"),
    MYTHIC(Formatting.LIGHT_PURPLE, "MYTHIC"),
    SPECIAL(Formatting.RED, "SPECIAL");

    public final Formatting color;
    public final String display;

    SBRarity(Formatting color, String display) {
        this.color = color;
        this.display = display;
    }
}
