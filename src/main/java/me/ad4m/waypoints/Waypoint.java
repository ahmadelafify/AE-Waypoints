package me.ad4m.waypoints;

import net.minecraft.client.*;
import net.minecraft.entity.*;

public class Waypoint {
    private static Minecraft mc;
    public static int rallyTimeMinutes;
    private final String name;
    private final String world;
    private final String server;
    private final int x;
    private final int y;
    private final int z;
    private final int color;
    private long timeCreated;
    private boolean faction;

    public Waypoint(final String name, final String world, final String server, final int x, final int y, final int z, final int color, final long timeCreated, final boolean faction) {
        this.name = name;
        this.world = world;
        this.server = server;
        this.x = x;
        this.z = z;
        this.y = y;
        this.color = color;
        this.timeCreated = timeCreated;
        this.faction = faction;
    }

    public String getName() {
        return this.name;
    }

    public String getWorld() {
        return this.world;
    }

    public String getServer() {
        return this.server;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public int getColor() {
        return this.color;
    }

    public void setTimeCreated(final long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public boolean isExpired() {
        return this.timeCreated != -1L && System.currentTimeMillis() - this.timeCreated > 60000 * Waypoint.rallyTimeMinutes;
    }

    public boolean isFaction() {
        return this.faction;
    }

    public boolean shouldRender() {
        return !Waypoint.mc.isSingleplayer() && Waypoint.mc.theWorld.provider.getDimensionName().equals(this.world) && Waypoint.mc.getCurrentServerData().serverIP.equals(this.server);
    }

    public double getDistance(final Entity en) {
        final double x = this.x - en.posX;
        final double y = this.y - en.posY;
        final double z = this.z - en.posZ;
        return Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public String toString() {
        return this.name + ";" + this.world + ";" + this.server + ";" + this.x + ";" + this.y + ";" + this.z + ";" + this.color + ";" + this.timeCreated + ";" + this.faction;
    }

    public static Waypoint fromString(final String string) {
        final String[] parts = string.split(";");
        return new Waypoint(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), Integer.parseInt(parts[6]), Long.parseLong(parts[7]), Boolean.parseBoolean(parts[8]));
    }

    static {
        Waypoint.mc = Minecraft.getMinecraft();
        Waypoint.rallyTimeMinutes = 5;
    }
}
