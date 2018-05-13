package com.lalameow.skintags.api;

import com.google.gson.Gson;
import com.lalameow.skintags.SkinTags;
import com.lalameow.skintags.network.packet.SkinPacket;
import com.lalameow.skintags.skin.EntitySkin;
import com.lalameow.skintags.skin.SkinType;
import me.dpohvar.powernbt.PowerNBT;
import me.dpohvar.powernbt.api.NBTCompound;
import me.dpohvar.powernbt.api.NBTList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import ru.endlesscode.rpginventory.inventory.ArmorType;
import ru.endlesscode.rpginventory.inventory.InventoryManager;
import ru.endlesscode.rpginventory.inventory.slot.Slot;
import ru.endlesscode.rpginventory.inventory.slot.SlotManager;

/**
 * Author: SettingDust.
 * Date: 2018/3/29.
 */
public class SkinNBTAPI {
    public static void setSkin(String skin, SkinType skinType, ItemStack itemStack) {
        NBTCompound itemCompound = PowerNBT.getApi().read(itemStack);

        NBTCompound skinCompound = new NBTCompound();
        skinCompound.put("ID", skin);
        skinCompound.put("type", skinType.ordinal());

        itemCompound = itemCompound == null ? new NBTCompound() : itemCompound;
        itemCompound.put("Skin", skinCompound);

        PowerNBT.getApi().write(itemStack, itemCompound);
    }

    public static NBTCompound getSkin(ItemStack itemStack) {
        NBTCompound nbt = PowerNBT.getApi().read(itemStack);
        return nbt == null ? new NBTCompound() : nbt.getCompound("Skin");
    }

    public static void removeSkin(ItemStack itemStack) {
        NBTCompound nbt = PowerNBT.getApi().read(itemStack);
        if (nbt != null) {
            nbt.remove("Skin");
        }
    }

    public static void setSkin(String skin, SkinType skinType, Entity entity) {
        EntitySkin entitySkin = getSkin(entity);
        entitySkin.setValue(skin, skinType);
        setSkin(entitySkin, entity);
    }

    public static void removeSkin(SkinType skinType, Entity entity) {
        NBTCompound nbtCompound = PowerNBT.getApi().read(entity);
        nbtCompound = nbtCompound == null ? new NBTCompound() : nbtCompound;
        EntitySkin entitySkin = getSkin(entity);
        if (nbtCompound.containsKey("Tags")) {
            entitySkin.setValue("", skinType);
        }
        setSkin(entitySkin, entity);
    }

    public static boolean setSkin(EntitySkin skin, Entity entity) {
        NBTCompound nbtCompound = PowerNBT.getApi().read(entity);
        NBTList tags = new NBTList();
        boolean isDifferent = !skin.equals(getSkin(entity));
        nbtCompound = nbtCompound == null ? new NBTCompound() : nbtCompound;
        if (nbtCompound.containsKey("Tags")) {
            int i = getSkinIndex(tags);
            tags = nbtCompound.getList("Tags");
            tags.remove(i);
        }
        tags.add(skin.toString());
        nbtCompound.put("Tags", tags);
        PowerNBT.getApi().write(entity, nbtCompound);
        return isDifferent;
    }

    public static EntitySkin getSkin(Entity entity) {
        NBTCompound nbt = PowerNBT.getApi().read(entity);
        if (nbt != null && nbt.containsKey("Tags")) {
            NBTList tags = (NBTList) nbt.get("Tags");
            return getSkin(tags);
        }
        return new EntitySkin();
    }

    public static void removeSkin(Entity entity) {
        NBTCompound nbt = PowerNBT.getApi().read(entity);
        if (nbt != null && nbt.containsKey("Tags")) {
            NBTList tags = (NBTList) nbt.get("Tags");
            tags.remove(getSkinIndex(tags));
        }
    }

    public static void refreshSkin(LivingEntity livingEntity) {
        EntityEquipment entityEquipment = livingEntity.getEquipment();

        //装备
        EntitySkin entitySkin = new EntitySkin();
        entitySkin.setValue(entityEquipment.getHelmet(), SkinType.HEAD);
        entitySkin.setValue(entityEquipment.getLeggings(), SkinType.LEGS);
        entitySkin.setValue(entityEquipment.getBoots(), SkinType.FEET);

        //胸甲
        if (SkinTags.isRPGInventoryLoaded()
                && livingEntity instanceof Player) {
            Player player = (Player) livingEntity;
            entitySkin.setValue(InventoryManager.get(player).getInventory()
                    .getItem(ArmorType.CHESTPLATE.getSlot()), SkinType.CHEST);
            entitySkin.setValue(InventoryManager.get(player).getInventory().
                    getItem(SlotManager.instance().getElytraSlot().getSlotId()), SkinType.WINGS);
        } else {
            ItemStack chest = entityEquipment.getChestplate();
            if (chest != null && chest.getType() == Material.ELYTRA) {
                entitySkin.setValue(chest, SkinType.WINGS);
            } else {
                entitySkin.setValue(chest, SkinType.CHEST);
            }
        }

        //手中物品
        ItemStack handItem = entityEquipment.getItemInMainHand();
        if (handItem.getType().toString().contains("SWORD")) {
            entitySkin.setValue(handItem, SkinType.SWORD);
        } else if (handItem.getType().toString().contains("BOW")) {
            entitySkin.setValue(handItem, SkinType.BOW);
        } else if (handItem.getType().toString().contains("SHIELD")) {
            entitySkin.setValue(handItem, SkinType.SWORD);
        }

        entitySkin.setValue(entityEquipment.getItemInOffHand(), SkinType.SHIELD);

        if (setSkin(entitySkin, livingEntity)) {
            if (livingEntity instanceof Player) {
                SkinPacket packet = new SkinPacket(livingEntity);
                ((Player) livingEntity).sendPluginMessage(SkinTags.getInstance(), SkinTags.getChannel(),
                        packet.toString().getBytes());

                for (Entity entity : livingEntity.getNearbyEntities(Bukkit.getViewDistance(), Bukkit.getViewDistance(), Bukkit.getViewDistance())) {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        player.sendPluginMessage(SkinTags.getInstance(), SkinTags.getChannel(),
                                packet.toString().getBytes());
                    }
                }
            }
        }
    }

    private static int getSkinIndex(NBTList tags) {
        int i;
        for (i = 0; i < tags.size(); i++) {
            if (tags.get(i).toString().startsWith(SkinTags.NBTPrefix)) {
                break;
            }
        }
        return i;
    }

    private static EntitySkin getSkin(NBTList tags) {
        EntitySkin entitySkin = new EntitySkin();
        for (Object tag : tags) {
            if (tag.toString().startsWith(SkinTags.NBTPrefix)) {
                entitySkin = new Gson().fromJson(
                        tag.toString().replaceFirst(SkinTags.NBTPrefix, ""),
                        EntitySkin.class);
            }
        }
        return entitySkin;
    }
}
