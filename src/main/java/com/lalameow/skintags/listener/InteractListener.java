package com.lalameow.skintags.listener;

import com.lalameow.skintags.api.SkinNBTAPI;
import com.lalameow.skintags.SkinTags;
import com.lalameow.skintags.skin.SkinType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

/**
 * Author: SettingDust.
 * Date: 2018/4/1.
 */
public class InteractListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if (!event.getRightClicked().getType().equals(EntityType.PLAYER)) {
            Player player = event.getPlayer();
            if (SkinTags.getRemoveMap().containsKey(player.getUniqueId())) {
                SkinNBTAPI.removeSkin(SkinType.valueOf(SkinTags.getRemoveMap().get(player.getUniqueId()).toUpperCase()), event.getRightClicked());
                player.sendMessage(ChatColor.GREEN + "删除成功");
            } else if (SkinTags.getSetMap().containsKey(player.getUniqueId())) {
                SkinNBTAPI.setSkin(
                        SkinTags.getSetMap().get(player.getUniqueId())[0],
                        SkinType.valueOf(SkinTags.getSetMap().get(player.getUniqueId())[1].toUpperCase()),
                        event.getRightClicked());
                player.sendMessage(ChatColor.GREEN + "设置成功");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer() != null) {
            SkinNBTAPI.refreshSkin(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onHeld(PlayerItemHeldEvent event) {
        Bukkit.getScheduler().runTask(SkinTags.getInstance(),
                () -> SkinNBTAPI.refreshSkin(event.getPlayer()));
    }
}
