package net.coxev.lightroots;

import net.coxev.lightroots.block.entity.ModBlockEntities;
import net.coxev.lightroots.block.ModBlocks;
import net.coxev.lightroots.world.ModFeatures;
import net.coxev.lightroots.world.gen.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lightroots implements ModInitializer {
	public static final String MOD_ID = "lightroots";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerModBlockEntities();
		ModFeatures.registerModFeatures();

		ModWorldGeneration.generateModWorldGen();
	}
}