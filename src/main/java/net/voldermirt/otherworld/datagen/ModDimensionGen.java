package net.voldermirt.otherworld.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.types.templates.Tag;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemUsage;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagEntry;
import net.minecraft.registry.tag.WorldPresetTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionOptionsRegistryHolder;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.WorldPresets;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.noise.NoiseRouter;
import net.voldermirt.otherworld.OtherworldMod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalLong;

public class ModDimensionGen {

    public static final RegistryKey<DimensionType> OTHERWORLD_TYPE_KEY = registerDimTypeKey("otherworld_dim_type");

    public static final RegistryKey<WorldPreset> OTHERWORLD_WORLD_PRESET =
            RegistryKey.of(RegistryKeys.WORLD_PRESET, new Identifier(OtherworldMod.MOD_ID, "otherworld_world_preset"));

    public static final RegistryKey<DimensionOptions> OTHERWORLD_DIM_KEY = registerDimKey("otherworld_dimension");

    public static final RegistryKey<ChunkGeneratorSettings> OTHERWORLD_CHUNK_GEN_KEY =
            RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, new Identifier(OtherworldMod.MOD_ID, "otherworld_chunk_gen"));

    public static void bootstrapDimension(Registerable<DimensionOptions> ctx) {
        System.out.println("Registering Otherworld Dimension.");

        RegistryEntryLookup<Biome> biomes = ctx.getRegistryLookup(RegistryKeys.BIOME);
        RegistryEntryLookup<DimensionType> dimTypes = ctx.getRegistryLookup(RegistryKeys.DIMENSION_TYPE);
        RegistryEntryLookup<ChunkGeneratorSettings> chunkGenSettings = ctx.getRegistryLookup(RegistryKeys.CHUNK_GENERATOR_SETTINGS);

        NoiseChunkGenerator chunkGenerator = new NoiseChunkGenerator(
                new MultiNoiseBiomeSource.Preset(new Identifier(OtherworldMod.MOD_ID, "biome_source"), (biomeLookup) ->
                {
                    return new MultiNoiseUtil.Entries<>(ImmutableList.of(
                            Pair.of(MultiNoiseUtil.createNoiseHypercube(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f), biomeLookup.getOrThrow(BiomeKeys.CRIMSON_FOREST))));
                }).getBiomeSource(biomes),
                chunkGenSettings.getOrThrow(OTHERWORLD_CHUNK_GEN_KEY)
        );

        DimensionOptions dim = new DimensionOptions(
                dimTypes.getOrThrow(OTHERWORLD_TYPE_KEY),
                chunkGenerator
        );


        ctx.register(OTHERWORLD_DIM_KEY, dim);
    }

    public static void bootstrapWorldPreset(Registerable<WorldPreset> ctx) {
        //Map dimensions = new HashMap<RegistryKey<DimensionOptions>, DimensionOptions>();
        //dimensions.put(OTHERWORLD_DIM_KEY, bootstrapDimension(ctx));
        //dimensions.put(DimensionOptions.OVERWORLD, ctx.getRegistryLookup(RegistryKeys.DIMENSION).getOrThrow(DimensionOptions.OVERWORLD));



        //ctx.register(OTHERWORLD_WORLD_PRESET, new WorldPreset(dimensions));
    }

    public static void bootstrapDimensionType(Registerable<DimensionType> ctx) {

        System.out.println("Registering Otherworld Dimension Type.");

        var otherworldDimType = registerDimensionType(ctx, OTHERWORLD_TYPE_KEY, new DimensionType(
                OptionalLong.empty(), // Fixed time
                true, // Skylight
                false, // Has ceiling
                false, // Ultrawarm
                false, // Natural
                1.0, // Coordinate scale
                true, // Bed works
                true, // Piglin Safe
                -64, // Min y
                64 + 320, // Height
                64 + 320, // Logical Height
                BlockTags.INFINIBURN_OVERWORLD, // Infiniburn
                new Identifier("minecraft:overworld"), // Effects
                0.0f, // Ambient Light
                new DimensionType.MonsterSettings(false, false, UniformIntProvider.create(0, 7), 7)



        ));
    }

    public static void bootstrapChunkGenerator(Registerable<ChunkGeneratorSettings> ctx) {
        System.out.println("Registering Chunk Generators.");


        GenerationShapeConfig shapeConfig = new GenerationShapeConfig(-64, 320, 1, 2);
        ctx.register(OTHERWORLD_CHUNK_GEN_KEY, new ChunkGeneratorSettings(
            shapeConfig,
                Blocks.STONE.getDefaultState(),
                Blocks.WATER.getDefaultState(),
                new NoiseRouter(
                        DensityFunctionTypes.zero(),
                        DensityFunctionTypes.zero(),
                        DensityFunctionTypes.zero(),
                        DensityFunctionTypes.zero(),
                        DensityFunctionTypes.zero(),
                        DensityFunctionTypes.zero(),
                        DensityFunctionTypes.zero(),
                        DensityFunctionTypes.zero(),
                        DensityFunctionTypes.zero(),
                        DensityFunctionTypes.zero(),
                        DensityFunctionTypes.zero(),
                        DensityFunctionTypes.zero(),
                        DensityFunctionTypes.zero(),
                        DensityFunctionTypes.zero(),
                        DensityFunctionTypes.zero()
                ),
                null, //TODO: This
                List.of(),
                0,
                false,
                false,
                false,
                false
        ));
    }

    private static void registerDimension(Registerable<DimensionOptions> ctx, RegistryKey<DimensionOptions> key, DimensionOptions dim) {
        ctx.register(key, dim);
    }

    private static RegistryEntry.Reference<DimensionType> registerDimensionType(Registerable<DimensionType> ctx, RegistryKey<DimensionType> key, DimensionType dimType) {
        return ctx.register(key, dimType);
    }

    public static RegistryKey<DimensionType> registerDimTypeKey(String name) {
        return RegistryKey.of(RegistryKeys.DIMENSION_TYPE, new Identifier(OtherworldMod.MOD_ID, name));
    }

    public static RegistryKey<DimensionOptions> registerDimKey(String name) {
        return RegistryKey.of(RegistryKeys.DIMENSION, new Identifier(OtherworldMod.MOD_ID, name));
    }

}
