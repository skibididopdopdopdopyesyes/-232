package com.swill.killaura;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KillauraMod implements ClientModInitializer {
    public static KillauraCore KILLAURA;
    public static KeyBinding TOGGLE_KEY;

    @Override
    public void onInitializeClient() {
        TOGGLE_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.killaura.toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "category.killaura"
        ));
        
        KILLAURA = new KillauraCore();
        
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (TOGGLE_KEY.wasPressed()) {
                KILLAURA.toggle();
            }
            if (KILLAURA.isEnabled() && client.player != null && client.world != null) {
                KILLAURA.update();
            }
        });
    }
}
