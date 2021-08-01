package me.ad4m.waypoints.gui;

import net.minecraft.client.gui.*;

import java.util.*;

import net.minecraft.client.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;

public class GuiColorPicker extends GuiButton {
    public static final int[] COLORS = {0xFFCF000F, 0xFFF22613, 0xFFDB0A5B, 0xFF9B59B6, 0xFF3A539B, 0xFF59ABE3, 0xFF1F3A93, 0xFF1BA39C, 0xFF3FC380, 0xFFE9D460, 0xFFF9690E};
    private int colorIndex;

    public GuiColorPicker(final int id, final int x, final int y, final int width, final int height) {
        super(id, x, y, width, height, "");
        this.colorIndex = new Random().nextInt(COLORS.length - 1);
    }

    public void drawButton(final Minecraft mc, final int x, final int y) {
        if (!this.visible) {
            return;
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.hovered = (x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height);
        GL11.glEnable(3042);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glBlendFunc(770, 771);
        drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -6250336);
        drawRect(this.xPosition + 1, this.yPosition + 1, this.xPosition + this.width - 1, this.yPosition + this.height - 1, this.getSelectedColor());
        this.mouseDragged(mc, x, y);
    }

    public void nextColor() {
        colorIndex++;
        if (colorIndex == COLORS.length) {
            colorIndex = 0;
        }
    }

    public static int getIndexByColor(final int color) {
        for (int i = 0; i < COLORS.length; ++i) {
            if (COLORS[i] == color) {
                return i;
            }
        }
        return 0;
    }

    public void setColorIndex(final int colorIndex) {
        this.colorIndex = colorIndex;
    }

    public int getSelectedColor() {
        return COLORS[colorIndex];
    }

}
