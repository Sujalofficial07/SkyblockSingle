package com.sb.items.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.sb.api.item.SBRarity;
import com.sb.items.util.ItemBuilder; 
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TestItemCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("sbitem")
            .executes(context -> {
                ServerPlayerEntity player = context.getSource().getPlayer();
                
                // Create "Hyperion" (Fake Version)
                var sword = ItemBuilder.build(
                    Items.IRON_SWORD, 
                    "Hyperion", 
                    SBRarity.LEGENDARY, 
                    200, // Damage
                    150, // Strength
                    300  // Intel
                );
                
                player.getInventory().insertStack(sword);
                context.getSource().sendFeedback(() -> Text.of("§aGiven Hyperion!"), false);
                return 1;
            })
        );
    }
}
