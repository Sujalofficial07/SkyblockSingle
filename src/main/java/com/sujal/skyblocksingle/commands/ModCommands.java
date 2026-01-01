package com.sujal.skyblocksingle.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.sujal.skyblocksingle.data.SkyblockData;
import com.sujal.skyblocksingle.world.island.IslandManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
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
    }
}
