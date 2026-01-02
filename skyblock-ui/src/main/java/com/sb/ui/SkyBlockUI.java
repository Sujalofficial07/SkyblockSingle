package com.sb.ui;

import com.sb.ui.hud.ActionBarHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class SkyBlockUI implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        // Server side logic (none for UI)
    }

    @Override
    public void onInitializeClient() {
        // Render Event Register
        HudRenderCallback.EVENT.register(new ActionBarHud());
    }
}
