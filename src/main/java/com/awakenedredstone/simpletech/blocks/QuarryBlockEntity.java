package com.awakenedredstone.simpletech.blocks;

import com.awakenedredstone.simpletech.ModBlockEntities;
import com.awakenedredstone.simpletech.SimpleTech;
import com.awakenedredstone.simpletech.client.RenderUtil;
import com.awakenedredstone.simpletech.client.rendering.QuarryScreen;
import com.awakenedredstone.simpletech.client.rendering.QuarryScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class QuarryBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
    private double x, y, z;

    public QuarryBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.QUARRY_BLOCK_ENTITY, pos, state);
        Box boundingBox = RenderUtil.getBoundingBox(state, pos);
        x = boundingBox.minX;
        y = boundingBox.maxY;
        z = boundingBox.minZ;
    }

    public static void tick(World world, BlockPos pos, BlockState state, QuarryBlockEntity blockEntity, QuarryBlock block) {
        if (!world.isClient && world.getServer() != null) {
            RenderUtil.renderOutline(world, state, pos);
            Box boundingBox = RenderUtil.getBoundingBox(state, pos);
            if (blockEntity.x < boundingBox.maxX) {
                if (blockEntity.y >= boundingBox.minY) {
                    int limit = 16;
                    for (; blockEntity.z < boundingBox.maxZ; blockEntity.z++) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        NbtCompound compound = new NbtCompound();
                        compound.putLong("id", new Random().nextLong());
                        compound.putInt("time", 5);
                        compound.putInt("color", 0x8000FF00);
                        compound.putBoolean("disableDepthTest", false);
                        NbtList from = new NbtList();
                        from.add(NbtDouble.of(blockEntity.x));
                        from.add(NbtDouble.of(blockEntity.y));
                        from.add(NbtDouble.of(blockEntity.z));
                        NbtList to = new NbtList();
                        to.add(NbtDouble.of(blockEntity.x + 1));
                        to.add(NbtDouble.of(blockEntity.y + 1));
                        to.add(NbtDouble.of(blockEntity.z + 1));
                        compound.put("from", from);
                        compound.put("to", to);
                        buf.writeNbt(compound);
                        world.getServer().getPlayerManager().getPlayerList().forEach(player -> {
                            ServerPlayNetworking.send(player, new Identifier(SimpleTech.MOD_ID, "render"), buf);
                        });
                        BlockPos blockPos = new BlockPos(blockEntity.x, blockEntity.y, blockEntity.z);
                        if (world.getBlockState(blockPos).getBlock().getHardness() != -1) {
                            world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS, 0);
                            Block.dropStacks(world.getBlockState(pos), world, pos, blockEntity, null, ItemStack.EMPTY);
                        }
                        //blockEntity.z++;
                        if (limit-- == 0) break;
                    }
                    if (blockEntity.z == boundingBox.maxZ) {
                        blockEntity.z = boundingBox.minZ;
                        blockEntity.y--;
                    }
                }
                if (blockEntity.y < boundingBox.minY || blockEntity.y < world.getBottomY()) {
                    blockEntity.y = boundingBox.maxY;
                    blockEntity.x++;
                }
            } else {
                blockEntity.x = boundingBox.minX;
                blockEntity.y = boundingBox.maxY;
                blockEntity.z = boundingBox.minZ;
            }
        }
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Test");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new QuarryScreenHandler(syncId, inv);
    }
}
