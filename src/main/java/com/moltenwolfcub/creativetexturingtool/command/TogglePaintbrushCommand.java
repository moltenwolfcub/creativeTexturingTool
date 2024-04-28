package com.moltenwolfcub.creativetexturingtool.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.moltenwolfcub.creativetexturingtool.CreativeTexturingTool;
import com.moltenwolfcub.creativetexturingtool.DataSaver;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class TogglePaintbrushCommand {
    public static void register(Event<CommandRegistrationCallback> event) {
        CreativeTexturingTool.LOGGER.info("Registering Commands");

        event.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                Commands.literal("paintbrush").then(Commands.literal("toggleActive").executes(TogglePaintbrushCommand::run))
        ));
    }

    private static int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        DataSaver player = (DataSaver) context.getSource().getPlayer();

        boolean current = player.getPersistantData().getBoolean("active");

        player.getPersistantData().putBoolean("active", !current);
        context.getSource().sendSuccess(() -> Component.literal("Toggled active to "+!current), false);
        return 1;
    }
}
