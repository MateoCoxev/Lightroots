package net.coxev.lightroots.world.feature;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;

public class LightrootHelper {
    public LightrootHelper() {
    }

    protected static boolean canGenerateBase(StructureWorldAccess world, BlockPos pos) {
        return canGenerate(world, pos);
    }

    protected static boolean canGenerate(WorldAccess world, BlockPos pos) {
        return world.testBlockState(pos, LightrootHelper::canGenerate);
    }

    public static boolean canReplaceOrLava(BlockState state) {
        return canReplace(state) || state.isOf(Blocks.LAVA);
    }

    public static boolean canReplace(BlockState state) {
        return state.isOf(Blocks.DRIPSTONE_BLOCK) || state.isIn(BlockTags.DRIPSTONE_REPLACEABLE_BLOCKS);
    }

    public static boolean canGenerate(BlockState state) {
        return state.isAir();
    }
}
