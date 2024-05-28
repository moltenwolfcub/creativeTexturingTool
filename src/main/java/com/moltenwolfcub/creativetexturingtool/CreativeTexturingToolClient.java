package com.moltenwolfcub.creativetexturingtool;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.lwjgl.glfw.GLFW;

public class CreativeTexturingToolClient implements ClientModInitializer {
    private static final KeyMapping toggle;

    static {
        toggle = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key."+CreativeTexturingTool.MODID+ ".toggle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_APOSTROPHE,
                "category."+CreativeTexturingTool.MODID
        ));
    }

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggle.consumeClick()) {
                if (client.hasSingleplayerServer()){
                    MinecraftServer server = client.getSingleplayerServer();
                    ServerPlayer player = server.getPlayerList().getPlayer(client.player.getUUID());
                    toggleActive(player);

                } else {
                    client.player.sendSystemMessage(Component.literal("Can't use keybinds in server setting."));
                }
            }
        });
    }

    private static void toggleActive(ServerPlayer player) {
        DataSaver dataSaver = (DataSaver) player;
        boolean current = dataSaver.getPersistantData().getBoolean("active");

        dataSaver.getPersistantData().putBoolean("active", !current);
        player.sendSystemMessage(Component.literal("Toggled paintbrush active to "+!current));
    }
}
