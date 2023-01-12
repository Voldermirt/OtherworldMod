package net.voldermirt.otherworld.mixin;

import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.voldermirt.otherworld.item.custom.NatureItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements FabricItemStack {

    @Shadow
    public abstract int getDamage();
    @Shadow
    public abstract int getMaxDamage();
    @Shadow
    public abstract Item getItem();

    @Inject(method="getTooltip", at=@At("RETURN"), cancellable = true)
    public void onGetTooltip(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
        List<Text> list = cir.getReturnValue();

        if (!(getItem() instanceof NatureItem)) { return; }

        for (Text t : list) {
            if (t.toString().contains("item.durability")) {
                list.remove(t);
                break;
            }
        }

        list.add(Text.translatable("item.otherworld.energy",
                getMaxDamage() - getDamage(), getMaxDamage()).formatted(Formatting.GREEN));
        cir.setReturnValue(list);

    }

}
