package com.sb.ui.hud;

import com.sb.api.stats.IPlayerStats;
import com.sb.api.stats.SBStat;
import com.sb.stats.SkyBlockStatsMod; // Access key
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ActionBarHud implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        if (player == null || client.options.hudHidden) return;

        // 1. Get Stats Data
        // Note: Client side par data sync hona zaroori hai. 
        // Cardinal Components auto-sync karta hai agar configured ho.
        IPlayerStats stats = SkyBlockStatsMod.PLAYER_STATS.get(player);

        double currentHealth = player.getHealth() * 5; // Vanilla 20HP -> 100HP scale
        double maxHealth = stats.getBaseStat(SBStat.HEALTH); // Base stat from our mod
        if (maxHealth < 100) maxHealth = 100; // Fallback

        double defense = stats.getBaseStat(SBStat.DEFENSE);
        
        double currentMana = stats.getCurrentMana();
        double maxMana = stats.getBaseStat(SBStat.INTELLIGENCE);

        // 2. Format The Text
        // Format: ❤ 100/100     ❈ 100/100 Mana
        
        String healthText = String.format("❤ %.0f/%.0f", currentHealth, maxHealth);
        String defenseText = defense > 0 ? String.format("  ❈ %.0f Def", defense) : "";
        String manaText = String.format("       ✎ %.0f/%.0f Mana", currentMana, maxMana);

        Text fullText = Text.literal("")
                .append(Text.literal(healthText).formatted(Formatting.RED))
                .append(Text.literal(defenseText).formatted(Formatting.GREEN))
                .append(Text.literal(manaText).formatted(Formatting.AQUA));

        // 3. Render on Action Bar (Bottom of screen)
        // Y - coordinate screen ke bottom se thoda upar
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        
        // Center Text
        int x = screenWidth / 2 - (client.textRenderer.getWidth(fullText) / 2);
        int y = screenHeight - 60; // Hotbar ke upar

        context.drawText(client.textRenderer, fullText, x, y, 0xFFFFFF, true);
    }
}
