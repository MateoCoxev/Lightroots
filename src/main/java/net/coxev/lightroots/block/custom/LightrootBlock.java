package net.coxev.lightroots.block.custom;

import net.coxev.lightroots.block.entity.LightrootBlockEntity;
import net.coxev.lightroots.block.entity.LightrootCoreBlockEntity;
import net.coxev.lightroots.block.entity.ModBlockEntities;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class LightrootBlock extends BlockWithEntity {

    public static final BooleanProperty LIT = Properties.LIT;

    public LightrootBlock() {
        super(FabricBlockSettings.copyOf(Blocks.SHROOMLIGHT).luminance(Blocks.createLightLevelFromLitBlockState(15)));
        this.setDefaultState(this.getDefaultState().with(LIT, false));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LightrootBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.LIGHTROOT_BLOCK_ENTITY, LightrootBlockEntity::tick);
    }
}
