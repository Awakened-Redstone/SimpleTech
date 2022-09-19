package com.awakenedredstone.simpletech.client;

import com.awakenedredstone.simpletech.SimpleTech;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class RenderUtil {
    public static void renderOutline(World world, BlockState state, BlockPos pos) {
        Box box = getBoundingBox(state, pos).shrink(0.01, 0.01, 0.01).shrink(-0.01, -0.01, -0.01);

        PacketByteBuf buf = PacketByteBufs.create();
        NbtCompound compound = new NbtCompound();
        compound.putLong("id", pos.asLong());
        compound.putInt("time", 5);
        compound.putInt("color", 0x40FF0000);
        compound.putBoolean("disableDepthTest", false);
        NbtList from = new NbtList();
        from.add(NbtDouble.of(box.minX));
        from.add(NbtDouble.of(box.minY));
        from.add(NbtDouble.of(box.minZ));
        NbtList to = new NbtList();
        to.add(NbtDouble.of(box.maxX));
        to.add(NbtDouble.of(box.maxY));
        to.add(NbtDouble.of(box.maxZ));
        compound.put("from", from);
        compound.put("to", to);
        buf.writeNbt(compound);
        world.getServer().getPlayerManager().getPlayerList().forEach(player -> {
            ServerPlayNetworking.send(player, new Identifier(SimpleTech.MOD_ID, "render"), buf);
        });
    }

    public static Box getBoundingBox(BlockState state, BlockPos pos) {
        int size = 64;
        int side = size / 2;
        Direction dir = state.get(Properties.HORIZONTAL_FACING);
        Vec3i dirVector = dir.getVector();
        int x = dirVector.getX(), y = dirVector.getY(), z = dirVector.getZ();
        Box box = new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).offset(-x, -y, -z);
        switch (dir) {
            case NORTH -> {
                box = box.stretch(-side, -side, 0);
                box = box.stretch(side, side, size);
            }
            case SOUTH -> {
                box = box.stretch(-side, -side, -size);
                box = box.stretch(side, side, 0);
            }
            case EAST -> {
                box = box.stretch(-size, -side, -side);
                box = box.stretch(0, side, side);
            }
            case WEST -> {
                box = box.stretch(0, -side, -side);
                box = box.stretch(size, side, side);
            }
        }
        return box;
    }
}
