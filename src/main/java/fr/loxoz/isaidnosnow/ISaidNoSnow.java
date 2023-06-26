package fr.loxoz.isaidnosnow;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

@Environment(EnvType.CLIENT)
public class ISaidNoSnow implements ClientModInitializer {
    public static final String MOD_ID = "isaidnosnow";
    public static ISaidNoSnow INSTANCE = null;
    public KeyBinding keyToggle;
    private ISNWConfig config;

    public ISaidNoSnow() { INSTANCE = this; }

    @Override
    public void onInitializeClient() {
        config = new ISNWConfig(FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".properties"));
        keyToggle = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + MOD_ID + ".toggle", InputUtil.Type.KEYSYM, -1, "key.categories." + MOD_ID));

        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> config.save());
        config.load();
    }

    private void onTick(MinecraftClient client) {
        while (keyToggle.wasPressed()) {
            setEnabled(!isEnabled());
        }
    }

    public boolean isEnabled() { return config.enabled; }
    public void setEnabled(boolean enabled) {
        if (config.enabled == enabled) return;
        var client = MinecraftClient.getInstance();
        client.worldRenderer.reload();
        config.enabled = enabled;
        client.inGameHud.setOverlayMessage(Text.translatable("text.isaidnosnow.snow_" + (isEnabled() ? "hide" : "show")), false);
        config.saveAsync();
    }
}
