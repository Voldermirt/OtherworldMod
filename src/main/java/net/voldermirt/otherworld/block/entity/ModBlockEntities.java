package net.voldermirt.otherworld.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.voldermirt.otherworld.OtherworldMod;
import net.voldermirt.otherworld.block.ModBlocks;

public class ModBlockEntities {

    public static BlockEntityType<DruidAltarBlockEntity> DRUID_ALTAR;

    public static void registerBlockEntities() {
        DRUID_ALTAR = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(OtherworldMod.MOD_ID, "druid_altar"),
                FabricBlockEntityTypeBuilder.create(DruidAltarBlockEntity::new,
                        ModBlocks.DRUID_ALTAR_BLOCK).build(null));
    }
}
