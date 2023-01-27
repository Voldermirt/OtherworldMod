package net.voldermirt.otherworld.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.voldermirt.otherworld.OtherworldMod;


public class DruidAltarScreen extends HandledScreen<DruidAltarScreenHandler> {

    private static final Identifier TEXTURE =
            new Identifier(OtherworldMod.MOD_ID, "textures/gui/druid_altar_gui.png");

    public DruidAltarScreen(DruidAltarScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
        titleY = titleY - 2;
        backgroundHeight = 168;
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("block.otherworld.druid_altar.charge"),
                btn -> {
                    handler.infuseItem();
                }).position(x + 86, y + 56).size(43, 20).
                tooltip(Tooltip.of(Text.translatable("block.otherworld.druid_altar.charge_tooltip"))).build());
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        // Weird vertical offset, hence the -1
        int y = ((height - backgroundHeight) / 2) + 1;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        renderEnergyBar(matrices, x, y);
    }

    private void renderEnergyBar(MatrixStack matrices, int x, int y) {
        drawTexture(matrices, x + 160, y + 10, 176, 0, 4, handler.getScaledEnergy());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}
