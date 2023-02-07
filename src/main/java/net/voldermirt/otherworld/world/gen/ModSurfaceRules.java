package net.voldermirt.otherworld.world.gen;


import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

public class ModSurfaceRules {

    private static MaterialRules.MaterialRule BEDROCK = makeStateRule(Blocks.BEDROCK);

    private static MaterialRules.MaterialRule makeStateRule(Block block) {
        return MaterialRules.block(block.getDefaultState());
    }

    public static MaterialRules.MaterialRule getSurfaceRules() {

        //TODO: Finish this


        ImmutableList.Builder<MaterialRules.MaterialRule> builder = ImmutableList.builder();
        builder
                .add(MaterialRules.condition(MaterialRules.verticalGradient("bedrock_floor", YOffset.BOTTOM, YOffset.aboveBottom(5)), BEDROCK));

        return MaterialRules.sequence(builder.build().toArray(MaterialRules.MaterialRule[]::new));
    }

}
