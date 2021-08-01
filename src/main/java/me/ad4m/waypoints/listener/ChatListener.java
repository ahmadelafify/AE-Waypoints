package me.ad4m.waypoints.listener;

import me.ad4m.waypoints.Waypoint;
import me.ad4m.waypoints.WaypointCommand;
import me.ad4m.waypoints.WaypointsMod;
import me.ad4m.waypoints.gui.GuiColorPicker;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatListener {
    private static Minecraft mc = Minecraft.getMinecraft();
    public static Waypoint waypoint;
    public static Waypoint fWaypoint;

    @SubscribeEvent
    public void onChat(final ClientChatReceivedEvent event) {
        final String message = event.message.getUnformattedText();
        String[] messageArr = message.split(" ");
        for (int i = 0; i < messageArr.length; i++) {
            messageArr[i] = EnumChatFormatting.getTextWithoutFormattingCodes(messageArr[i]);
        }
//        System.out.println(Arrays.toString(messageArr));
        if (messageArr.length == 5 && message.startsWith("(Team)") && messageArr[2].startsWith("[") && messageArr[2].endsWith(",") && messageArr[3].endsWith(",") && messageArr[4].endsWith("]")) {
            final int x = (int) Float.parseFloat(messageArr[2].substring(1, messageArr[2].length() - 1));
            final int y = (int) Float.parseFloat(messageArr[3].substring(0, messageArr[3].length() - 1)) + 1;
            final int z = (int) Float.parseFloat(messageArr[4].substring(0, messageArr[4].length() - 1));
            ChatListener.waypoint = new Waypoint("Rally", ChatListener.mc.theWorld.provider.getDimensionName(), mc.getCurrentServerData().serverIP, x, y, z, GuiColorPicker.COLORS[1], System.currentTimeMillis(), false);
            if (WaypointsMod.auto) {
                WaypointCommand.run(messageArr[1].substring(0, messageArr[1].length() - 1));
            } else {
                final ChatComponentText hover = new ChatComponentText(EnumChatFormatting.YELLOW + "Click to set waypoint.");
                event.message.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover));
                event.message.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/newwaypoint " + messageArr[1].substring(0, messageArr[1].length() - 1)));
            }
        }
        if (messageArr.length >= 6 && messageArr[1].replaceAll("\\d", "").equals("[/]") && messageArr[2].length() == 1 && messageArr[3].equals("HQ:")) {
//            !-  Faction TPing  -!
//            final ChatComponentText hover2 = new ChatComponentText(EnumChatFormatting.YELLOW + "Click to teleport to faction home.");
//            final IChatComponent component1 = (IChatComponent) event.message.getSiblings().get(0);
//            component1.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover2));
//            component1.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f tp " + component1.getUnformattedText()));
            final ChatComponentText hover3 = new ChatComponentText(EnumChatFormatting.YELLOW + "Click to create waypoint.");
            final IChatComponent component2 = event.message.getSiblings().get(2);
            if (!component2.getUnformattedText().contains("None")) {
                final String[] aosdfj = EnumChatFormatting.getTextWithoutFormattingCodes(component2.getUnformattedText()).replace("HQ: ", "").split(", ");
                final int x2 = Integer.parseInt(aosdfj[0]);
                final int z2 = Integer.parseInt(aosdfj[1]);
                ChatListener.fWaypoint = new Waypoint(messageArr[0], ChatListener.mc.theWorld.provider.getDimensionName(), mc.getCurrentServerData().serverIP, x2, 70, z2, GuiColorPicker.COLORS[1], -1L, true);
                component2.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover3));
                component2.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/newwaypoint FACTION"));
//                mc.thePlayer.addChatMessage((IChatComponent)new ChatComponentText(EnumChatFormatting.GREEN + String.format("Waypoint has been set to %s.", messageArr[0])));
            }

        }
    }
}
