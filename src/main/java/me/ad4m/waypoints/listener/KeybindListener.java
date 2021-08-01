package me.ad4m.waypoints.listener;

import me.ad4m.waypoints.WaypointsMod;
import me.ad4m.waypoints.gui.screen.GuiScreenCreateWaypoint;
import me.ad4m.waypoints.gui.screen.GuiScreenWaypointsMenu;
import net.minecraft.client.*;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeybindListener {
    private Minecraft mc;

    public KeybindListener() {
        this.mc = Minecraft.getMinecraft();
    }

    @SubscribeEvent
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (WaypointsMod.bindWaypointCreate.isPressed()) {
            if (this.mc.isSingleplayer()) {
                this.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "You can only use adam's Waypoints in multiplayer!"));
            } else {
                this.mc.displayGuiScreen(new GuiScreenCreateWaypoint());
            }
        } else if (WaypointsMod.bindWaypointMenu.isPressed()) {
            if (this.mc.isSingleplayer()) {
                this.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "You can only use adam's Waypoints in multiplayer!"));
            } else {
                this.mc.displayGuiScreen(new GuiScreenWaypointsMenu());
            }
        } else if (WaypointsMod.bindTeamLocation.isPressed() && this.mc.thePlayer != null) {
            if (this.mc.isSingleplayer()) {
                this.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "You can only use adam's Waypoints in multiplayer!"));
            } else {
                this.mc.thePlayer.sendChatMessage("/tl");
            }
        } else if (WaypointsMod.bindSOTWkey.isPressed() && this.mc.thePlayer != null) {
            if (this.mc.isSingleplayer() || !this.mc.getCurrentServerData().serverIP.contains("vipermc")) {
                this.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "You are not on ViperMC!"));
            } else {
//                mc.thePlayer.inventory.dropAllItems();
                mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("/kit"));
            }
        }
    }
}
