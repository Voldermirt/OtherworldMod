package net.voldermirt.otherworld.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.voldermirt.otherworld.block.entity.DruidAltarBlockEntity;
import net.voldermirt.otherworld.screen.DruidAltarScreenHandler;

import java.util.ArrayList;
import java.util.List;

public class DruidAltarRecipe implements Recipe<SimpleInventory> {

    private final Identifier id;
    private final ItemStack output;
    private final Ingredient primaryIngredient;
    private final DefaultedList<Ingredient> secondaryIngredients;


    public DruidAltarRecipe(Identifier id, ItemStack output, Ingredient primaryIngredient,
                            DefaultedList<Ingredient> secondaryIngredients) {
        this.id = id;
        this.output = output;
        this.primaryIngredient = primaryIngredient;
        this.secondaryIngredients = secondaryIngredients;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if (world.isClient()) return false;


        RecipeMatcher recipeMatcher = new RecipeMatcher();
        int i = 0;

        for(int j = 1; j < 5; ++j) {
            ItemStack itemStack = inventory.getStack(j);
            if (!itemStack.isEmpty()) {
                ++i;
                recipeMatcher.addInput(itemStack, 1);
            }
        }

        return i == numSecondaryIngredients() && recipeMatcher.match(this, (IntList)null) &&
                primaryIngredient.test(inventory.getStack(0));
    }

    private int numSecondaryIngredients() {
        int i = 0;
        for (Ingredient ing : secondaryIngredients) {
            if (!ing.isEmpty()) i++;
        }
        return i;
    }

    @Override
    public ItemStack craft(SimpleInventory inventory) {
        return output;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return output.copy();
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.SINGLETON;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.SINGLETON;
    }

    public Ingredient getPrimaryIngredient() {
        return primaryIngredient;
    }


    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return secondaryIngredients;
    }

    public static class Type implements RecipeType<DruidAltarRecipe> {
        private Type() {}
        public static final Type SINGLETON = new Type();
        public static final String ID = "druid_altar";
    }

    public static class Serializer implements RecipeSerializer<DruidAltarRecipe> {
        public static final Serializer SINGLETON = new Serializer();
        public static final String ID = "druid_altar";

        @Override
        public DruidAltarRecipe read(Identifier id, JsonObject json) {
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));


            Ingredient primaryInput = Ingredient.fromJson(JsonHelper.getObject(json, "primary_ingredient"));
            JsonArray secondaryIngredients = JsonHelper.getArray(json, "secondary_ingredients");
            DefaultedList<Ingredient> secondaryInputs = DefaultedList.ofSize(4, Ingredient.EMPTY);

            for (int i = 0; i < secondaryInputs.size(); i++) {
                if (i >= secondaryIngredients.size()) break;

                secondaryInputs.set(i, Ingredient.fromJson(secondaryIngredients.get(i)));
            }

            return new DruidAltarRecipe(id, output, primaryInput, secondaryInputs);
        }


        @Override
        public DruidAltarRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> secondaryInputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            Ingredient primaryInput = Ingredient.fromPacket(buf);

            for (int i = 0; i < secondaryInputs.size(); i++) {
                secondaryInputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();
            return new DruidAltarRecipe(id, output, primaryInput, secondaryInputs);
        }

        @Override
        public void write(PacketByteBuf buf, DruidAltarRecipe recipe) {
            // Primary ingredient before secondary ingredients
            buf.writeInt(recipe.getIngredients().size() + 1);


            DefaultedList<Ingredient> ingredients = recipe.getIngredients();
            ingredients.set(0, recipe.getPrimaryIngredient());
            for (Ingredient i : ingredients) {
                i.write(buf);
            }
            buf.writeItemStack(recipe.getOutput());
        }
    }

}
