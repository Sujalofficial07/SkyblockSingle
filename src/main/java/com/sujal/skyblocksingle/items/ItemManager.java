package com.sujal.skyblocksingle.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    public static ItemStack createSkyblockItem(Item baseItem, String name, Rarity rarity, String... loreLines) {
        ItemStack stack = new ItemStack(baseItem);
        NbtCompound nbt = stack.getOrCreateNbt();

        // Mark as SkyBlock item
        nbt.putBoolean("SkyblockItem", true);
        nbt.putString("Rarity", rarity.name());

        // Set Custom Name with Rarity Color
        stack.setCustomName(Text.literal(name).formatted(rarity.getColor()));

        // Build Lore
        List<Text> lore = new ArrayList<>();
        
        // Add stats placeholder (Phase 4 me actual stats calculation aayega)
        // lore.add(Text.literal("§7Damage: §c+0")); 
        
        for (String line : loreLines) {
            lore.add(Text.literal("§7" + line));
        }
        
        lore.add(Text.empty());
        lore.add(Text.literal("§l" + rarity.name()).formatted(rarity.getColor(), Formatting.BOLD));

        // Set Lore to Item
        stack.getOrCreateSubNbt("display").put("Lore", toNbtList(lore));

        return stack;
    }

    // Helper to convert Text list to NBT list
    private static net.minecraft.nbt.NbtList toNbtList(List<Text> texts) {
        net.minecraft.nbt.NbtList list = new net.minecraft.nbt.NbtList();
        for (Text text : texts) {
            list.add(net.minecraft.nbt.NbtString.of(Text.Serializer.toJson(text)));
        }
        return list;
    }
}
