package net.coxev.lightroots.world.feature;

import com.mojang.serialization.Codec;
import net.coxev.lightroots.Lightroots;
import net.coxev.lightroots.util.ElegantPairing;
import net.coxev.lightroots.util.LightrootsArrayPersistentState;
import net.minecraft.block.Blocks;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.PersistentState;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.CaveSurface;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.rodofire.easierworldcreator.nbtutil.StructureUtil;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        LightrootsArrayPersistentState persistentState = structureWorldAccess.toServerWorld().getPersistentStateManager().getOrCreate(LightrootsArrayPersistentState::fromNbt, LightrootsArrayPersistentState::new, "lightroots_array");

        Optional<CaveSurface> optional = CaveSurface.create(structureWorldAccess, blockPos, lightrootFeatureConfig.floorToCeilingSearchRange, LightrootHelper::isAir, LightrootHelper::canReplaceOrLava);
        if (optional.isPresent() && optional.get() instanceof CaveSurface.Bounded bounded) {
            if (bounded.getHeight() < 10) {
                return false;
            } else {
                LightrootFeature.LightrootGenerator lightrootGenerator = createGenerator(blockPos.withY(bounded.getFloor() + 1), random, persistentState);
                boolean bl = lightrootGenerator.canGenerate(structureWorldAccess);
                if (bl) {
                    lightrootGenerator.generate(structureWorldAccess, random, persistentState);
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    private static LightrootFeature.LightrootGenerator createGenerator(BlockPos pos, Random random, LightrootsArrayPersistentState persistentState) {
        return new LightrootFeature.LightrootGenerator(pos, random, persistentState);
    }

    static final class LightrootGenerator {
        private BlockPos pos;
        private Random random;

        LightrootGenerator(BlockPos pos, Random random, LightrootsArrayPersistentState persistentState) {
            this.pos = pos;
            this.random = random;
        }

        boolean canGenerate(StructureWorldAccess world) {
            BlockPos.Mutable mutable = this.pos.mutableCopy();

            if (LightrootHelper.canGenerateBase(world, mutable)) {
                return true;
            }

            this.pos = mutable.move(Direction.DOWN);
            return false;
        }

        void generate(StructureWorldAccess world, Random random, LightrootsArrayPersistentState persistentState) {
            BlockPos.Mutable origin = this.pos.mutableCopy();

            if (LightrootHelper.canGenerate(world, origin)) {
                world.setBlockState(origin, Blocks.BEACON.getDefaultState(), 2);
                /**
                 * Tried something lol
                 */
                /**BlockState goldBlock = Blocks.GOLD_BLOCK.getDefaultState();
                int height = 20;
                double step = 0.2;
                int radius = 4;

                for(double t = 0; t < height; t += step){
                    int x = (int) Math.round(15 * Math.sin(t) - 3);
                    int y = (int) Math.round(3 * t);
                    int z = (int) Math.round(15 * Math.cos(t));
                    int localRadius = t > height * 0.8 ? radius - 1 : radius;
                    double percentageHeight = t / height;
                    BlockPos.Mutable currentVertebra = origin.mutableCopy();
                    currentVertebra.add(x, y, z);
                    for (int x1 = -localRadius; x1 <= localRadius; x1++) {
                        for (int y1 = -localRadius; y1 <= localRadius; y1++) {
                            for (int z1 = -localRadius; z1 <= localRadius; z1++) {
                                double factor = MathHelper.clamp((double) y / height, 0, 1);
                                double radiusFactor = MathHelper.clampedLerp(-0.5, 1, factor);
                                double delta = noise * 0.5; // 0-1 range
                                double amplifiedDelta = MathHelper.clampedLerp(0.2, 1, 1 - Math.pow(1 - delta, 5));
                                int localRadius = (int) (BlendingFunction.EaseInCirc.INSTANCE.apply(radiusFactor, 2, radius) * amplifiedDelta);

                                BlockPos.Mutable mutable1 = currentVertebra.mutableCopy();
                                mutable1.add(x1, y1, z1);

                                double relativeDistance = Math.sqrt(Math.pow(x1, 2) + Math.pow(y1, 2) + Math.pow(z1, 2));
                                if(mutable1.isWithinDistance(currentVertebra, localRadius)){
                                    boolean bl = Math.abs(x1) >= localRadius || Math.abs(y1) >= localRadius || Math.abs(z1) >= relativeDistance;
                                    double chanceForRoots = bl ? Math.max(0.5, percentageHeight) : 1;
                                    double chanceForAir = bl ? (percentageHeight > 0.7 ? 0.2 : 0) : 0;
                                    BlockState blockState = Math.random() > chanceForRoots ? Blocks.MUDDY_MANGROVE_ROOTS.getDefaultState() : Blocks.MANGROVE_ROOTS.getDefaultState();
                                    BlockState blockState1 = Math.random() > chanceForAir ? blockState : Blocks.AIR.getDefaultState();
                                    world.setBlockState(mutable1, goldBlock, 2);
                                }
                            }
                        }
                    }
                }**/

                BlockPos.Mutable structurePlacement = origin.mutableCopy();
                structurePlacement.move(Direction.DOWN);
                int bottomPieceId = (int) Math.floor(Math.random() * 3) + 1;
                int topPieceId = (int) Math.floor(Math.random() * 3) + 1;
                if(bottomPieceId == 1) StructureUtil.place(world, new Identifier(Lightroots.MOD_ID, "lightroot/bottom_1"), 1f, structurePlacement.add(-5, -1, -5), new BlockPos(0, 0, 0), BlockMirror.NONE, BlockRotation.NONE, true);
                if(bottomPieceId == 2) StructureUtil.place(world, new Identifier(Lightroots.MOD_ID, "lightroot/bottom_2"), 1f, structurePlacement.add(-5, -1, -5), new BlockPos(0, 0, 0), BlockMirror.NONE, BlockRotation.NONE, true);
                if(bottomPieceId == 3) StructureUtil.place(world, new Identifier(Lightroots.MOD_ID, "lightroot/bottom_3"), 1f, structurePlacement.add(-7, -1, -7), new BlockPos(0, 0, 0), BlockMirror.NONE, BlockRotation.NONE, true);
                if(topPieceId == 1) {
                    StructureUtil.place(world, new Identifier(Lightroots.MOD_ID, "lightroot/middle_1"), 1f, structurePlacement.add(-15, 16, -10), new BlockPos(0, 0, 0), BlockMirror.NONE, BlockRotation.NONE, true);
                    StructureUtil.place(world, new Identifier(Lightroots.MOD_ID, "lightroot/top_1"), 1f, structurePlacement.add(-7, 64, -5), new BlockPos(0, 0, 0), BlockMirror.NONE, BlockRotation.NONE, true);
                }
                if(topPieceId == 2) {
                    StructureUtil.place(world, new Identifier(Lightroots.MOD_ID, "lightroot/middle_2"), 1f, structurePlacement.add(-15, 16, -17), new BlockPos(0, 0, 0), BlockMirror.NONE, BlockRotation.NONE, true);
                    StructureUtil.place(world, new Identifier(Lightroots.MOD_ID, "lightroot/top_2"), 1f, structurePlacement.add(-15, 64, -9), new BlockPos(0, 0, 0), BlockMirror.NONE, BlockRotation.NONE, true);
                }
                if(topPieceId == 3) {
                    StructureUtil.place(world, new Identifier(Lightroots.MOD_ID, "lightroot/middle_3"), 1f, structurePlacement.add(-13, 16, -17), new BlockPos(0, 0, 0), BlockMirror.NONE, BlockRotation.NONE, true);
                    StructureUtil.place(world, new Identifier(Lightroots.MOD_ID, "lightroot/top_3"), 1f, structurePlacement.add(-16, 64, -11), new BlockPos(0, 0, 0), BlockMirror.NONE, BlockRotation.NONE, true);
                }
                origin.move(Direction.DOWN);
                int radius = 7;
                for(int x = -radius; x <= radius; x++){
                    for(int y = -2; y <= 1; y++){
                        for(int z = -radius; z <= radius; z++){
                            double distanceToCenter = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
                            if(distanceToCenter <= radius){
                                if(Math.random() > distanceToCenter / radius){
                                    if(world.getBlockState(origin.add(x, y, z)).isOf(Blocks.DEEPSLATE) || world.getBlockState(origin.add(x, y, z)).isOf(Blocks.STONE)){
                                        world.setBlockState(origin.add(x, y, z), Blocks.MOSS_BLOCK.getDefaultState(), 2);
                                        if(world.getBlockState(origin.add(x, y + 1, z)).isAir()){
                                            if(Math.random() < 0.5){
                                                world.setBlockState(origin.add(x, y + 1, z), Blocks.MOSS_CARPET.getDefaultState(), 2);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if(world.getBlockState(origin.add(0, 1, 0)).isAir()) world.setBlockState(origin.add(0, 1, 0), Blocks.MOSS_BLOCK.getDefaultState(), 2);
                persistentState.lightroots_array.add(ElegantPairing.pair(origin.getX(), origin.getZ()));
            }
        }
    }
}
