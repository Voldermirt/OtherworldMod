package net.voldermirt.otherworld;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.voldermirt.otherworld.block.ModBlocks;
import net.voldermirt.otherworld.networking.ModMessages;
import net.voldermirt.otherworld.screen.DruidAltarScreen;
import net.voldermirt.otherworld.screen.ModScreenHandlers;

public class OtherworldModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ROWAN_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ROWAN_SAPLING, RenderLayer.getCutout());

        HandledScreens.register(ModScreenHandlers.DRUID_ALTAR_SCREEN_HANDLER, DruidAltarScreen::new);
        ModMessages.registerS2CPackets();
    }
}
