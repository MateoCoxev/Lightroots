package net.coxev.lightroots.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.FeatureConfig;

public class LightrootFeatureConfig implements FeatureConfig {
    public static final Codec<LightrootFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codec.intRange(1, 512).fieldOf("floor_to_ceiling_search_range").orElse(30).forGetter((config) -> {
            return config.floorToCeilingSearchRange;
        })).apply(instance, LightrootFeatureConfig::new);
    });

    public final int floorToCeilingSearchRange;

    public LightrootFeatureConfig(int floorToCeilingSearchRange) {
        this.floorToCeilingSearchRange = floorToCeilingSearchRange;
    }
}
