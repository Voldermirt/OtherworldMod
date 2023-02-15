package net.voldermirt.otherworld.datagen;

import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.MiscPlacedFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.voldermirt.otherworld.OtherworldMod;
import org.joml.Vector3f;

public class ModBiomeGen {

    public static final RegistryKey<Biome> FEY_WOODS = getKey("fey_woods");

    private static RegistryKey<Biome> getKey(String id) {
        return RegistryKey.of(RegistryKeys.BIOME, OtherworldMod.id(id));
    }

    public static void bootstrap(Registerable<Biome> ctx) {
        RegistryEntryLookup<PlacedFeature> features = ctx.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
        RegistryEntryLookup<ConfiguredCarver<?>> carvers = ctx.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER);

        // Fey woods
        ctx.register(FEY_WOODS,
                new Biome.Builder()
                        .temperature(0.5f)
                        .downfall(0.68f)
                        .precipitation(Biome.Precipitation.RAIN)
                        .effects(getDefaultEffects().build())
                        .spawnSettings(getDefaultSpawnSettings().build())
                        .generationSettings(getDefaultGenSettings(features, carvers)
                                .feature(GenerationStep.Feature.LOCAL_MODIFICATIONS, MiscPlacedFeatures.FOREST_ROCK)
                                .feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.PATCH_LARGE_FERN)
                                .feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.FLOWER_WARM).build())
                        .temperatureModifier(Biome.TemperatureModifier.NONE)
                        .build()
        );
    }

    private static BiomeEffects.Builder getDefaultEffects() {
        BiomeEffects.Builder builder = new BiomeEffects.Builder();

        builder.skyColor(0x28B896);
        builder.fogColor(0x466E45);
        builder.grassColor(0x06AD00);
        builder.foliageColor(0x048500);
        builder.waterColor(0x0099D1);
        builder.waterFogColor(0x0099D1);
        builder.particleConfig(new BiomeParticleConfig(
                new DustParticleEffect(new Vector3f(0.137f, 0.55f, 0.157f), 0.5f),
                0.1f
        ));

        return builder;
    }

    private static SpawnSettings.Builder getDefaultSpawnSettings() {
        return new SpawnSettings.Builder();
    }

    private static GenerationSettings.LookupBackedBuilder getDefaultGenSettings(RegistryEntryLookup<PlacedFeature> featureGetter, RegistryEntryLookup<ConfiguredCarver<?>> carverGetter) {
        GenerationSettings.LookupBackedBuilder builder = new GenerationSettings.LookupBackedBuilder(featureGetter, carverGetter);

        DefaultBiomeFeatures.addDefaultOres(builder);
        DefaultBiomeFeatures.addMineables(builder);
        DefaultBiomeFeatures.addDefaultGrass(builder);
        DefaultBiomeFeatures.addDefaultFlowers(builder);


        return builder;
    }

}
