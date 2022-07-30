package fr.loxoz.isaidnosnow;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.MessageType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
            getClient().inGameHud.addChatMessage(MessageType.GAME_INFO, new TranslatableText("isaidnosnow.text.snow_" + (this.enabled ? "hide" : "show")), Util.NIL_UUID);
        }
    }
}
