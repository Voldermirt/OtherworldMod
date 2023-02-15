package net.voldermirt.otherworld.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.voldermirt.otherworld.OtherworldMod;

import java.util.concurrent.CompletableFuture;

public class ModWorldGen extends FabricDynamicRegistryProvider {

    public ModWorldGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        entries.addAll(registries.getWrapperOrThrow(RegistryKeys.DIMENSION_TYPE));
        entries.addAll(registries.getWrapperOrThrow(RegistryKeys.BIOME));
        //entries.addAll(registries.getWrapperOrThrow(RegistryKeys.DIMENSION));
        entries.addAll(registries.getWrapperOrThrow(RegistryKeys.CHUNK_GENERATOR_SETTINGS));
        //entries.addAll(registries.getWrapperOrThrow(RegistryKeys.WORLD_PRESET));
        entries.addAll(registries.getWrapperOrThrow(RegistryKeys.CONFIGURED_FEATURE));
        entries.addAll(registries.getWrapperOrThrow(RegistryKeys.PLACED_FEATURE));
    }

    @Override
    public String getName() {
        return OtherworldMod.MOD_ID;
    }
}
