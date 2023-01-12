package net.voldermirt.otherworld.world.feature;

import net.fabricmc.fabric.api.biome.v1.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;
import net.voldermirt.otherworld.OtherworldMod;

import java.util.List;
import java.util.function.BiConsumer;

//
// Thanks to @MBektic on Discord
public class ModConfiguredFeatures {

    public static final RegistryKey<PlacedFeature> VERIDIUM_ORE =
            RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(OtherworldMod.MOD_ID, "ore_veridium"));
// This is for the nether, you need 2 more json files.
//    public static final RegistryKey<PlacedFeature> TANZINTE_ORE_NETHER =
//            RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(TutorialMod.MOD_ID, "tanzinte_ore_nether"));

    public static void registerConfiguredFeatures() {
        OtherworldMod.LOGGER.debug("Registering ConfiguredFeatures!");

        BiomeModifications.create(new Identifier(OtherworldMod.MOD_ID, "features"))
                .add( ModificationPhase.ADDITIONS, BiomeSelectors.foundInOverworld(), myOreModifier(VERIDIUM_ORE) )
        // This is for the nether, you need 2 more json files.
        //.add( ModificationPhase.ADDITIONS, BiomeSelectors.foundInTheNether(), myOreModifier(TANZINTE_ORE_NETHER) )
        ;
    }

    private static BiConsumer<BiomeSelectionContext, BiomeModificationContext> myOreModifier(RegistryKey<PlacedFeature> orePlacedFeatureKey) {
        return (biomeSelectionContext, biomeModificationContext) ->
                biomeModificationContext.getGenerationSettings()
                        .addFeature(GenerationStep.Feature.UNDERGROUND_ORES, orePlacedFeatureKey);
    }


}