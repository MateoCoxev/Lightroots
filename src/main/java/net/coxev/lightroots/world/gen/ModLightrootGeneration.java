package net.coxev.lightroots.world.gen;

import net.coxev.lightroots.world.ModPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;

public class ModLightrootGeneration {
    public static void generateLightroots(){
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_DECORATION, ModPlacedFeatures.LIGHTROOT_PLACED_KEY);
    }
}
