package net.voldermirt.otherworld.block.custom;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.voldermirt.otherworld.OtherworldMod;
import org.jetbrains.annotations.Nullable;

public class OtherworldPortalBlock extends HorizontalFacingBlock {

    public static final BooleanProperty OPEN = Properties.OPEN;
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    private final SoundEvent closeSound = SoundEvents.BLOCK_WOODEN_TRAPDOOR_CLOSE;
    private final SoundEvent openSound = SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN;

    public OtherworldPortalBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(OPEN, false).with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(OPEN, FACING);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        state = state.cycle(OPEN);
        world.setBlockState(pos, state, 2);

        world.playSound(player, pos, state.get(OPEN).booleanValue() ? this.openSound : this.closeSound, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.1F + 0.9F);
        world.emitGameEvent(player, state.get(OPEN).booleanValue() ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        return ActionResult.success(world.isClient);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!state.get(OPEN).booleanValue()) {
            return;
        }

        if (world instanceof ServerWorld) {
            System.out.println(world.getRegistryKey().getValue());
            System.out.println(OtherworldMod.THE_OTHERWORLD.getValue());
        }

        if (world instanceof ServerWorld && !entity.hasVehicle() && !entity.hasPassengers() && entity.canUsePortals() && VoxelShapes.matchesAnywhere(VoxelShapes.cuboid(entity.getBoundingBox().offset((double)(-pos.getX()), (double)(-pos.getY()), (double)(-pos.getZ()))), state.getOutlineShape(world, pos), BooleanBiFunction.AND)) {
            RegistryKey<World> registryKey = world.getRegistryKey() == OtherworldMod.THE_OTHERWORLD ? World.OVERWORLD : OtherworldMod.THE_OTHERWORLD;
            ServerWorld serverWorld = ((ServerWorld)world).getServer().getWorld(registryKey);
            if (serverWorld == null) {
                return;
            }

            //entity.moveToWorld(serverWorld);
            FabricDimensions.teleport(entity, serverWorld, getTeleportTarget(serverWorld, entity));

        }
    }

    private TeleportTarget getTeleportTarget(ServerWorld world, Entity entity) {
        Vec3d pos = entity.getPos();

        Random random = world.getRandom();
        pos = pos.add(random.nextBetweenExclusive(-51, 51), 0, random.nextBetweenExclusive(-51, 51));

        pos.add(0, world.getTopY(Heightmap.Type.WORLD_SURFACE, (int)pos.x, (int)pos.z), 0);

        return new TeleportTarget(pos, entity.getVelocity(), entity.getYaw(), entity.getPitch());
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return !state.get(OPEN).booleanValue() ? state.getOutlineShape(world, pos) : VoxelShapes.empty();
    }
}
