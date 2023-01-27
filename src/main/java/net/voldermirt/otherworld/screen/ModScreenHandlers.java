package net.voldermirt.otherworld.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.voldermirt.otherworld.OtherworldMod;

public class ModScreenHandlers {

    public static ScreenHandlerType<DruidAltarScreenHandler> DRUID_ALTAR_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(DruidAltarScreenHandler::new);

    public static void registerScreenHandlers() {
        Registry.register(Registries.SCREEN_HANDLER, new Identifier(OtherworldMod.MOD_ID, "druid_altar"),
                DRUID_ALTAR_SCREEN_HANDLER);
    }
}
