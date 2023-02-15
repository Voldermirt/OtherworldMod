package net.voldermirt.otherworld.block;

import com.ibm.icu.impl.Row;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.voldermirt.otherworld.OtherworldMod;
import net.voldermirt.otherworld.block.custom.DruidAltarBlock;
import net.voldermirt.otherworld.block.custom.OtherworldPortalBlock;
import net.voldermirt.otherworld.block.custom.RowanLeavesBlock;
import net.voldermirt.otherworld.item.ModItemGroups;
import net.voldermirt.otherworld.world.feature.tree.RowanSaplingGenerator;

public class ModBlocks {

    // Blocks
    public static final Block VERIDIUM_BLOCK = registerBlock("veridium_block",
            new Block(FabricBlockSettings.of(Material.SOLID_ORGANIC).hardness(20).resistance(1000f).requiresTool()), ModItemGroups.OTHERWORLD_ITEMS);
    public static final Block VERIDIUM_ORE = registerBlock("veridium_ore",
            new ExperienceDroppingBlock(FabricBlockSettings.of(Material.STONE).hardness(3).resistance(3f).requiresTool(), UniformIntProvider.create(3, 7)), ModItemGroups.OTHERWORLD_ITEMS);
    public static final Block DRUID_ALTAR_BLOCK = registerBlock("druid_altar",
            new DruidAltarBlock(FabricBlockSettings.of(Material.SOLID_ORGANIC).hardness(12).resistance(900f).requiresTool().nonOpaque()), ModItemGroups.OTHERWORLD_ITEMS);
    public static final Block ROWAN_LEAVES = registerBlock("rowan_leaves",
            new RowanLeavesBlock(AbstractBlock.Settings.of(Material.LEAVES).strength(0.2F).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque()
                    .allowsSpawning((state, world, pos, type) -> type == EntityType.OCELOT || type == EntityType.PARROT).suffocates((a, b, c) -> false)
                    .blockVision((a, b, c) -> false)), ModItemGroups.OTHERWORLD_ITEMS);
    public static final Block ROWAN_LOG = registerBlock("rowan_log",
            new PillarBlock(AbstractBlock.Settings.of(Material.WOOD, (state) -> {
                return state.get(PillarBlock.AXIS) == Direction.Axis.Y ? MapColor.ORANGE : MapColor.OAK_TAN;
            }).strength(2.0F).sounds(BlockSoundGroup.WOOD)), ModItemGroups.OTHERWORLD_ITEMS);
    public static final Block ROWAN_WOOD = registerBlock("rowan_wood",
            new PillarBlock(AbstractBlock.Settings.of(Material.WOOD, MapColor.OAK_TAN).strength(2.0F).
                    sounds(BlockSoundGroup.WOOD)), ModItemGroups.OTHERWORLD_ITEMS);
    public static final Block ROWAN_PLANKS = registerBlock("rowan_planks",
            new Block(AbstractBlock.Settings.of(Material.WOOD, MapColor.OAK_TAN).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD)),
            ModItemGroups.OTHERWORLD_ITEMS);

    public static final Block ROWAN_SAPLING = registerBlock("rowan_sapling",
            new SaplingBlock(new RowanSaplingGenerator(), FabricBlockSettings.copy(Blocks.OAK_SAPLING)), ModItemGroups.OTHERWORLD_ITEMS);

    public static final Block OTHERWORLD_PORTAL = registerBlock("otherworld_portal",
            new OtherworldPortalBlock(FabricBlockSettings.of(Material.PORTAL, MapColor.BLACK).luminance(12)/*.solidBlock(((state, world, pos) -> {
                return !state.get(OtherworldPortalBlock.OPEN).booleanValue();
            }))*/.noCollision()));


    private static Block registerBlock(String name, Block block, ItemGroup group) {
        registerBlockItem(name, block, group);
        return registerBlock(name, block);
    }

    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(OtherworldMod.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup group) {
        Item i = Registry.register(Registries.ITEM, new Identifier(OtherworldMod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
        // Add to group
        ItemGroupEvents.modifyEntriesEvent(group).register(entries -> {
            entries.add(i);
        });
        return i;
    }

    public static void registerModBlocks () {
        OtherworldMod.LOGGER.debug("Registering blocks!");
    }

}
