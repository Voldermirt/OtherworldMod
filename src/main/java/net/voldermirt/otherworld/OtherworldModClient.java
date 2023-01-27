package net.voldermirt.otherworld;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.voldermirt.otherworld.networking.ModMessages;
import net.voldermirt.otherworld.screen.DruidAltarScreen;
import net.voldermirt.otherworld.screen.ModScreenHandlers;

public class OtherworldModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.DRUID_ALTAR_SCREEN_HANDLER, DruidAltarScreen::new);
        ModMessages.registerS2CPackets();
    }
}
