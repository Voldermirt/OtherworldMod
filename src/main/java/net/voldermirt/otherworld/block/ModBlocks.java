package net.voldermirt.otherworld.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.voldermirt.otherworld.OtherworldMod;
import net.voldermirt.otherworld.item.ModItemGroups;

public class ModBlocks {

    // Blocks
    public static final Block VERIDIUM_BLOCK = registerBlock("veridium_block",
            new Block(FabricBlockSettings.of(Material.SOLID_ORGANIC).hardness(20).resistance(1000f).requiresTool()), ModItemGroups.OTHERWORLD_ITEMS);
    public static final Block VERIDIUM_ORE = registerBlock("veridium_ore",
            new ExperienceDroppingBlock(FabricBlockSettings.of(Material.STONE).hardness(3).resistance(3f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroups.OTHERWORLD_ITEMS);


    private static Block registerBlock(String name, Block block, ItemGroup group) {
        registerBlockItem(name, block, group);
        return Registry.register(Registries.BLOCK, new Identifier(OtherworldMod.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup group) {
        Item i = Registry.register(Registries.ITEM, new Identifier(OtherworldMod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
        // Add to group
        ItemGroupEvents.modifyEntriesEvent(ModItemGroups.OTHERWORLD_ITEMS).register(entries -> {
            entries.add(i);
        });
        return i;
    }

    public static void registerModBlocks () {
        OtherworldMod.LOGGER.debug("Registering blocks!");
    }

}
