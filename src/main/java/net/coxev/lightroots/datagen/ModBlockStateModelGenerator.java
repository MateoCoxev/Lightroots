package net.coxev.lightroots.datagen;

import com.google.gson.JsonElement;
import net.coxev.lightroots.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModBlockStateModelGenerator extends BlockStateModelGenerator {
    public final Consumer<BlockStateSupplier> blockStateCollector;
    public final BiConsumer<Identifier, Supplier<JsonElement>> modelCollector;

    public ModBlockStateModelGenerator(Consumer<BlockStateSupplier> blockStateCollector, BiConsumer<Identifier, Supplier<JsonElement>> modelCollector, Consumer<Item> simpleItemModelExemptionCollector) {
        super(blockStateCollector, modelCollector, simpleItemModelExemptionCollector);
        this.blockStateCollector = blockStateCollector;
        this.modelCollector = modelCollector;
    }

    public void registerSimpleLitBlock(Block... blocks) {
        for (Block block : blocks) {
            Identifier identifier = TexturedModel.CUBE_ALL.upload(ModBlocks.LIGHTROOT, this.modelCollector);
            Identifier identifier2 = this.createSubModel(block, "_lit", Models.CUBE_ALL, TextureMap::all);
            this.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(createBooleanModelMap(Properties.LIT, identifier2, identifier)));
        }
    }
}
