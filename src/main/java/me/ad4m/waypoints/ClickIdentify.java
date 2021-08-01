package me.ad4m.waypoints;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClickIdentify {
    protected static final Minecraft mc;
    public static ClickIdentify instance;

    @SubscribeEvent
    public void onRightClick(final EntityInteractEvent event) {
        if (WaypointsMod.bindClickIdentify.isPressed()) {
//             && (mc.thePlayer.getCurrentEquippedItem() == null || mc.thePlayer.getCurrentEquippedItem().getDisplayName().equalsIgnoreCase("Air"))
            if (event.target instanceof EntityOtherPlayerMP) {
//                mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "/f who " + "/f who " + EnumChatFormatting.getTextWithoutFormattingCodes(event.target.getDisplayName().getUnformattedText())));
                mc.getNetHandler().addToSendQueue(new C01PacketChatMessage("/f who " + EnumChatFormatting.getTextWithoutFormattingCodes(event.target.getDisplayName().getUnformattedText())));
            }
        }
    }

    static {
        mc = Minecraft.getMinecraft();
        ClickIdentify.instance = new ClickIdentify();
    }
}
