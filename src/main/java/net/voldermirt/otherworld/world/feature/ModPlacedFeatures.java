package net.voldermirt.otherworld.world.feature;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.voldermirt.otherworld.OtherworldMod;
import net.voldermirt.otherworld.block.ModBlocks;

import java.util.List;

public class ModPlacedFeatures {

    public static final RegistryKey<PlacedFeature> ROWAN_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, OtherworldMod.id("rowan_placed"));
    public static final RegistryKey<PlacedFeature> ROWAN_CHECKED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, OtherworldMod.id("rowan_checked"));

    public static void bootstrap(Registerable<PlacedFeature> ctx) {
        var configuredFeatureRegistryEntryLookup = ctx.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        ctx.register(ROWAN_CHECKED, new PlacedFeature(configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.ROWAN_TREE),
                List.of(PlacedFeatures.wouldSurvive(ModBlocks.ROWAN_SAPLING))));

        ctx.register(ROWAN_PLACED, new PlacedFeature(configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.ROWAN_SPAWN_KEY),
                VegetationPlacedFeatures.modifiers(PlacedFeatures.createCountExtraModifier(1, 0.1f, 2))));
    }
}
