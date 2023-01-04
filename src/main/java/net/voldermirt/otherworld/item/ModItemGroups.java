package net.voldermirt.otherworld.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.voldermirt.otherworld.OtherworldMod;

public class ModItemGroups {

    public static final ItemGroup OTHERWORLD_ITEMS = FabricItemGroup.builder(new Identifier(OtherworldMod.MOD_ID, "otherworld_items"))
            .displayName(Text.literal("Otherworld"))
            .icon(() -> new ItemStack(ModItems.RAW_VERIDIUM))
            .build();

}
