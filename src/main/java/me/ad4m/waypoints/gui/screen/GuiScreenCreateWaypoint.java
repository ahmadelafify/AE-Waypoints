package me.ad4m.waypoints.gui.screen;

import me.ad4m.waypoints.Waypoint;
import me.ad4m.waypoints.WaypointsMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.math.NumberUtils;
import me.ad4m.waypoints.gui.GuiColorPicker;

import java.io.IOException;

public class GuiScreenCreateWaypoint extends GuiScreen {
    private GuiTextField name;
    private GuiTextField coordsX;
    private GuiTextField coordsY;
    private GuiTextField coordsZ;
    private GuiColorPicker colorPicker;
    private GuiButton create;
    private GuiButton cancel;

    public void initGui() {
        this.name = new GuiTextField(0, fontRendererObj, this.width / 2 - 100, this.height / 2 - 48, 200, 20);
        this.name.setFocused(true);
        this.coordsX = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 100, this.height / 2 - 10, 64, 20);
        this.coordsY = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 32, this.height / 2 - 10, 63, 20);
        this.coordsZ = new GuiTextField(3, this.fontRendererObj, this.width / 2 + 35, this.height / 2 - 10, 64, 20);
        this.buttonList.add(this.colorPicker = new GuiColorPicker(0, this.width / 2 - 101, this.height / 2 + 25, 202, 20));
        this.buttonList.add(this.create = new GuiButton(1, this.width / 2 - 101, this.height / 2 + 50, 100, 20, "Create"));
        this.buttonList.add(this.cancel = new GuiButton(2, this.width / 2 + 1, this.height / 2 + 50, 100, 20, "Cancel"));
        this.coordsX.setText(String.valueOf((int) this.mc.thePlayer.posX));
        this.coordsY.setText(String.valueOf((int) this.mc.thePlayer.posY));
        this.coordsZ.setText(String.valueOf((int) this.mc.thePlayer.posZ));
        this.create.enabled = false;
    }

    public void drawScreen(final int x, final int y, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Create Waypoint", this.width / 2, 10, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj, "Name:", this.width / 2, this.height / 2 - 60, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj, "Coordinates (X/Y/Z):", this.width / 2, this.height / 2 - 22, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj, "Color:", this.width / 2, this.height / 2 + 14, 0xFFFFFF);
        this.name.drawTextBox();
        this.coordsX.drawTextBox();
        this.coordsY.drawTextBox();
        this.coordsZ.drawTextBox();
        super.drawScreen(x, y, partialTicks);
    }

    protected void keyTyped(final char character, final int index) {
        if (index == 1) {
            this.mc.displayGuiScreen(null);
        } else if (this.name.isFocused()) {
            this.name.textboxKeyTyped(character, index);
        } else if (this.coordsX.isFocused()) {
            this.coordsX.textboxKeyTyped(character, index);
        } else if (this.coordsY.isFocused()) {
            this.coordsY.textboxKeyTyped(character, index);
        } else if (this.coordsZ.isFocused()) {
            this.coordsZ.textboxKeyTyped(character, index);
        }
        for (final Waypoint waypoint : WaypointsMod.getWaypoints()) {
            if (waypoint.getName().equalsIgnoreCase(this.name.getText()) && waypoint.getServer().equalsIgnoreCase(mc.getCurrentServerData().serverIP)) {
                this.create.enabled = false;
                return;
            }
        }
        this.create.enabled = (this.name.getText().length() > 0 && NumberUtils.isDigits(this.coordsX.getText().replace("-", "")) && NumberUtils.isDigits(this.coordsY.getText().replace("-", "")) && NumberUtils.isDigits(this.coordsZ.getText().replace("-", "")));
    }

    public void updateScreen() {
        this.name.updateCursorCounter();
        this.coordsX.updateCursorCounter();
        this.coordsY.updateCursorCounter();
        this.coordsZ.updateCursorCounter();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                colorPicker.nextColor();
                return;

            case 1:
                String name = this.name.getText();
                String world = mc.theWorld.provider.getDimensionName();
                String server = mc.getCurrentServerData().serverIP;
                int x = Integer.parseInt(coordsX.getText());
                int y = Integer.parseInt(coordsY.getText());
                int z = Integer.parseInt(coordsZ.getText());
                int color = colorPicker.getSelectedColor();

                WaypointsMod.addWaypoint(new Waypoint(name, world, server, x, y, z, color, -1L, false));
                mc.displayGuiScreen(null);
                mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Waypoint '" + name + "' created!"));
                return;

            case 2:
                mc.displayGuiScreen(null);
                return;
        }
    }

    protected void mouseClicked(final int x, final int y, final int key) throws IOException {
        super.mouseClicked(x, y, key);
        this.name.mouseClicked(x, y, key);
        this.coordsX.mouseClicked(x, y, key);
        this.coordsY.mouseClicked(x, y, key);
        this.coordsZ.mouseClicked(x, y, key);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}
