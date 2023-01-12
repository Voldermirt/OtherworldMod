package net.voldermirt.otherworld;

import net.fabricmc.api.ModInitializer;
import net.voldermirt.otherworld.block.ModBlocks;
import net.voldermirt.otherworld.block.entity.ModBlockEntities;
import net.voldermirt.otherworld.item.ModItems;
import net.voldermirt.otherworld.screen.ModScreenHandlers;
import net.voldermirt.otherworld.world.feature.ModConfiguredFeatures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OtherworldMod implements ModInitializer {
	public static final String MOD_ID = "otherworld";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModConfiguredFeatures.registerConfiguredFeatures();

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();
	}
}
