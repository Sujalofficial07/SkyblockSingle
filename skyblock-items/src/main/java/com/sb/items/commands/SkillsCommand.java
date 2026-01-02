package com.sb.items.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.sb.api.skills.ISkillData;
import com.sb.api.skills.SBSkill;
import com.sb.stats.SkyBlockStatsComponents; // Stats mod dependency needed in Items build.gradle
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SkillsCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("skills")
            .executes(context -> {
                ServerPlayerEntity player = context.getSource().getPlayer();
                openSkillsMenu(player);
                return 1;
            }));
    }

    private static void openSkillsMenu(ServerPlayerEntity player) {
        // 1. Create a Chest Inventory (3 Rows = 27 Slots)
        SimpleInventory inventory = new SimpleInventory(27);

        // 2. Get Player Data
        ISkillData data = SkyBlockStatsComponents.SKILLS.get(player);

        // 3. Populate Items
        int slot = 10; // Start from middle row
        for (SBSkill skill : SBSkill.values()) {
            if (slot > 16) break; // Safety break

            ItemStack icon = new ItemStack(skill.icon);
            int level = data.getLevel(skill);
            double currentXp = data.getXp(skill);
            double nextXp = data.getXpForNextLevel(level);
            
            // Format Lore (Description)
            var lore = icon.getOrCreateSubNbt("display");
            net.minecraft.nbt.NbtList loreList = new net.minecraft.nbt.NbtList();
            
            loreList.add(net.minecraft.nbt.NbtString.of("§7Level: " + level));
            loreList.add(net.minecraft.nbt.NbtString.of(""));
            loreList.add(net.minecraft.nbt.NbtString.of("§7Progress: §e" + String.format("%.0f", currentXp) + "§6/§e" + String.format("%.0f", nextXp)));
            loreList.add(net.minecraft.nbt.NbtString.of(getProgressBar(currentXp, nextXp)));
            loreList.add(net.minecraft.nbt.NbtString.of(""));
            loreList.add(net.minecraft.nbt.NbtString.of("§eClick to view rewards!"));
            
            lore.put("Lore", loreList);
            icon.setCustomName(Text.of(skill.color + "§l" + skill.name + " " + level));
            
            inventory.setStack(slot, icon);
            slot++;
        }

        // 4. Fill Empty Spots with Glass (Optional, Hypixel style)
        ItemStack glass = new ItemStack(net.minecraft.item.Items.GRAY_STAINED_GLASS_PANE);
        glass.setCustomName(Text.of(" "));
        for(int i=0; i<27; i++) {
            if(inventory.getStack(i).isEmpty()) inventory.setStack(i, glass);
        }

        // 5. Open GUI
        player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
            (syncId, playerInv, p) -> GenericContainerScreenHandler.createGeneric9x3(syncId, playerInv, inventory),
            Text.of("Your Skills")
        ));
    }

    // Progress Bar Helper: [#####-----]
    private static String getProgressBar(double current, double max) {
        int totalBars = 20;
        int filledBars = (int) ((current / max) * totalBars);
        StringBuilder bar = new StringBuilder("§f[");
        for (int i = 0; i < totalBars; i++) {
            if (i < filledBars) bar.append("§a|"); // Green
            else bar.append("§8|"); // Gray
        }
        bar.append("§f]");
        return bar.toString();
    }
}
