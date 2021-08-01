package me.ad4m.waypoints.gui;

import me.ad4m.waypoints.Waypoint;
import me.ad4m.waypoints.WaypointsMod;
import me.ad4m.waypoints.gui.screen.GuiScreenDeleteConfirm;
import me.ad4m.waypoints.gui.screen.GuiScreenEditWaypoint;
import me.ad4m.waypoints.gui.screen.GuiScreenWaypointsMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;

public class GuiSlotWaypoints extends GuiSlot {
    private Minecraft mc;
    private GuiScreenWaypointsMenu parent;
    private int selectedIndex;

    public GuiSlotWaypoints(final GuiScreenWaypointsMenu parent) {
        super(Minecraft.getMinecraft(), parent.width, parent.height, 48, parent.height - 48, 24);
        this.mc = Minecraft.getMinecraft();
        this.selectedIndex = -1;
        this.parent = parent;
    }

    protected int getSize() {
        return WaypointsMod.getWaypointsToRender().size();
    }

    protected boolean isSelected(final int index) {
        return this.selectedIndex == index;
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    protected int getContentHeight() {
        return this.getSize() * 24;
    }

    protected void drawBackground() {
    }

    protected void elementClicked(final int index, final boolean doubleClicked, final int p_148144_3_, final int p_148144_4_) {
        this.selectedIndex = index;
        if (doubleClicked) {
            final Waypoint waypoint = WaypointsMod.getWaypointsToRender().get(this.selectedIndex);
            this.mc.displayGuiScreen(new GuiScreenEditWaypoint(this.parent, waypoint));
        }
    }

    protected void drawSlot(final int index, final int x, final int y, final int p_148126_4_, final int p_148126_6_, final int p_148126_7_) {
        final Waypoint waypoint = WaypointsMod.getWaypointsToRender().get(index);
        this.parent.drawString(this.parent.getFontRenderer(), waypoint.getName(), x + 2, y, 16777215);
        this.parent.drawString(this.parent.getFontRenderer(), waypoint.getWorld() + " - " + waypoint.getX() + " / " + waypoint.getY() + " / " + waypoint.getZ(), x + 2, y + 12, 7829367);
    }
}
