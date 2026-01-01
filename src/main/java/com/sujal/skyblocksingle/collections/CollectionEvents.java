package com.sujal.skyblocksingle.collections;

import com.sujal.skyblocksingle.data.SkyblockData;
import net.fabricmc.fabric.api.event.player.EntityPickUpItemCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public class CollectionEvents {

    public static void register() {
        EntityPickUpItemCallback.EVENT.register((player, entity) -> {
            if (!(player instanceof ServerPlayerEntity)) return ActionResult.PASS;
            
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            ItemStack stack = entity.getStack();
            Item item = stack.getItem();
            String itemId = Registries.ITEM.getId(item).toString();
            int amount = stack.getCount();

            SkyblockData data = SkyblockData.getServerState(serverPlayer.getServerWorld());
            int oldAmount = data.getCollection(serverPlayer, itemId);
            data.addCollection(serverPlayer, itemId, amount);
            int newAmount = data.getCollection(serverPlayer, itemId);

            // Simple notification for testing (spammy, but confirms it works)
            // In real game, you only notify on milestones.
            // serverPlayer.sendMessage(Text.of("§eCollection: " + itemId + " + " + amount + " (§6" + newAmount + "§e)"), true);

            // Check Milestone Example (e.g., 50 Cobblestone)
            if (oldAmount < 50 && newAmount >= 50 && itemId.contains("cobblestone")) {
                serverPlayer.sendMessage(Text.of("§6§lCOLLECTION LEVEL UP §eCobblestone I"), false);
                serverPlayer.sendMessage(Text.of("§7Rewards: §fMining XP"), false);
            }

            return ActionResult.PASS;
        });
    }
}
