package net.coxev.lightroots.block.custom;

import net.coxev.lightroots.block.ModBlocks;
import net.coxev.lightroots.block.entity.LightrootCoreBlockEntity;
import net.coxev.lightroots.block.entity.ModBlockEntities;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class LightrootCoreBlock extends BlockWithEntity {

    public static final BooleanProperty LIT = Properties.LIT;

    public LightrootCoreBlock() {
        super(FabricBlockSettings.copyOf(Blocks.SHROOMLIGHT).luminance(Blocks.createLightLevelFromLitBlockState(15)));
        this.setDefaultState(this.getDefaultState().with(LIT, false));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LightrootCoreBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof LightrootCoreBlockEntity) {
                if(!((LightrootCoreBlockEntity) blockEntity).isActive()){
                    ((LightrootCoreBlockEntity) blockEntity).switchActive();
                    LightrootCoreBlockEntity.playSound(world, pos, SoundEvents.BLOCK_BEACON_ACTIVATE);
                }
                return ActionResult.CONSUME;
            }
            return ActionResult.CONSUME;
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.LIGHTROOT_CORE_BLOCK_ENTITY, LightrootCoreBlockEntity::tick);
    }
}
