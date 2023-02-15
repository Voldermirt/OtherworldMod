package net.voldermirt.otherworld.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.voldermirt.otherworld.world.feature.ModConfiguredFeatures;
import net.voldermirt.otherworld.world.feature.ModPlacedFeatures;

public class ModDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModAdvancementGen::new);
        pack.addProvider(ModWorldGen::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.DIMENSION_TYPE, ModDimensionGen::bootstrapDimensionType);
        registryBuilder.addRegistry(RegistryKeys.BIOME, ModBiomeGen::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.CHUNK_GENERATOR_SETTINGS, ModDimensionGen::bootstrapChunkGenerator);
        //registryBuilder.addRegistry(RegistryKeys.WORLD_PRESET, ModDimensionGen::bootstrapWorldPreset);
        //registryBuilder.addRegistry(RegistryKeys.DIMENSION, ModDimensionGen::bootstrapDimension);
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
    }
}
