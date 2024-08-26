package net.coxev.lightroots.world.feature;

import com.mojang.serialization.Codec;
import net.coxev.lightroots.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StructureBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
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
                Block structureBlock = Blocks.STRUCTURE_BLOCK;
                Block redstoneBlock = Blocks.REDSTONE_BLOCK;
                world.setBlockState(mutable.move(Direction.DOWN), structureBlock.getDefaultState(), 3);
                BlockEntityType<StructureBlockBlockEntity> structureBlockEntity = BlockEntityType.STRUCTURE_BLOCK;
                structureBlockEntity.get(world, mutable).setTemplateName("lightroots:regular_lightroot/bottom_1");
                structureBlockEntity.get(world, mutable).setOffset(new BlockPos(-5, 0, -5));
                world.setBlockState(mutable.move(Direction.UP), redstoneBlock.getDefaultState(), 3);
            }
        }
    }
}
