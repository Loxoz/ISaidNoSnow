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
import java.util.Properties;

@Environment(EnvType.CLIENT)
public class ISaidNoSnow implements ClientModInitializer {
    public static final String MOD_ID = "isaidnosnow";
    public static KeyBinding keyToggle;
    public static ISaidNoSnow INSTANCE = null;
    private boolean enabled = false;

    public ISaidNoSnow() {
        INSTANCE = this;
    }

    @Override
    public void onInitializeClient() {
        keyToggle = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + MOD_ID + ".toggle", InputUtil.Type.KEYSYM, -1, "key.categories." + MOD_ID));

        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
        ClientLifecycleEvents.CLIENT_STOPPING.register(this::saveConfig);
        loadConfig();
    }

    public File getConfigFile() {
        return new File(FabricLoader.getInstance().getConfigDir().toFile(), MOD_ID + ".properties");
    }

    public void loadConfig() {
        var file = getConfigFile();
        if (!file.exists() || !file.isFile()) return;
        try {
            var reader = new FileReader(file);
            var props = new Properties();
            props.load(reader);
            String res = props.getProperty("enabled");
            if (res == null) return;
            enabled = Boolean.parseBoolean(res);
        } catch (IOException ignored) {}
    }

    public void saveConfig(MinecraftClient client) {
        try {
            var reader = new FileWriter(getConfigFile());
            var props = new Properties();
            props.setProperty("enabled", String.valueOf(enabled));
            props.store(reader, null);
        } catch (IOException ignored) {}
    }

    public void onTick(MinecraftClient client) {
        while (keyToggle.wasPressed()) {
            setEnabled(!isEnabled());
        }
    }

    public static MinecraftClient getClient() { return MinecraftClient.getInstance(); }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            getClient().worldRenderer.reload();
            getClient().inGameHud.setOverlayMessage(Text.translatable("text.isaidnosnow.snow_" + (this.enabled ? "hide" : "show")), false);
        }
    }
}
