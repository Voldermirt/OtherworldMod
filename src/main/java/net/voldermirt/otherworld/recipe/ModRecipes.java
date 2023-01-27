package net.voldermirt.otherworld.recipe;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.voldermirt.otherworld.OtherworldMod;

public class ModRecipes {

    public static void registerRecipes() {
        OtherworldMod.LOGGER.debug("Registering recipes!");

        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(OtherworldMod.MOD_ID, DruidAltarRecipe.Serializer.ID),
                DruidAltarRecipe.Serializer.SINGLETON);
        Registry.register(Registries.RECIPE_TYPE, new Identifier(OtherworldMod.MOD_ID, DruidAltarRecipe.Type.ID),
                DruidAltarRecipe.Type.SINGLETON);
    }
}
