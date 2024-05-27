package com.moltenwolfcub.creativetexturingtool.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.moltenwolfcub.creativetexturingtool.DataSaver;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class PaintbrushCommand {
    public static void register(Event<CommandRegistrationCallback> event) {
        event.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                Commands.literal("paintbrush").requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                        .then(
                                Commands.literal("toggle")
                                        .executes(PaintbrushCommand::toggleActive)
                        ).then(
                                Commands.literal("size")
                                        .then(Commands.argument("size", IntegerArgumentType.integer(1))
                                                .executes(PaintbrushCommand::changeSize)
                                        )
                        )
        ));
    }

    private static int toggleActive(CommandContext<CommandSourceStack> context) {
        DataSaver player = (DataSaver) context.getSource().getPlayer();

        boolean current = player.getPersistantData().getBoolean("active");

        player.getPersistantData().putBoolean("active", !current);
        context.getSource().sendSuccess(() -> Component.literal("Toggled paintbrush active to "+!current), false);
        return 1;
    }

    private static int changeSize(CommandContext<CommandSourceStack> context) {
        final int size = IntegerArgumentType.getInteger(context, "size");

        DataSaver player = (DataSaver) context.getSource().getPlayer();

        player.getPersistantData().putInt("size", size);
        context.getSource().sendSuccess(() -> Component.literal("Set paintbrush size to "+size), false);

        return 1;
    }
}
