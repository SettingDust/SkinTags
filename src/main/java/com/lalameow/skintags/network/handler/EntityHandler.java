package com.lalameow.skintags.network.handler;

import com.lalameow.skintags.SkinTags;
import com.lalameow.skintags.network.packet.SkinPacket;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * Author: SettingDust.
 * Date: 2018/4/30.
 */
public class EntityHandler implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals(SkinTags.getChannel())) {
            int id = byteArrayToInt(message);
            for (Entity entity : player.getWorld().getEntities()) {
                if (entity.getEntityId() == id) {
                    SkinPacket packet = new SkinPacket(entity);
                    if (packet.getSkin().toString() != null) {
                        player.sendPluginMessage(SkinTags.getInstance(), SkinTags.getChannel(),
                                packet.toString().getBytes());
                    }
                    break;
                }
            }
        }
    }

    public static int byteArrayToInt(byte[] b) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i] & 0x000000FF) << shift;
        }
        return value;
    }
}
