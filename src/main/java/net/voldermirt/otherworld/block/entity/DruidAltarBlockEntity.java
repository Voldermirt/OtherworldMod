package net.voldermirt.otherworld.block.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.voldermirt.otherworld.item.custom.NatureItem;
import net.voldermirt.otherworld.networking.ModMessages;
import net.voldermirt.otherworld.recipe.DruidAltarRecipe;
import net.voldermirt.otherworld.screen.DruidAltarScreenHandler;
import net.voldermirt.otherworld.util.BlockMatcher;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DruidAltarBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {

    public static final int INVENTORY_SIZE = 6;
    public static final int PROPERTY_DELEGATE_AMOUNT = 2;

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
    protected final PropertyDelegate propertyDelegate;

    private boolean isCompleteRecipePresent = false;
    private boolean isOutputFromCrafting = false;
    private DruidAltarScreenHandler handler;

    private BlockMatcher natureBlockMatcher = new BlockMatcher();
    private static final List<BlockPos> SEARCH_OFFSETS = BlockPos.stream(-2, -2, -2, 2, 2, 2)
            .filter((pos) -> !(pos.getX() == 0 && pos.getY() == 0 && pos.getZ() == 0)).map(BlockPos::toImmutable).toList();

    private int ticksPerEnergy = 200;
    private int energyTimer = ticksPerEnergy;

    private final Timer checkEnergyTicks;

    /*
    Properties like progress, etc.
    They go here
    Make sure to set them up properly in the constructor,
    and in the read/writeNbt() methods
     */
    private int currentEnergy = 0;
    private int maxEnergy = 64;

    public DruidAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DRUID_ALTAR, pos, state);


        this.propertyDelegate = new PropertyDelegate() {

            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> DruidAltarBlockEntity.this.currentEnergy;
                    case 1 -> DruidAltarBlockEntity.this.maxEnergy;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> DruidAltarBlockEntity.this.currentEnergy = value;
                    case 1 -> DruidAltarBlockEntity.this.maxEnergy = value;
                }
            }

            @Override
            public int size() {
                return PROPERTY_DELEGATE_AMOUNT;
            }
        };

        // Initialize BlockMatcher
        natureBlockMatcher.addTags(
                BlockTags.FLOWERS,
                BlockTags.LOGS,
                BlockTags.CROPS,
                BlockTags.CORALS,
                BlockTags.LEAVES,
                BlockTags.NYLIUM,
                BlockTags.SAPLINGS
        );

        natureBlockMatcher.addMaterials(
                Material.UNDERWATER_PLANT,
                Material.BAMBOO,
                Material.BAMBOO_SAPLING,
                Material.CACTUS,
                Material.GOURD,
                Material.LEAVES,
                Material.MOSS_BLOCK,
                Material.ORGANIC_PRODUCT,
                Material.PLANT,
                Material.REPLACEABLE_PLANT,
                Material.SOLID_ORGANIC,
                Material.UNDERWATER_PLANT
        );

        natureBlockMatcher.addBlocks(
                Blocks.GRASS_BLOCK
        );

        checkEnergyTicks = new Timer("checkEnergyTicks");
        checkEnergyTicks.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ticksPerEnergy = getTicksPerEnergy();
            }
        }, 1000, 1000);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.otherworld.druid_altar.title");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        DruidAltarScreenHandler handler = new DruidAltarScreenHandler(syncId, inv, this, this.propertyDelegate, ScreenHandlerContext.create(getWorld(), getPos()));
        syncEnergy(currentEnergy);
        return handler;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }


    public static void tick(World world, BlockPos blockPos, BlockState blockState, DruidAltarBlockEntity entity) {


        if (world.isClient()) return;

        SimpleInventory inv = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inv.setStack(i, entity.getStack(i));
        }


        if (hasRecipe(entity)) {

            if (entity.getStack(5).isEmpty()) {
                if (entity.isCompleteRecipePresent) {
                    finalizeCrafting(entity);
                    markDirty(world, blockPos, blockState);
                } else {

                    if (isOutputAvailable(inv)) {
                        Optional<DruidAltarRecipe> recipe = entity.getWorld().getRecipeManager()
                                .getFirstMatch(DruidAltarRecipe.Type.SINGLETON, inv, entity.getWorld());
                        entity.setStack(5, recipe.get().getOutput());
                        entity.getStack(5).setDamage(entity.getStack(5).getMaxDamage() - 1);
                        entity.isCompleteRecipePresent = true;
                        entity.isOutputFromCrafting = true;
                        markDirty(world, blockPos, blockState);
                    }
                }
            }

        } else {

            if (entity.isCompleteRecipePresent && entity.isOutputFromCrafting) {
                entity.setStack(5, ItemStack.EMPTY);
                markDirty(world, blockPos, blockState);
                entity.isOutputFromCrafting = false;
            }
            entity.isCompleteRecipePresent = false;

        }


        //Increase energy levels

        if (entity.ticksPerEnergy == 0) {
            return;
        }
        --entity.energyTimer;
        if (entity.energyTimer <= 0) {
            entity.energyTimer = entity.ticksPerEnergy;
            if (entity.getEnergy() < entity.getMaxEnergy() && entity.ticksPerEnergy > 0) {
                entity.setEnergy(entity.getEnergy() + 1);
            }
        }
    }

    private int getTicksPerEnergy() {
        float k = 0.16f;
        float energyPerSecond = k * getNatureBlockRatio();
        if (energyPerSecond == 0) {
            return 0;
        }

        float secondsPerEnergy = 1 / energyPerSecond;

        return (int)secondsPerEnergy * 20;
    }

    private float getNatureBlockRatio() {
        float maxBlocks = 124.0f;
        Iterator nearbyBlocks = SEARCH_OFFSETS.iterator();

        int blocksFound = 0;
        Set<Block> uniqueBlocks = new HashSet<>();
        while (nearbyBlocks.hasNext()) {
            BlockPos pos = (BlockPos)nearbyBlocks.next();

            BlockState block = world.getBlockState(getPos().add(pos.getX(), pos.getY(), pos.getZ()));
            if (natureBlockMatcher.matches(block)) {
                ++blocksFound;
                uniqueBlocks.add(block.getBlock());
            }
        }

        return (blocksFound + (0.5f * uniqueBlocks.size())) / maxBlocks;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("druid_altar.current_energy", getEnergy());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        int energy = nbt.getInt("druid_altar.current_energy");
        setEnergy(energy);
     }

    private static void finalizeCrafting(DruidAltarBlockEntity entity) {
        if (!entity.isOutputFromCrafting) return;

        for (int i = 0; i < 5; i++) {
            // Decrease ingredients by 1
            entity.setStack(i, new ItemStack(entity.getStack(i).getItem(), entity.getStack(i).getCount() - 1));
        }
        entity.isCompleteRecipePresent = false;
        entity.isOutputFromCrafting = false;


        //TODO: Effects (e.g. sound)
     }

    public void infuseItem() {

        finalizeCrafting(this);


        ItemStack stack = getStack(5);
        if (!(stack.getItem() instanceof NatureItem)) {
            markDirty();
            return;
        }


        int stackEnergy = stack.getMaxDamage() - stack.getDamage();
        int maxStackEnergy = stack.getMaxDamage();

        if (getEnergy() <= 0 || stackEnergy >= maxStackEnergy)  {
            markDirty();
            return;
        }


        int neededEnergy = maxStackEnergy - stackEnergy;
        if (getEnergy() >= neededEnergy) {
            stack.setDamage(0);
            setEnergy(getEnergy() - neededEnergy);
        } else {
            stack.setDamage(stack.getDamage() - getEnergy());
            setEnergy(0);
        }


        markDirty();
         world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
     }

    private static boolean hasRecipe(DruidAltarBlockEntity entity) {
         SimpleInventory inv = new SimpleInventory(entity.size());
         for (int i = 0; i < entity.size(); i++) {
             inv.setStack(i, entity.getStack(i));
         }

         Optional<DruidAltarRecipe> match = entity.getWorld().getRecipeManager()
                 .getFirstMatch(DruidAltarRecipe.Type.SINGLETON, inv, entity.getWorld());

         return match.isPresent();
     }

    private static boolean isOutputAvailable(SimpleInventory inv) {
        return inv.getStack(5).isEmpty();
     }


    public void setEnergy(int energy) {
        propertyDelegate.set(0, energy);
        markDirty();

        syncEnergy(energy);
    }

    private void syncEnergy(int energy) {
        if (world != null && !world.isClient()) {
            PacketByteBuf data = PacketByteBufs.create();
            data.writeInt(energy);
            data.writeBlockPos(pos);

            for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld)world, getPos())) {
                ServerPlayNetworking.send(player, ModMessages.ENERGY_SYNC_ID, data);
            }
        }
    }


    public int getEnergy() {
        return propertyDelegate.get(0);
    }

    public void setMaxEnergy(int maxEnergy) {
        propertyDelegate.set(1, maxEnergy);
    }

    public int getMaxEnergy() {
        return propertyDelegate.get(1);
    }

    public boolean getIsOutputFromCrafting() {
        return this.isOutputFromCrafting;
    }
}
