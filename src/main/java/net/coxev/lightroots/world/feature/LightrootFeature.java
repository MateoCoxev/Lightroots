package net.coxev.lightroots.world.feature;

import com.mojang.serialization.Codec;
import net.coxev.lightroots.block.ModBlocks;
import net.coxev.lightroots.util.GenerateStructure;
import net.coxev.lightroots.world.ModStructures;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StructureBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.test.StructureTestUtil;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.CaveSurface;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

public class LightrootFeature  extends Feature<LightrootFeatureConfig> {
    public LightrootFeature(Codec<LightrootFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<LightrootFeatureConfig> context) {
        StructureWorldAccess structureWorldAccess = context.getWorld();
        BlockPos blockPos = context.getOrigin();
        LightrootFeatureConfig lightrootFeatureConfig = context.getConfig();
        Random random = context.getRandom();
        Optional<CaveSurface> optional = CaveSurface.create(structureWorldAccess, blockPos, lightrootFeatureConfig.floorToCeilingSearchRange, LightrootHelper::canGenerate, LightrootHelper::canReplaceOrLava);
        if (optional.isPresent() && optional.get() instanceof CaveSurface.Bounded bounded) {
            if (bounded.getHeight() < 10) {
                return false;
            } else {
                LightrootFeature.LightrootGenerator lightrootGenerator = createGenerator(blockPos.withY(bounded.getFloor() + 1), random);
                boolean bl = lightrootGenerator.canGenerate(structureWorldAccess);
                if (bl) {
                    lightrootGenerator.generate(structureWorldAccess, random);
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    private static LightrootFeature.LightrootGenerator createGenerator(BlockPos pos, Random random) {
        return new LightrootFeature.LightrootGenerator(pos, random);
    }

    static final class LightrootGenerator {
        private BlockPos pos;
        private Random random;

        LightrootGenerator(BlockPos pos, Random random) {
            this.pos = pos;
            this.random = random;
        }

        boolean canGenerate(StructureWorldAccess world) {
            BlockPos.Mutable mutable = this.pos.mutableCopy();

            mutable.move(Direction.UP);

            Box box = (new Box(mutable)).expand(1);

            Stream<BlockPos> stream = BlockPos.stream(box);
            Iterator<BlockPos> blockPosList = stream.iterator();

            while(blockPosList.hasNext()) {
                BlockPos blockPos = blockPosList.next();
                if (!LightrootHelper.canGenerateBase(world, blockPos)) {
                    return false;
                }
            }

            this.pos = mutable.move(Direction.DOWN);
            return true;
        }

        void generate(StructureWorldAccess world, Random random) {
            BlockPos.Mutable mutable = this.pos.mutableCopy();
            if (LightrootHelper.canGenerate(world, mutable)) {
                /**Block goldBlock = Blocks.GOLD_BLOCK;
                int height = 20;
                for(double t = 0; t < height; t += 0.2){
                    int x = (int) Math.round(10 * Math.sin(t) - 3);
                    int y = (int) Math.round(3 * t);
                    int z = (int) Math.round(10 * Math.cos(t));
                    int radius = t > 15 ? 1 : 2;
                    double percentageHeight = t / height;
                    for (int x1 = -radius; x1 <= radius; x1++) {
                        for (int y1 = -radius; y1 <= radius; y1++) {
                            for (int z1 = -radius; z1 <= radius; z1++) {
                                double relativeDistance = Math.sqrt(Math.pow(x1, 2) + Math.pow(y1, 2) + Math.pow(z1, 2));
                                if(relativeDistance <= radius){
                                    boolean bl = Math.abs(x1) >= radius || Math.abs(y1) >= radius || Math.abs(z1) >= relativeDistance;
                                    double chanceForRoots = bl ? Math.max(0.5, percentageHeight) : 1;
                                    double chanceForAir = bl ? (percentageHeight > 0.7 ? 0.2 : 0) : 0;
                                    BlockState blockState = Math.random() > chanceForRoots       ? Blocks.MUDDY_MANGROVE_ROOTS.getDefaultState() : Blocks.MANGROVE_ROOTS.getDefaultState();
                                    BlockState blockState1 = Math.random() > chanceForAir ? blockState : Blocks.AIR.getDefaultState();
                                    BlockPos.Mutable mutable1 = mutable.mutableCopy();
                                    world.setBlockState(mutable1.add(x + x1, y + y1, z + z1), Blocks.GOLD_BLOCK.getDefaultState(), 3);
                                }
                            }
                        }
                    }
                }**/

                world.setBlockState(mutable, Blocks.GOLD_BLOCK.getDefaultState(), 1);
                GenerateStructure.generateStructure(world, World.OVERWORLD, ModStructures.LIGHTROOT, mutable);
            }
        }
    }
}
