package com.sb.items.events;

import com.sb.api.item.SBRarity;
import com.sb.api.utils.SBConstants;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class TooltipHandler {
    
    public static void register() {
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            // Check agar yeh SkyBlock item hai
            NbtCompound nbt = stack.getSubNbt(SBConstants.NBT_ROOT);
            if (nbt == null) return;

            // 1. Clear Vanilla Tooltips (Attack Damage +7 etc.)
            // Hum pehli line (Name) ko chod kar sab uda denge
            if (lines.size() > 1) {
                lines.subList(1, lines.size()).clear();
            }

            // 2. Add Stats
            int dmg = nbt.getInt("DAMAGE");
            int str = nbt.getInt("STRENGTH");
            int intel = nbt.getInt("INTELLIGENCE");

            if (dmg > 0) lines.add(Text.literal("Damage: " + Formatting.RED + "+" + dmg));
            if (str > 0) lines.add(Text.literal("Strength: " + Formatting.RED + "+" + str));
            if (intel > 0) lines.add(Text.literal("Intelligence: " + Formatting.GREEN + "+" + intel));

            lines.add(Text.literal("")); // Empty Line

            // 3. Rarity Line (e.g. "LEGENDARY SWORD")
            String rarityStr = nbt.getString("RARITY");
            SBRarity rarity = SBRarity.valueOf(rarityStr);
            
            // Check if it's sword, bow, etc. (Simple check for now)
            String type = "ITEM";
            if (stack.getItem().toString().contains("sword")) type = "SWORD";
            
            lines.add(Text.literal("").append(
                Text.literal(rarity.display + " " + type)
                    .formatted(rarity.color, Formatting.BOLD)
            ));
        });
    }
}
