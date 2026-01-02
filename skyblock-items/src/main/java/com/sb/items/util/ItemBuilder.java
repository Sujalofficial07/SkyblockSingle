package com.sb.items.util;

import com.sb.api.item.SBRarity;
import com.sb.api.utils.SBConstants;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class ItemBuilder {

    public static ItemStack build(Item baseItem, String name, SBRarity rarity, int damage, int strength, int intel) {
        ItemStack stack = new ItemStack(baseItem);
        
        // 1. NBT Data Setup
        NbtCompound nbt = stack.getOrCreateSubNbt(SBConstants.NBT_ROOT); // "SkyBlockData"
        nbt.putString("ID", name.toUpperCase().replace(" ", "_"));
        nbt.putInt("DAMAGE", damage);
        nbt.putInt("STRENGTH", strength);
        nbt.putInt("INTELLIGENCE", intel);
        nbt.putString("RARITY", rarity.name());

        // 2. Display Name (Rarity Color ke saath)
        stack.setCustomName(net.minecraft.text.Text.literal(name).formatted(rarity.color));
        
        return stack;
    }
}
