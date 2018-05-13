package com.lalameow.skintags.skin;

import com.google.gson.Gson;
import com.lalameow.skintags.api.SkinNBTAPI;
import com.lalameow.skintags.SkinTags;
import lombok.Data;
import me.dpohvar.powernbt.api.NBTCompound;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Author: SettingDust.
 * Date: 2018/4/1.
 */
@Data
public class EntitySkin {
    private String arrow;
    private String block;
    private String bow;
    private String chest;
    private String feet;
    private String head;
    private String legs;
    private String shield;
    private String sword;
    private String wings;


    public void setValue(ItemStack itemStack, SkinType skinType) {
        String skinID = null;
        if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
            NBTCompound nbt = SkinNBTAPI.getSkin(itemStack);
            if (nbt != null) {
                skinID = nbt.getString("ID");
            }
        }
        setValue(skinID, skinType);
    }

    public void setValue(String id, SkinType skinType) {
        switch (skinType) {
            case SWORD:
                this.setSword(id);
                break;
            case CHEST:
                this.setChest(id);
                break;
            case BOW:
                this.setBow(id);
                break;
            case FEET:
                this.setFeet(id);
                break;
            case HEAD:
                this.setHead(id);
                break;
            case LEGS:
                this.setLegs(id);
                break;
            case ARROW:
                this.setArrow(id);
                break;
            case BLOCK:
                this.setBlock(id);
                break;
            case WINGS:
                this.setWings(id);
                break;
            case SHIELD:
                this.setShield(id);
                break;
        }
    }

    @Override
    public String toString() {
        return SkinTags.NBTPrefix + new Gson().toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        EntitySkin that = (EntitySkin) o;

        if (arrow != null ? !arrow.equals(that.arrow) : that.arrow != null) return false;
        if (block != null ? !block.equals(that.block) : that.block != null) return false;
        if (bow != null ? !bow.equals(that.bow) : that.bow != null) return false;
        if (chest != null ? !chest.equals(that.chest) : that.chest != null) return false;
        if (feet != null ? !feet.equals(that.feet) : that.feet != null) return false;
        if (head != null ? !head.equals(that.head) : that.head != null) return false;
        if (legs != null ? !legs.equals(that.legs) : that.legs != null) return false;
        if (shield != null ? !shield.equals(that.shield) : that.shield != null) return false;
        if (sword != null ? !sword.equals(that.sword) : that.sword != null) return false;
        return wings != null ? wings.equals(that.wings) : that.wings == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (arrow != null ? arrow.hashCode() : 0);
        result = 31 * result + (block != null ? block.hashCode() : 0);
        result = 31 * result + (bow != null ? bow.hashCode() : 0);
        result = 31 * result + (chest != null ? chest.hashCode() : 0);
        result = 31 * result + (feet != null ? feet.hashCode() : 0);
        result = 31 * result + (head != null ? head.hashCode() : 0);
        result = 31 * result + (legs != null ? legs.hashCode() : 0);
        result = 31 * result + (shield != null ? shield.hashCode() : 0);
        result = 31 * result + (sword != null ? sword.hashCode() : 0);
        result = 31 * result + (wings != null ? wings.hashCode() : 0);
        return result;
    }
}
