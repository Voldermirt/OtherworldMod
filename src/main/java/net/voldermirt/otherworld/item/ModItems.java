package net.voldermirt.otherworld.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.enchantment.MendingEnchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.voldermirt.otherworld.OtherworldMod;
import net.voldermirt.otherworld.item.custom.GrassWandItem;



public class ModItems {

    // Items
    public static final Item RAW_VERIDIUM = registerItem("raw_veridium",
            new Item(new FabricItemSettings()));
    public static final Item VERIDIUM_INGOT = registerItem("veridium_ingot",
            new Item(new FabricItemSettings()));
    public static final Item GRASS_WAND = registerItem("grass_wand",
            new GrassWandItem(new FabricItemSettings(), 64));


    private static Item registerItem(String name, Item item) {
        Item i = Registry.register(Registries.ITEM, new Identifier(OtherworldMod.MOD_ID, name), item);
        // Add to group
        ItemGroupEvents.modifyEntriesEvent(ModItemGroups.OTHERWORLD_ITEMS).register(entries -> {
            entries.add(i);
        });
        return i;

    }

    public static void registerModItems() {
        OtherworldMod.LOGGER.debug("Registering Items!");
    }
}
