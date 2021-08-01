package me.ad4m.waypoints.listener;

import me.ad4m.waypoints.Waypoint;
import me.ad4m.waypoints.WaypointsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class WorldListener {

    @SubscribeEvent
    public void onClientConnectedToServerEvent(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (!event.isLocal) {
            WaypointsMod.refresh = false;
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!WaypointsMod.refresh) {
            WaypointsMod.refreshWaypointsToRender();
        }
        Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        double d3 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.partialTicks;
        double d4 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.partialTicks;
        double d5 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.partialTicks;
        Tessellator tessellator = Tessellator.getInstance();
        WaypointsMod.looping = true;
        for (Waypoint waypoint : WaypointsMod.getWaypointsToRender()) {
            WaypointsMod.renderIngameWaypoint(waypoint, 12.0, d3, d4, d5, entity, tessellator);
        }
        WaypointsMod.looping = false;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
    }
}
