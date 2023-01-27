package net.voldermirt.otherworld.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;

public class NatureItem extends Item {

    public NatureItem(Settings settings, int maxEnergy) {
        super(settings.maxDamage(maxEnergy));
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        float t = Math.max(0.0F, ((float)this.getMaxDamage() - (float)stack.getDamage()) / (float)this.getMaxDamage());
        float g = (float)127.0 + ((float)128.0) * t;
        return MathHelper.packRgb(0, (int)g, 64);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    protected int getEnergy(ItemStack stack) {
        return stack.getMaxDamage() - stack.getDamage();
    }

    protected boolean canUse(ItemStack stack, int energyCost) {
        return (getEnergy(stack)) > energyCost;
    }

    protected void reduceEnergy(int amount, ItemUsageContext ctx) {
        ItemStack itemStack = ctx.getStack();
        PlayerEntity playerEntity = ctx.getPlayer();

        if (!(playerEntity instanceof ServerPlayerEntity)) { return; }

        itemStack.damage(Math.min(amount, getEnergy(itemStack) - 1), playerEntity, p -> {});
    }
}
