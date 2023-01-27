package net.voldermirt.otherworld.screen;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.voldermirt.otherworld.block.entity.DruidAltarBlockEntity;
import net.voldermirt.otherworld.item.custom.NatureItem;
import net.voldermirt.otherworld.networking.ModMessages;

public class DruidAltarScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    private final ScreenHandlerContext context;
    public final DruidAltarBlockEntity entity;


    public DruidAltarScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(buf.readBlockPos()),
                new ArrayPropertyDelegate(DruidAltarBlockEntity.PROPERTY_DELEGATE_AMOUNT), ScreenHandlerContext.EMPTY);
    }

    public DruidAltarScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity entity, PropertyDelegate delegate, ScreenHandlerContext ctx) {
        super(ModScreenHandlers.DRUID_ALTAR_SCREEN_HANDLER, syncId);
        checkSize(((Inventory) entity), DruidAltarBlockEntity.INVENTORY_SIZE);
        this.inventory = (Inventory) entity;
        inventory.onOpen(playerInventory.player);

        this.entity = (DruidAltarBlockEntity) entity;
        this.context = ctx;
        this.propertyDelegate = delegate;

        // Slots
        this.addSlot(new Slot(inventory, 0, 44, 34));
        this.addSlot(new Slot(inventory, 1, 24, 14));
        this.addSlot(new Slot(inventory, 2, 64, 14));
        this.addSlot(new Slot(inventory, 3, 24, 54));
        this.addSlot(new Slot(inventory, 4, 64, 54));
        this.addSlot(new Slot(inventory, 5, 112, 34));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addProperties(delegate);


    }


    public void infuseItem() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(entity.getPos());


        ClientPlayNetworking.send(ModMessages.INFUSE_ITEM_ID, buf);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int p_slot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(p_slot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (p_slot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }


    public int getScaledEnergy() {
        int energy = entity.getEnergy();
        int maxEnergy = entity.getMaxEnergy();
        int barSize = 64;

        System.out.println(energy);

        return maxEnergy != 0 && energy != 0 ? energy * barSize / maxEnergy : 0;
    }

    public void close(PlayerEntity player) {
        super.close(player);
        this.context.run((world, pos) -> {
            if (entity.getIsOutputFromCrafting()) {
                entity.setStack(5, ItemStack.EMPTY);
            }
                this.dropInventory(player, inventory);
        });
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }
}
