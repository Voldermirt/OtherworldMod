package net.voldermirt.otherworld.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.voldermirt.otherworld.OtherworldMod;

public class GrassWandItem extends NatureItem {

    public GrassWandItem(Settings settings, int maxEnergy) {
        super(settings, maxEnergy);
    }
    public GrassWandItem(Settings settings) {
        this(settings, 64);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        BlockState block = ctx.getWorld().getBlockState(ctx.getBlockPos());
        OtherworldMod.LOGGER.debug(block.getBlock().getTranslationKey());

        if (block.isOf(Blocks.DIRT) && canUse(ctx.getStack(), 1)) {
            ctx.getWorld().setBlockState(ctx.getBlockPos(), Blocks.GRASS_BLOCK.getDefaultState());

            reduceEnergy(1, ctx);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

}
