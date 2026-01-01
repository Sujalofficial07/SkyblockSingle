package com.sujal.skyblocksingle.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.sujal.skyblocksingle.data.SkyblockData;
import com.sujal.skyblocksingle.skills.SkillManager;
import com.sujal.skyblocksingle.skills.SkillType;
import com.sujal.skyblocksingle.world.island.IslandManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
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
        // ... (Existing /skyblock and /island commands remain same, see Phase 1) ...
        // Re-adding them here for completeness in this file context, 
        // but focusing on the NEW /skills command.
        
        dispatcher.register(literal("skyblock")
                .executes(ctx -> {
                    ctx.getSource().sendMessage(Text.of("§bSkyblockSingle v1.0.0 by Sujal"));
                    return 1;
                })
        );

        dispatcher.register(literal("island")
                .then(literal("reset")
                        .executes(ctx -> {
                            IslandManager.resetIsland(ctx.getSource().getPlayer());
                            return 1;
                        }))
        );

        // --- COINS (Updated) ---
        dispatcher.register(literal("coins")
                .executes(ctx -> {
                    SkyblockData data = SkyblockData.getServerState(ctx.getSource().getWorld());
                    double coins = data.getCoins(ctx.getSource().getPlayer());
                    ctx.getSource().sendMessage(Text.of("§6Coins: " + String.format("%.1f", coins)));
                    return 1;
                })
                .then(literal("add")
                        .then(argument("amount", DoubleArgumentType.doubleArg(0))
                                .executes(ctx -> {
                                    double amount = DoubleArgumentType.getDouble(ctx, "amount");
                                    SkyblockData data = SkyblockData.getServerState(ctx.getSource().getWorld());
                                    data.addCoins(ctx.getSource().getPlayer(), amount);
                                    ctx.getSource().sendMessage(Text.of("§aAdded " + amount + " coins."));
                                    return 1;
                                })
                        )
                )
        );

        // --- NEW: SKILLS COMMAND ---
        dispatcher.register(literal("skills")
                .executes(ctx -> {
                    ServerPlayerEntity player = ctx.getSource().getPlayer();
                    SkyblockData data = SkyblockData.getServerState(player.getServerWorld());
                    
                    player.sendMessage(Text.of("§8§m------------------------"), false);
                    player.sendMessage(Text.of("§b§lYOUR SKILLS"), false);
                    
                    for(SkillType type : SkillType.values()) {
                        double xp = data.getSkillXp(player, type);
                        int level = SkillManager.getLevel(xp);
                        player.sendMessage(Text.of(type.getColor() + type.getDisplayName() + ": §fLvl " + level + " §7(" + String.format("%.1f", xp) + " XP)"), false);
                    }
                    
                    player.sendMessage(Text.of("§8§m------------------------"), false);
                    return 1;
                })
        );
    }
}
