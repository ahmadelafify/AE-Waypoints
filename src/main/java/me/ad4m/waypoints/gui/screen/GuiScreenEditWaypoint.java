package me.ad4m.waypoints.gui.screen;

import me.ad4m.waypoints.Waypoint;
import me.ad4m.waypoints.WaypointsMod;
import me.ad4m.waypoints.gui.GuiColorPicker;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiScreenEditWaypoint extends GuiScreen {

    private GuiTextField name;
    private GuiTextField coordsX;
    private GuiTextField coordsY;
    private GuiTextField coordsZ;
    private GuiColorPicker colorPicker;
    private GuiButton edit, cancel;

    private GuiScreenWaypointsMenu parent;
    private Waypoint waypoint;

    public GuiScreenEditWaypoint(GuiScreenWaypointsMenu parent, Waypoint waypoint) {
        this.parent = parent;
        this.waypoint = waypoint;
    }

    @Override
    public void initGui() {
        name = new GuiTextField(0, fontRendererObj, width / 2 - 100, height / 2 - 48, 200, 20);
        name.setFocused(true);
        name.setText(waypoint.getName());

        coordsX = new GuiTextField(1, fontRendererObj, width / 2 - 100, height / 2 - 10, 64, 20);
        coordsY = new GuiTextField(2, fontRendererObj, width / 2 - 32, height / 2 - 10, 63, 20);
        coordsZ = new GuiTextField(3, fontRendererObj, width / 2 + 35, height / 2 - 10, 64, 20);

        buttonList.add(colorPicker = new GuiColorPicker(0, width / 2 - 101, height / 2 + 25, 202, 20));
        buttonList.add(edit = new GuiButton(1, width / 2 - 101, height / 2 + 50, 100, 20, "Edit"));
        buttonList.add(cancel = new GuiButton(2, width / 2 + 1, height / 2 + 50, 100, 20, "Cancel"));
        colorPicker.setColorIndex(GuiColorPicker.getIndexByColor(waypoint.getColor()));

        coordsX.setText(String.valueOf(waypoint.getX()));
        coordsY.setText(String.valueOf(waypoint.getY()));
        coordsZ.setText(String.valueOf(waypoint.getZ()));
        edit.enabled = true;
    }

    @Override
    public void drawScreen(int x, int y, float partialTicks) {
        drawDefaultBackground();

        drawCenteredString(fontRendererObj, "Edit Waypoint", width / 2, 10, 0xFFFFFF);
        drawCenteredString(fontRendererObj, "Name:", width / 2, height / 2 - 60, 0xFFFFFF);
        drawCenteredString(fontRendererObj, "Coordinates (X/Y/Z):", width / 2, height / 2 - 22, 0xFFFFFF);
        drawCenteredString(fontRendererObj, "Color:", width / 2, height / 2 + 14, 0xFFFFFF);

        name.drawTextBox();
        coordsX.drawTextBox();
        coordsY.drawTextBox();
        coordsZ.drawTextBox();

        super.drawScreen(x, y, partialTicks);
    }

    @Override
    protected void keyTyped(char character, int index) {
        if (index == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(null);
        } else if (name.isFocused()) {
            name.textboxKeyTyped(character, index);
        } else if (coordsX.isFocused()) {
            coordsX.textboxKeyTyped(character, index);
        } else if (coordsY.isFocused()) {
            coordsY.textboxKeyTyped(character, index);
        } else if (coordsZ.isFocused()) {
            coordsZ.textboxKeyTyped(character, index);
        }

        for (Waypoint waypoint : WaypointsMod.getWaypoints()) {
            if (waypoint.getName().equalsIgnoreCase(name.getText()) && waypoint.getServer().equalsIgnoreCase(mc.getCurrentServerData().serverIP) &&
                    !waypoint.getName().equals(this.waypoint.getName())) {
                edit.enabled = false;
                return;
            }
        }

        edit.enabled = name.getText().length() > 0 && NumberUtils.isDigits(coordsX.getText().replace("-", "")) && NumberUtils.isDigits(coordsY.getText().replace("-", "")) && NumberUtils.isDigits(coordsZ.getText().replace("-", ""));
    }

    @Override
    public void updateScreen() {
        name.updateCursorCounter();
        coordsX.updateCursorCounter();
        coordsY.updateCursorCounter();
        coordsZ.updateCursorCounter();
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
                int x = Integer.valueOf(coordsX.getText());
                int y = Integer.valueOf(coordsY.getText());
                int z = Integer.valueOf(coordsZ.getText());
                int color = colorPicker.getSelectedColor();

                WaypointsMod.editWaypoint(waypoint, new Waypoint(name, world, server, x, y, z, color, -1, false));
                mc.displayGuiScreen(null);
                mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Waypoint '" + waypoint.getName() + "' edited! Now " + name));
                return;

            case 2:
                mc.displayGuiScreen(parent);
                return;
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int key) throws IOException {
        super.mouseClicked(x, y, key);
        name.mouseClicked(x, y, key);
        coordsX.mouseClicked(x, y, key);
        coordsY.mouseClicked(x, y, key);
        coordsZ.mouseClicked(x, y, key);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
