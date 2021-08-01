package me.ad4m.waypoints.gui.screen;

import me.ad4m.waypoints.Waypoint;
import me.ad4m.waypoints.WaypointsMod;
import me.ad4m.waypoints.gui.GuiSlotWaypoints;
import me.ad4m.waypoints.gui.*;
import me.ad4m.waypoints.*;
import net.minecraft.client.gui.*;

import java.io.IOException;

public class GuiScreenWaypointsMenu extends GuiScreen {
    private GuiSlotWaypoints waypointsList;
    private GuiButton delete;
    private GuiButton cancel;
    private GuiButton edit;
    private GuiButton mode;

    public void initGui() {
        this.buttonList.add(this.mode = new GuiButton(3, this.width / 2 - 60, this.height - 46, 120, 20, WaypointsMod.auto ? "Auto" : "Manual"));
        this.buttonList.add(this.delete = new GuiButton(0, this.width / 2 - 151, this.height - 24, 100, 20, "Delete"));
        this.buttonList.add(this.edit = new GuiButton(2, this.width / 2 - 50, this.height - 24, 100, 20, "Edit"));
        this.buttonList.add(this.cancel = new GuiButton(1, this.width / 2 + 51, this.height - 24, 100, 20, "Cancel"));
        this.delete.enabled = false;
        this.edit.enabled = false;
        this.waypointsList = new GuiSlotWaypoints(this);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        waypointsList.handleMouseInput();
    }

    public void drawScreen(final int x, final int y, final float partialTicks) {
        this.drawDefaultBackground();
        this.waypointsList.drawScreen(x, y, partialTicks);
        this.drawCenteredString(this.fontRendererObj, "Waypoints Menu", this.width / 2, 18, 16777215);
        super.drawScreen(x, y, partialTicks);
    }

    public void updateScreen() {
        this.delete.enabled = (this.waypointsList.getSelectedIndex() != -1);
        this.edit.enabled = (this.waypointsList.getSelectedIndex() != -1);
    }

    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                final Waypoint waypoint = WaypointsMod.getWaypointsToRender().get(this.waypointsList.getSelectedIndex());
                this.mc.displayGuiScreen(new GuiScreenDeleteConfirm(this, waypoint));
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(null);
                break;
            }
            case 2: {
                final Waypoint waypoint2 = WaypointsMod.getWaypointsToRender().get(this.waypointsList.getSelectedIndex());
                this.mc.displayGuiScreen(new GuiScreenEditWaypoint(this, waypoint2));
                break;
            }
            case 3: {
                WaypointsMod.auto = !WaypointsMod.auto;
                this.mode.displayString = (WaypointsMod.auto ? "Auto" : "Manual");
                WaypointsMod.writeModeToDisk();
                break;
            }
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public FontRenderer getFontRenderer() {
        return this.fontRendererObj;
    }
}
