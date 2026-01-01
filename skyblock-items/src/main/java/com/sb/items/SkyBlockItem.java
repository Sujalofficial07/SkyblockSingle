// Path: skyblock-items/src/main/java/com/sb/items/SkyBlockItem.java
package com.sb.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import java.util.List;

public class SkyBlockItem extends Item {
    public SkyBlockItem(Settings settings) {
        super(settings);
    }

    // Item creation helper
    public static ItemStack createSword(String id, int damage, int str) {
        ItemStack stack = new ItemStack(SkyBlockItems.HYPERION); // example
        NbtCompound nbt = stack.getOrCreateNbt();
        
        // Custom NBT Data (Hypixel style)
        NbtCompound sbData = new NbtCompound();
        sbData.putString("ID", id);
        sbData.putInt("DAMAGE", damage);
        sbData.putInt("STRENGTH", str);
        sbData.putString("RARITY", "LEGENDARY");
        
        nbt.put("SkyBlockData", sbData);
        return stack;
    }
}
