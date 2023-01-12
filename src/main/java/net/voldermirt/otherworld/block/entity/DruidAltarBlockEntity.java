package net.voldermirt.otherworld.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.voldermirt.otherworld.screen.DruidAltarScreenHandler;
import org.jetbrains.annotations.Nullable;

public class DruidAltarBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    public static final int INVENTORY_SIZE = 6;
    public static final int PROPERTY_DELEGATE_AMOUNT = 2;

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);

    protected final PropertyDelegate propertyDelegate;
    /*
    Properties like progress, etc.
    They go here
    Make sure to set them up properly in the constructor,
    and in the read/writeNbt() methods
     */
    private int current_energy = 0;
    private int max_energy = 64;

    public DruidAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DRUID_ALTAR, pos, state);
        this.propertyDelegate = new PropertyDelegate() {

            @Override
            public int get(int index) {
                switch (index) {
                    case 0: return DruidAltarBlockEntity.this.current_energy;
                    case 1: return DruidAltarBlockEntity.this.max_energy;
                    default: return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: DruidAltarBlockEntity.this.current_energy = value; break;
                    case 1: DruidAltarBlockEntity.this.max_energy = value; break;
                }
            }

            @Override
            public int size() {
                return PROPERTY_DELEGATE_AMOUNT;
            }
        };
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.otherworld.druid_altar.title");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new DruidAltarScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, DruidAltarBlockEntity entity) {


    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("druid_altar.current_energy", current_energy);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        current_energy = nbt.getInt("druid_altar.current_progress");
     }
}
