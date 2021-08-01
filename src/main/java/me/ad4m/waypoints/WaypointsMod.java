package me.ad4m.waypoints;

import me.ad4m.waypoints.listener.ChatListener;
import me.ad4m.waypoints.listener.KeybindListener;
import me.ad4m.waypoints.listener.WorldListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.opengl.GL11;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Mod(name = "adam's Waypoints", modid = "waypoints", version = "1.0-Beta")
public class WaypointsMod {
    private static final File WAYPOINTS_FILE;
    private static final File MODE_FILE;
    private static Minecraft mc = Minecraft.getMinecraft();
    public static KeyBinding bindClickIdentify = new KeyBinding("Faction Identify", 15, "adam's Waypoints");
    public static KeyBinding bindWaypointCreate = new KeyBinding("Create Waypoint", 39, "adam's Waypoints");
    public static KeyBinding bindWaypointMenu = new KeyBinding("Open Menu", 41, "adam's Waypoints");
    public static KeyBinding bindTeamLocation = new KeyBinding("Team Location", 56, "adam's Waypoints");
    public static KeyBinding bindSOTWkey = new KeyBinding("SOTW Setup", 43, "adam's Waypoints");
    private static Set<Waypoint> waypoints = new HashSet<Waypoint>();
    private static ArrayList<Waypoint> waypointsToRender = new ArrayList<Waypoint>();
    public static boolean auto;
    public static boolean refresh;
    public static boolean looping;

