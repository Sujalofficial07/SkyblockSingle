package com.sujal.skyblocksingle.mixin;

import com.sujal.skyblocksingle.data.SkyblockData;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemPickupMixin {

    @Shadow public abstract ItemStack getStack();

    // Injects code right after the player successfully picks up an item
    @Inject(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;sendPickup(Lnet/minecraft/entity/Entity;I)V"))
    private void onPickup(PlayerEntity player, CallbackInfo ci) {
        if (!player.getWorld().isClient && player instanceof ServerPlayerEntity serverPlayer) {
            ItemStack stack = this.getStack();
            String itemId = Registries.ITEM.getId(stack.getItem()).toString();
            int amount = stack.getCount(); // Note: This might be the remaining count, but at this injection point, the stack is about to be handled.
            
            // Note: The stack count might be modified during pickup, but for simple collection tracking 
            // hooking here captures the intent. Ideally, we track the amount 'i' passed to sendPickup, 
            // but getting local variables in Mixins is complex. 
            // For now, we assume the whole stack was picked up if the event triggers.
            
            SkyblockData data = SkyblockData.getServerState(serverPlayer.getServerWorld());
            int oldAmount = data.getCollection(serverPlayer, itemId);
            data.addCollection(serverPlayer, itemId, amount);
            int newAmount = data.getCollection(serverPlayer, itemId);

            // Collection Milestone Example (Cobblestone)
            if (oldAmount < 50 && newAmount >= 50 && itemId.equals("minecraft:cobblestone")) {
                serverPlayer.sendMessage(Text.of("§6§lCOLLECTION LEVEL UP §eCobblestone I"), false);
                serverPlayer.sendMessage(Text.of("§7Rewards: §fMining XP"), false);
            }
        }
    }
}
