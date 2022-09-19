package com.awakenedredstone.simpletech;

import com.awakenedredstone.simpletech.client.rendering.QuarryScreenHandler;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleTech implements ModInitializer {
    public static final String MOD_ID = "simpletech";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "general"), () -> new ItemStack(ModBlocks.QUARRY));
    public static ScreenHandlerType<QuarryScreenHandler> QUARRY_SCREEN_HANDLER;

    @Override
    public void onInitialize() {
        QUARRY_SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, new Identifier(MOD_ID, "quarry"), new ScreenHandlerType<>(QuarryScreenHandler::new));
        FieldRegistrationHandler.register(ModBlocks.class, MOD_ID, false);
        FieldRegistrationHandler.register(ModBlockEntities.class, MOD_ID, false);
    }
}
