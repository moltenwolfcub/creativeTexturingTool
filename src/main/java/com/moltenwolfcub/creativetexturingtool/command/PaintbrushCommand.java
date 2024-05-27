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
                                        .then(Commands.literal("set")
                                                .then(Commands.argument("size", IntegerArgumentType.integer(1))
                                                        .executes(PaintbrushCommand::changeSize)
                                                )
                                        )
                                        .then(Commands.literal("add")
                                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                                        .executes(PaintbrushCommand::increaseSize)
                                                )
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

    private static int increaseSize(CommandContext<CommandSourceStack> context) {
        final int amount = IntegerArgumentType.getInteger(context, "amount");

        DataSaver player = (DataSaver) context.getSource().getPlayer();
        int current = player.getPersistantData().getInt("size");
        int newSize = Math.max(current+amount, 1);


        player.getPersistantData().putInt("size", newSize);
        context.getSource().sendSuccess(() -> Component.literal("Increased paintbrush size by "+amount+" ("+newSize+")"), false);

        return 1;
    }
}
