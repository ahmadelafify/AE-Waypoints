package me.ad4m.waypoints;

import me.ad4m.waypoints.listener.ChatListener;
import net.minecraft.command.*;
import net.minecraft.client.*;
import me.ad4m.waypoints.listener.*;
import net.minecraft.util.*;

import java.util.*;

public class WaypointCommand extends CommandBase {
    public String getCommandName() {
        return "newwaypoint";
    }

    public String getCommandUsage(final ICommandSender p_71518_1_) {
        return "/newwaypoint";
    }

    public void processCommand(final ICommandSender p_71515_1_, final String[] args) {
        run(args[0]);
        Minecraft.getMinecraft().ingameGUI.getChatGUI().getSentMessages().remove(Minecraft.getMinecraft().ingameGUI.getChatGUI().getSentMessages().size() - 1);
    }

    public boolean canCommandSenderUseCommand(final ICommandSender p_71519_1_) {
        return true;
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public static void run(final String s) {
        if (s.equals("FACTION")) {
            if (ChatListener.fWaypoint != null) {
                final Iterator<Waypoint> iter = WaypointsMod.getWaypoints().iterator();
                while (iter.hasNext()) {
                    if (iter.next().getName().equals(ChatListener.fWaypoint.getName())) {
                        iter.remove();
                    }
                }
                WaypointsMod.addWaypoint(ChatListener.fWaypoint);
            }
        } else if (ChatListener.waypoint != null) {
            ChatListener.waypoint.setTimeCreated(System.currentTimeMillis());
            final Iterator<Waypoint> iter = WaypointsMod.getWaypoints().iterator();
            while (iter.hasNext()) {
                if (iter.next().getName().equals(ChatListener.waypoint.getName())) {
                    iter.remove();
                }
            }
            WaypointsMod.addWaypoint(ChatListener.waypoint);
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_AQUA + s + " has updated the team's rally point! This will last for " + Waypoint.rallyTimeMinutes + " minutes."));
        }
    }
}
