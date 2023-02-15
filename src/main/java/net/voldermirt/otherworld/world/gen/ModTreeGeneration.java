package net.voldermirt.otherworld.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;
import net.voldermirt.otherworld.datagen.ModBiomeGen;
import net.voldermirt.otherworld.world.feature.ModPlacedFeatures;

public class ModTreeGeneration {
    public static void generateTrees() {
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomeGen.FEY_WOODS),
                GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.ROWAN_PLACED);
    }
}
