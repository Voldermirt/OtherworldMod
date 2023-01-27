package net.voldermirt.otherworld.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.voldermirt.otherworld.OtherworldMod;
import net.voldermirt.otherworld.networking.packets.EnergySyncS2CPacket;
import net.voldermirt.otherworld.networking.packets.InfuseItemC2SPacket;

public class ModMessages {

    public static final Identifier ENERGY_SYNC_ID = new Identifier(OtherworldMod.MOD_ID, "energy_sync");
    public static final Identifier INFUSE_ITEM_ID = new Identifier(OtherworldMod.MOD_ID, "infuse_item");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(INFUSE_ITEM_ID, InfuseItemC2SPacket::receive);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ENERGY_SYNC_ID, EnergySyncS2CPacket::receive);
    }
}
