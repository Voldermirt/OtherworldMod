package net.voldermirt.otherworld;

import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.voldermirt.otherworld.block.ModBlocks;
import net.voldermirt.otherworld.block.entity.ModBlockEntities;
import net.voldermirt.otherworld.item.ModItems;
import net.voldermirt.otherworld.networking.ModMessages;
import net.voldermirt.otherworld.recipe.ModRecipes;
import net.voldermirt.otherworld.screen.ModScreenHandlers;
import net.voldermirt.otherworld.world.feature.ModConfiguredFeatures;
import net.voldermirt.otherworld.world.gen.ModTreeGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OtherworldMod implements ModInitializer {
	public static final String MOD_ID = "otherworld";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final RegistryKey<World> THE_OTHERWORLD = RegistryKey.of(RegistryKeys.WORLD, id("the_otherworld"));

	public static Identifier id (String name) {
		return new Identifier(MOD_ID, name);
	}

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		ModTreeGeneration.generateTrees();

		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();

		ModRecipes.registerRecipes();
		ModMessages.registerC2SPackets();
	}
}
