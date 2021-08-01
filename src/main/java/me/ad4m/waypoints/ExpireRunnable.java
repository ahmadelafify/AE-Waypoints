package me.ad4m.waypoints;

import net.minecraft.client.*;
import net.minecraft.util.*;

import java.util.*;
import java.util.Timer;

public class ExpireRunnable implements Runnable
{
    private static Minecraft mc;
    
    @Override
    public void run() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                final Iterator<Waypoint> iter = WaypointsMod.getWaypoints().iterator();
                while (iter.hasNext()) {
                    final Waypoint waypoint = iter.next();
                    if (!WaypointsMod.looping) {
                        if (waypoint.isExpired()) {
                            iter.remove();
                            WaypointsMod.refreshWaypointsToRender();
                            WaypointsMod.writeWaypointsToDisk();
                            if (ExpireRunnable.mc != null) {
                                ExpireRunnable.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_AQUA + "Rally has expired!"));
                            }
                        }
                        if (waypoint.isFaction() && ExpireRunnable.mc.thePlayer != null && waypoint.getDistance(ExpireRunnable.mc.thePlayer) < 15.0) {
                            iter.remove();
                            WaypointsMod.refreshWaypointsToRender();
                            WaypointsMod.writeWaypointsToDisk();
                        }
                    }
                }
            }
        }, 0L, 1000L);
    }
    
    static {
        ExpireRunnable.mc = Minecraft.getMinecraft();
    }
}
