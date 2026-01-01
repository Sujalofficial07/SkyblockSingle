package com.sujal.skyblocksingle.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.sujal.skyblocksingle.data.SkyblockData;
import com.sujal.skyblocksingle.items.ItemManager;
import com.sujal.skyblocksingle.items.Rarity;
import com.sujal.skyblocksingle.skills.SkillManager;
import com.sujal.skyblocksingle.skills.SkillType;
import com.sujal.skyblocksingle.world.island.IslandManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.item.Items;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

public class ModCommands {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerSkyblockCommand(dispatcher);
        });
    }

    private static void registerSkyblockCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        
        // --- BASE COMMANDS ---
        dispatcher.register(literal("skyblock")
                .executes(ctx -> {
                    ctx.getSource().sendMessage(Text.of("§bSkyblockSingle v1.0.0"));
                    return 1;
                })
        );

        dispatcher.register(literal("island").then(literal("reset").executes(ctx -> {
            IslandManager.resetIsland(ctx.getSource().getPlayer());
            return 1;
        })));

        // --- COINS ---
        dispatcher.register(literal("coins")
                .executes(ctx -> {
                    double coins = SkyblockData.getServerState(ctx.getSource().getWorld()).getCoins(ctx.getSource().getPlayer());
                    ctx.getSource().sendMessage(Text.of("§6Coins: " + String.format("%.1f", coins)));
                    return 1;
                })
                .then(literal("add").then(argument("amount", DoubleArgumentType.doubleArg(0)).executes(ctx -> {
                    SkyblockData.getServerState(ctx.getSource().getWorld()).addCoins(ctx.getSource().getPlayer(), DoubleArgumentType.getDouble(ctx, "amount"));
                    ctx.getSource().sendMessage(Text.of("§aCoins added."));
                    return 1;
                })))
        );

        // --- SKILLS ---
        dispatcher.register(literal("skills").executes(ctx -> {
            ServerPlayerEntity player = ctx.getSource().getPlayer();
            SkyblockData data = SkyblockData.getServerState(player.getServerWorld());
            player.sendMessage(Text.of("§8§m------------------------"), false);
            for(SkillType type : SkillType.values()) {
                double xp = data.getSkillXp(player, type);
                player.sendMessage(Text.of(type.getColor() + type.getDisplayName() + ": §fLvl " + SkillManager.getLevel(xp)));
            }
            player.sendMessage(Text.of("§8§m------------------------"), false);
            return 1;
        }));

        // --- NEW: COLLECTIONS COMMAND ---
        dispatcher.register(literal("collections").executes(ctx -> {
            ServerPlayerEntity player = ctx.getSource().getPlayer();
            SkyblockData data = SkyblockData.getServerState(player.getServerWorld());
            
            player.sendMessage(Text.of("§6§lYOUR COLLECTIONS"), false);
            // Just showing a few key ones for now
            String[] keyItems = {"minecraft:cobblestone", "minecraft:oak_log", "minecraft:coal", "minecraft:iron_ingot", "minecraft:diamond"};
            
            for (String item : keyItems) {
                int count = data.getCollection(player, item);
                if (count > 0) {
                    player.sendMessage(Text.of("§e" + item.replace("minecraft:", "") + ": §a" + count), false);
                }
            }
            return 1;
        }));

        // --- NEW: DEBUG GIVE ITEM ---
        dispatcher.register(literal("sb_give_test")
                .executes(ctx -> {
                    ServerPlayerEntity player = ctx.getSource().getPlayer();
                    // Give a custom SkyBlock Item
                    player.getInventory().insertStack(
                            ItemManager.createSkyblockItem(Items.DIAMOND_SWORD, "Rogue Sword", Rarity.COMMON, "Click to speed up!", "Mana Cost: 50")
                    );
                    player.sendMessage(Text.of("§aGave Custom Item!"), false);
                    return 1;
                })
        );
    }
}
