package net.voldermirt.otherworld.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;

import java.util.function.Consumer;

public class ModAdvancementGen extends FabricAdvancementProvider {
    protected ModAdvancementGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
    }
}
