// Path: skyblock-admin/src/main/java/com/sb/admin/commands/StatCommand.java
public class StatCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("sb")
            .requires(source -> source.hasPermissionLevel(2)) // Admin check
            .then(CommandManager.literal("setstrength")
                .then(CommandManager.argument("amount", DoubleArgumentType.doubleArg())
                    .executes(context -> {
                        double amount = DoubleArgumentType.getDouble(context, "amount");
                        ServerPlayerEntity player = context.getSource().getPlayer();
                        
                        // API call to Stats Mod
                        IPlayerStats stats = player.getComponent(StatsComponents.PLAYER_STATS);
                        stats.setStrength(amount);
                        
                        context.getSource().sendFeedback(() -> Text.of("Strength set to " + amount), false);
                        return 1;
                    })
                )
            )
        );
    }
}
