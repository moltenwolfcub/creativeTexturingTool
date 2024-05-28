package com.moltenwolfcub.creativetexturingtool;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class CreativeTexturingToolClient implements ClientModInitializer {
    private static final KeyMapping toggle;
    private static final KeyMapping increaseSize;
    private static final KeyMapping decreaseSize;

    static {
        toggle = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key."+CreativeTexturingTool.MODID+ ".toggle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_APOSTROPHE,
                "category."+CreativeTexturingTool.MODID
        ));
        increaseSize = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key."+CreativeTexturingTool.MODID+ ".increaseSize",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_KP_ADD,
                "category."+CreativeTexturingTool.MODID
        ));
        decreaseSize = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key."+CreativeTexturingTool.MODID+ ".decreaseSize",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_KP_SUBTRACT,
                "category."+CreativeTexturingTool.MODID
        ));
    }

    private void handleKeybind(KeyMapping key, Consumer<ServerPlayer> behaviour, Minecraft client){
        while (key.consumeClick()) {
            if (client.hasSingleplayerServer()){
                MinecraftServer server = client.getSingleplayerServer();
                ServerPlayer player = server.getPlayerList().getPlayer(client.player.getUUID());
                behaviour.accept(player);

            } else {
                client.player.sendSystemMessage(Component.literal("Can't use keybinds in server setting."));
            }
        }
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
            while (increaseSize.consumeClick()) {
                if (client.hasSingleplayerServer()){
                    MinecraftServer server = client.getSingleplayerServer();
                    ServerPlayer player = server.getPlayerList().getPlayer(client.player.getUUID());
                    increaseSize(player);

                } else {
                    client.player.sendSystemMessage(Component.literal("Can't use keybinds in server setting."));
                }
            }
            while (decreaseSize.consumeClick()) {
                if (client.hasSingleplayerServer()){
                    MinecraftServer server = client.getSingleplayerServer();
                    ServerPlayer player = server.getPlayerList().getPlayer(client.player.getUUID());
                    decreaseSize(player);

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
    private static void increaseSize(ServerPlayer player) {
        DataSaver dataSaver = (DataSaver) player;
        int current = dataSaver.getPersistantData().getInt("size");
        int newSize = current+1;

        dataSaver.getPersistantData().putInt("size", newSize);
        player.sendSystemMessage(Component.literal("Increased paintbrush size ("+newSize+")"));
    }
    private static void decreaseSize(ServerPlayer player) {
        DataSaver dataSaver = (DataSaver) player;
        int current = dataSaver.getPersistantData().getInt("size");
        if (current < 1){
            dataSaver.getPersistantData().putInt("size", 1);
        }
        if (current == 1){
            player.sendSystemMessage(Component.literal("Can't make size any smaller than "+current));
            return;
        }

        int newSize = current-1;

        dataSaver.getPersistantData().putInt("size", newSize);
        player.sendSystemMessage(Component.literal("Decreased paintbrush size ("+newSize+")"));
    }
}
