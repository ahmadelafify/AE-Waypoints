package me.ad4m.waypoints.gui.screen;

import me.ad4m.waypoints.Waypoint;
import me.ad4m.waypoints.WaypointsMod;
import net.minecraft.client.gui.*;
import me.ad4m.waypoints.*;
import net.minecraft.util.*;

public class GuiScreenDeleteConfirm extends GuiScreen {
    private GuiScreenWaypointsMenu parent;
    private Waypoint waypoint;

    public GuiScreenDeleteConfirm(final GuiScreenWaypointsMenu parent, final Waypoint waypoint) {
        this.parent = parent;
        this.waypoint = waypoint;
    }

    public void initGui() {
        this.buttonList.add(new GuiButton(0, this.width / 2 - 101, this.height / 2 + 12, 100, 20, "Confirm"));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 1, this.height / 2 + 12, 100, 20, "Cancel"));
    }

    public void drawScreen(final int x, final int y, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.mc.fontRendererObj, "Are you sure you want to delete waypoint '" + this.waypoint.getName() + "'?", this.width / 2, this.height / 2 - 12, 16777215);
        super.drawScreen(x, y, partialTicks);
    }

    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                WaypointsMod.removeWaypoint(this.waypoint);
                this.mc.displayGuiScreen(new GuiScreenWaypointsMenu());
                this.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Waypoint '" + this.waypoint.getName() + "' removed!"));
            }
            case 1: {
                this.mc.displayGuiScreen(this.parent);
            }
            default: {
            }
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}
