package net.voldermirt.otherworld.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;
import java.util.Arrays;

public class BlockMatcher {

    private ArrayList<Block> specificBlocks = new ArrayList<>();
    private ArrayList<TagKey<Block>> blockTags = new ArrayList<>();
    private ArrayList<Material> blockMaterials = new ArrayList<>();

    public BlockMatcher() {
    }

    public boolean matches(BlockState block) {
        for (Block b : specificBlocks) {
            if (block.isOf(b)) {
                return true;
            }
        }

        for (TagKey<Block> tag : blockTags) {
            if (block.isIn(tag)) {
                return true;
            }
        }

        for (Material mat : blockMaterials) {
            if (block.getMaterial() == mat) {
                return true;
            }
        }

        return false;
    }

    public void addBlocks(Block...blocks) {
        specificBlocks.addAll(Arrays.asList(blocks));
    }

    public void addTags(TagKey<Block>...tags) {
        blockTags.addAll(Arrays.asList(tags));
    }

    public void addMaterials(Material...mats) {
        blockMaterials.addAll(Arrays.asList(mats));
    }


}
