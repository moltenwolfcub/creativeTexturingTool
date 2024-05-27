package com.moltenwolfcub.creativetexturingtool;

import com.moltenwolfcub.creativetexturingtool.command.PaintbrushCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreativeTexturingTool implements ModInitializer {
    public static final String MODID = "creativetexturingtool";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
        LOGGER.info("Starting Creative Texturing Tool Initialisation");
        registerCommands();
    }

    public void registerCommands() {
        CreativeTexturingTool.LOGGER.info("Registering Commands");
        PaintbrushCommand.register(CommandRegistrationCallback.EVENT);
    }
}
