package net.voldermirt.otherworld.world.feature;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.voldermirt.otherworld.OtherworldMod;
import net.voldermirt.otherworld.block.ModBlocks;

import java.util.List;

public class ModConfiguredFeatures {
/*
    private static final TagMatchRuleTest STONE_ORE_REPLACEABLES = new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES);

    public static final List<OreFeatureConfig.Target> OVERWORLD_VERIDIUM_ORES = List.of(
            OreFeatureConfig.createTarget(STONE_ORE_REPLACEABLES, ModBlocks.VERIDIUM_ORE.getDefaultState())
    );

    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> VERIDIUM_ORE =
            ConfiguredFeatures.register();

*/
    public static void registerConfiguredFeatures() {
        OtherworldMod.LOGGER.debug("Registering Configured Features!");
    }
}
