package net.voldermirt.otherworld.screen;

import net.minecraft.screen.ScreenHandlerType;

public class ModScreenHandlers {

    public static ScreenHandlerType<DruidAltarScreenHandler> DRUID_ALTAR_SCREEN_HANDLER;

    public static void registerScreenHandlers() {
        DRUID_ALTAR_SCREEN_HANDLER = new ScreenHandlerType<>(DruidAltarScreenHandler::new);
    }
}
