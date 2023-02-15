package net.voldermirt.otherworld.block.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;

public class RowanLeavesBlock extends LeavesBlock {
    public RowanLeavesBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected boolean shouldDecay(BlockState state) {
        return !(Boolean)state.get(PERSISTENT) && (Integer)state.get(DISTANCE) == 9;
    }
}
