package net.voldermirt.otherworld.world.feature;

import net.fabricmc.fabric.api.biome.v1.*;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.ForkingTrunkPlacer;
import net.voldermirt.otherworld.OtherworldMod;
import net.voldermirt.otherworld.block.ModBlocks;

import java.util.List;
import java.util.function.BiConsumer;
public class ModConfiguredFeatures {

    public static final RegistryKey<ConfiguredFeature<?, ?>> ROWAN_TREE = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, OtherworldMod.id("rowan_tree"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> ROWAN_SPAWN_KEY = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, OtherworldMod.id("rowan_spawn"));


    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> ctx) {
        var placedFeatureRegistryEntryLookup = ctx.getRegistryLookup(RegistryKeys.PLACED_FEATURE);

        register(ctx, ROWAN_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.ROWAN_LOG),
                new ForkingTrunkPlacer(5, 6, 3),
                BlockStateProvider.of(ModBlocks.ROWAN_LEAVES),
                new BlobFoliagePlacer(ConstantIntProvider.create(3), ConstantIntProvider.create(0), 5),
                new TwoLayersFeatureSize(1, 0, 2)
        ).build());

        register(ctx, ROWAN_SPAWN_KEY, Feature.RANDOM_SELECTOR,
                new RandomFeatureConfig(List.of(new RandomFeatureEntry(placedFeatureRegistryEntryLookup.getOrThrow(ModPlacedFeatures.ROWAN_CHECKED),
                        0.5f)), placedFeatureRegistryEntryLookup.getOrThrow(ModPlacedFeatures.ROWAN_CHECKED)));
    }


    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> ctx,
                                                                                   RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        ctx.register(key, new ConfiguredFeature<>(feature, configuration));
    }

}