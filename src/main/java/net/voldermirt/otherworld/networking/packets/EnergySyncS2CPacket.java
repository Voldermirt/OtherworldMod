package net.voldermirt.otherworld.networking.packets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.voldermirt.otherworld.block.entity.DruidAltarBlockEntity;

public class EnergySyncS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int energy = buf.readInt();
        ClientWorld world = client.world;
        BlockPos pos = buf.readBlockPos();

        client.execute(() -> {
            if (world.getBlockEntity(pos) instanceof DruidAltarBlockEntity entity) {
                entity.setEnergy(energy);
            }
        });
    }
}
