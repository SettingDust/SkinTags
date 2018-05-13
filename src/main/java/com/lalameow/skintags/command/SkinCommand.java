package com.lalameow.skintags.command;

import com.lalameow.skintags.api.SkinNBTAPI;
import com.lalameow.skintags.SkinTags;
import com.lalameow.skintags.skin.SkinType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Author: SettingDust.
 * Date: 2018/3/28.
 */
public class SkinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("skintags")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length >= 2) {
                    switch (args[0]) {
                        case "set":
                            if (args.length >= 4)
                                switch (args[1]) {
                                    case "item":
                                        ItemStack itemStack = player.getEquipment().getItemInMainHand();
                                        if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
                                            SkinNBTAPI.setSkin(args[3], SkinType.valueOf(args[2].toUpperCase()), itemStack);
                                            sender.sendMessage(ChatColor.GREEN + "设置成功");
                                        } else {
                                            sender.sendMessage(ChatColor.RED + "请手持想设置皮肤的物品!");
                                        }
                                        break;
                                    case "entity":
                                        SkinTags.getSetMap().put(player.getUniqueId(),
                                                new String[]{args[3], args[2]});
                                        sender.sendMessage(ChatColor.GREEN + "请右键想要设置的实体");
                                        break;
                                }
                            else
                                sender.sendMessage(ChatColor.RED + "缺少参数!");
                            break;
                        case "remove":
                            switch (args[1]) {
                                case "item":
                                    ItemStack itemStack = player.getEquipment().getItemInMainHand();
                                    if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
                                        SkinNBTAPI.removeSkin(itemStack);
                                        sender.sendMessage(ChatColor.GREEN + "删除成功");
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "请手持想删除皮肤的物品!");
                                    }
                                    break;
                                case "entity":
                                    SkinTags.getRemoveMap().put(player.getUniqueId(), args[2]);
                                    sender.sendMessage(ChatColor.GREEN + "请右键想要设置的实体");
                                    break;
                            }
                            break;
                    }
                } else {
                    sender.sendMessage(new String[]{
                            ChatColor.GOLD + "/skintags set item [类型] [皮肤] 设置物品皮肤",
                            ChatColor.GOLD + "/skintags set entity [类型] [皮肤] 设置实体皮肤",
                            ChatColor.GOLD + "/skintags remove item 删除物品皮肤",
                            ChatColor.GOLD + "/skintags remove entity [类型] 删除实体皮肤"
                    });
                }
            } else {
                sender.sendMessage(ChatColor.RED + "该指令必须由玩家执行!");
            }
        }
        return true;
    }
}
