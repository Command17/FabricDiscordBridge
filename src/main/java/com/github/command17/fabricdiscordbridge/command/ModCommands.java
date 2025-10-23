package com.github.command17.fabricdiscordbridge.command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class ModCommands {
    private static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        DiscordBridgeCommand.register(dispatcher);
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register(ModCommands::registerCommands);
    }
}
