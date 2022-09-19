package com.awakenedredstone.simpletech;

import com.awakenedredstone.simpletech.blocks.QuarryBlockEntity;
import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities implements AutoRegistryContainer<BlockEntityType<?>> {

    public static final BlockEntityType<QuarryBlockEntity> QUARRY_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(QuarryBlockEntity::new, ModBlocks.QUARRY).build();

    @Override
    public Registry<BlockEntityType<?>> getRegistry() {
        return Registry.BLOCK_ENTITY_TYPE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<BlockEntityType<?>> getTargetFieldType() {
        return (Class<BlockEntityType<?>>) (Object) BlockEntityType.class;
    }
}