    static {
        final File root = new File(WaypointsMod.mc.mcDataDir, "config/adam's Waypoints");
        root.mkdir();
        WAYPOINTS_FILE = new File(root, "waypoints");
        MODE_FILE = new File(root, "mode");
    }

    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        final ModMetadata metadata = event.getModMetadata();
        metadata.version = "1.0-Beta";
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        if (WaypointsMod.WAYPOINTS_FILE.exists()) {
            try {
                final BufferedReader reader = new BufferedReader(new FileReader(WaypointsMod.WAYPOINTS_FILE));
                String readLine;
                while ((readLine = reader.readLine()) != null) {
                    WaypointsMod.waypoints.add(Waypoint.fromString(readLine));
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (WaypointsMod.MODE_FILE.exists()) {
            try {
                final BufferedReader reader = new BufferedReader(new FileReader(WaypointsMod.MODE_FILE));
                WaypointsMod.auto = Boolean.parseBoolean(reader.readLine());
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ClientRegistry.registerKeyBinding(WaypointsMod.bindClickIdentify);
        ClientRegistry.registerKeyBinding(WaypointsMod.bindWaypointCreate);
        ClientRegistry.registerKeyBinding(WaypointsMod.bindWaypointMenu);
        ClientRegistry.registerKeyBinding(WaypointsMod.bindTeamLocation);
        ClientRegistry.registerKeyBinding(WaypointsMod.bindSOTWkey);
        FMLCommonHandler.instance().bus().register(new KeybindListener());
        MinecraftForge.EVENT_BUS.register(new ChatListener());
        MinecraftForge.EVENT_BUS.register(new WorldListener());
        MinecraftForge.EVENT_BUS.register(new ClickIdentify());
        ClientCommandHandler.instance.registerCommand(new WaypointCommand());
        new ExpireRunnable().run();
    }

    public static void addWaypoint(final Waypoint waypoint) {
        WaypointsMod.waypoints.add(waypoint);
        refreshWaypointsToRender();
        writeWaypointsToDisk();
    }

    public static void editWaypoint(final Waypoint oldWaypoint, final Waypoint newWaypoint) {
        WaypointsMod.waypoints.remove(oldWaypoint);
        addWaypoint(newWaypoint);
    }

    public static void removeWaypoint(final Waypoint waypoint) {
        WaypointsMod.waypoints.remove(waypoint);
        refreshWaypointsToRender();
        writeWaypointsToDisk();
    }

    public static void writeWaypointsToDisk() {
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(WaypointsMod.WAYPOINTS_FILE));
            for (final Waypoint w : WaypointsMod.waypoints) {
                writer.write(w.toString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeModeToDisk() {
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(WaypointsMod.MODE_FILE));
            writer.write(Boolean.toString(WaypointsMod.auto));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Set<Waypoint> getWaypoints() {
        return WaypointsMod.waypoints;
    }

    public static void renderIngameWaypoint(final Waypoint waypoint, final double d, final double d3, final double d4, final double d5, final Entity entity, final Tessellator tessellator) {
        double x = waypoint.getX() - d3 + 0.5;
        double y = waypoint.getY() - d4 + 1.0;
        double z = waypoint.getZ() - d5 + 0.5;
        double distance = Math.sqrt(x * x + y * y + z * z);
        final String name = waypoint.getName() + " [" + (int)distance + "m]";
        final double maxDistance = FMLClientHandler.instance().getClient().gameSettings.renderDistanceChunks * 12.0;
        if (distance > maxDistance) {
            x = x / distance * maxDistance;
            y = y / distance * maxDistance;
            z = z / distance * maxDistance;
            distance = maxDistance;
        }
        final float size = ((float)distance * 0.1f + 1.0f) * 0.0266f;
        final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        final FontRenderer fontrenderer = renderManager.getFontRenderer();
        if (fontrenderer == null) {
            return;
        }
        final int width = fontrenderer.getStringWidth(name) / 2;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 0.0f);
        GlStateManager.scale(size, size, size);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GlStateManager.disableTexture2D();
        GlStateManager.enableDepth();
        if (distance < maxDistance) {
            GlStateManager.depthMask(true);
        }
        final int r = waypoint.getColor() >> 16 & 0xFF;
        final int g = waypoint.getColor() >> 8 & 0xFF;
        final int b = waypoint.getColor() & 0xFF;
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 3.0f);
        final WorldRenderer wr = tessellator.getWorldRenderer();
        wr.begin(7, DefaultVertexFormats.POSITION_COLOR);
        wr.pos((double)(-width - 2), -2.0, 0.0).color(r, g, b, 150).endVertex();
        wr.pos((double)(-width - 2), 9.0, 0.0).color(r, g, b, 150).endVertex();
        wr.pos((double)(width + 2), 9.0, 0.0).color(r, g, b, 150).endVertex();
        wr.pos((double)(width + 2), -2.0, 0.0).color(r, g, b, 150).endVertex();
        tessellator.draw();
        GlStateManager.doPolygonOffset(1.0f, 1.0f);
        wr.begin(7, DefaultVertexFormats.POSITION_COLOR);
        wr.pos((double)(-width - 1), -1.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.15f).endVertex();
        wr.pos((double)(-width - 1), 8.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.15f).endVertex();
        wr.pos((double)(width + 1), 8.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.15f).endVertex();
        wr.pos((double)(width + 1), -1.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.15f).endVertex();
        tessellator.draw();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.doPolygonOffset(1.0f, 7.0f);
        wr.begin(7, DefaultVertexFormats.POSITION_COLOR);
        wr.pos((double)(-width - 2), -2.0, 0.0).color(r, g, b, 40).endVertex();
        wr.pos((double)(-width - 2), 9.0, 0.0).color(r, g, b, 40).endVertex();
        wr.pos((double)(width + 2), 9.0, 0.0).color(r, g, b, 40).endVertex();
        wr.pos((double)(width + 2), -2.0, 0.0).color(r, g, b, 40).endVertex();
        tessellator.draw();
        GlStateManager.doPolygonOffset(1.0f, 5.0f);
        wr.begin(7, DefaultVertexFormats.POSITION_COLOR);
        wr.pos((double)(-width - 1), -1.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.15f).endVertex();
        wr.pos((double)(-width - 1), 8.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.15f).endVertex();
        wr.pos((double)(width + 1), 8.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.15f).endVertex();
        wr.pos((double)(width + 1), -1.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.15f).endVertex();
        tessellator.draw();
        GlStateManager.disablePolygonOffset();
        GlStateManager.enableTexture2D();
        fontrenderer.drawString(name, -width, 0, -3355444);
        GlStateManager.disableDepth();
        fontrenderer.drawString(name, -width, 0, -1);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    public static void refreshWaypointsToRender() {
        WaypointsMod.waypointsToRender.clear();
        for (final Waypoint waypoint : getWaypoints()) {
            if (waypoint.shouldRender()) {
                WaypointsMod.waypointsToRender.add(waypoint);
            }
        }
    }

    public static ArrayList<Waypoint> getWaypointsToRender() {
        return WaypointsMod.waypointsToRender;
    }

}
