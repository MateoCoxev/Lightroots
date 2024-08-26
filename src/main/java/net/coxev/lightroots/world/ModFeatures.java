package net.coxev.lightroots.world;

import net.coxev.lightroots.Lightroots;
import net.coxev.lightroots.world.feature.LightrootFeature;
import net.coxev.lightroots.world.feature.LightrootFeatureConfig;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class ModFeatures<FC extends FeatureConfig> {
    public static final Feature<LightrootFeatureConfig> LIGHTROOT = registerFeature("lightroot", new LightrootFeature(LightrootFeatureConfig.CODEC));

    private static <C extends FeatureConfig, F extends Feature<C>> F registerFeature(String name, F feature){
        return Registry.register(Registries.FEATURE, new Identifier(Lightroots.MOD_ID, name), feature);
    }

    public static void registerModFeatures(){
        Lightroots.LOGGER.info("Registering ModFeatures for " + Lightroots.MOD_ID);
    }
}
