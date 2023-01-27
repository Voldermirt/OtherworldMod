package net.voldermirt.otherworld.networking.packets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.voldermirt.otherworld.block.entity.DruidAltarBlockEntity;

public class InfuseItemC2SPacket  {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {

        BlockPos pos = buf.readBlockPos();
        World world = player.getWorld();
        server.execute(() -> {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof DruidAltarBlockEntity entity) {
                entity.infuseItem();
            }
        });
    }
}