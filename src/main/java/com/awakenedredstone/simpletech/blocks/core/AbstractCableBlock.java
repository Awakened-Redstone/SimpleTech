package com.awakenedredstone.simpletech.blocks.core;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class AbstractCableBlock extends Block {
    private static final BooleanProperty NORTH = BooleanProperty.of("north");
    private static final BooleanProperty EAST = BooleanProperty.of("east");
    private static final BooleanProperty SOUTH = BooleanProperty.of("south");
    private static final BooleanProperty WEST = BooleanProperty.of("west");
    private static final BooleanProperty UP = BooleanProperty.of("up");
    private static final BooleanProperty DOWN = BooleanProperty.of("down");

    private static final VoxelShape SHAPE_CORE = Block.createCuboidShape(6, 6, 6, 10, 10, 10);
    private static final VoxelShape SHAPE_NORTH = Block.createCuboidShape(6, 6, 0, 10, 10, 6);
    private static final VoxelShape SHAPE_EAST = Block.createCuboidShape(10, 6, 6, 16, 10, 10);
    private static final VoxelShape SHAPE_SOUTH = Block.createCuboidShape(6, 6, 10, 10, 10, 16);
    private static final VoxelShape SHAPE_WEST = Block.createCuboidShape(0, 6, 6, 6, 10, 10);
    private static final VoxelShape SHAPE_UP = Block.createCuboidShape(6, 10, 6, 10, 16, 10);
    private static final VoxelShape SHAPE_DOWN = Block.createCuboidShape(6, 0, 6, 10, 6, 10);

    public AbstractCableBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState(getDefaultState()));
    }

    private BlockState getDefaultState(final BlockState state) {
        return state
                .with(NORTH, false)
                .with(EAST, false)
                .with(SOUTH, false)
                .with(WEST, false)
                .with(UP, false)
                .with(DOWN, false)
                .with(Properties.WATERLOGGED, false);
    }

    /*@Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShape(state);
    }

    private VoxelShape getShape(final BlockState state) {
        VoxelShape shape = SHAPE_CORE;
        if (Boolean.TRUE.equals(state.get(NORTH))) {
            shape = VoxelShapes.union(shape, SHAPE_NORTH);
        }
        if (Boolean.TRUE.equals(state.get(EAST))) {
            shape = VoxelShapes.union(shape, SHAPE_EAST);
        }
        if (Boolean.TRUE.equals(state.get(SOUTH))) {
            shape = VoxelShapes.union(shape, SHAPE_SOUTH);
        }
        if (Boolean.TRUE.equals(state.get(WEST))) {
            shape = VoxelShapes.union(shape, SHAPE_WEST);
        }
        if (Boolean.TRUE.equals(state.get(UP))) {
            shape = VoxelShapes.union(shape, SHAPE_UP);
        }
        if (Boolean.TRUE.equals(state.get(DOWN))) {
            shape = VoxelShapes.union(shape, SHAPE_DOWN);
        }
        return shape;
    }

    @Override
    public BlockState getPlacementState(final ItemPlacementContext ctx) {
        final BlockState stateWithDirection = Objects.requireNonNull(super.getPlacementState(ctx));
        final Direction direction = getDirection(stateWithDirection);
        return getState(
                stateWithDirection,
                ctx.getLevel(),
                ctx.getClickedPos(),
                direction
        );
    }

    private BlockState getState(final BlockState currentState,
                                final WorldAccess world,
                                final BlockPos pos,
                                @Nullable final Direction blacklistedDirection) {
        final boolean north = hasVisualConnection(world, pos, Direction.NORTH, blacklistedDirection);
        final boolean east = hasVisualConnection(world, pos, Direction.EAST, blacklistedDirection);
        final boolean south = hasVisualConnection(world, pos, Direction.SOUTH, blacklistedDirection);
        final boolean west = hasVisualConnection(world, pos, Direction.WEST, blacklistedDirection);
        final boolean up = hasVisualConnection(world, pos, Direction.UP, blacklistedDirection);
        final boolean down = hasVisualConnection(world, pos, Direction.DOWN, blacklistedDirection);

        return currentState
                .with(NORTH, north)
                .with(EAST, east)
                .with(SOUTH, south)
                .with(WEST, west)
                .with(UP, up)
                .with(DOWN, down);
    }

    private boolean hasVisualConnection(final WorldAccess world,
                                        final BlockPos pos,
                                        final Direction direction,
                                        @Nullable final Direction blacklistedDirection) {
        if (direction == blacklistedDirection) {
            return false;
        }
        final BlockPos offsetPos = pos.offset(direction);
        if (!(world.getBlockEntity(offsetPos) instanceof PlatformNetworkNodeContainer container)) {
            return false;
        }
        return container.canAcceptIncomingConnection(direction);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        getState(state, world, pos, getDirection(state));
    }

    @Nullable
    public T getDirection(@Nullable final BlockState state) {
        final EnumProperty<T> directionProperty = getDirectionType().getProperty();
        return state != null && state.hasProperty(directionProperty)
                ? state.getValue(directionProperty)
                : null;
    }*/
}
