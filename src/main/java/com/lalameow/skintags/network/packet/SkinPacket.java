package com.lalameow.skintags.network.packet;

import com.google.gson.Gson;
import com.lalameow.skintags.api.SkinNBTAPI;
import com.lalameow.skintags.skin.EntitySkin;
import com.lalameow.skintags.SkinTags;
import lombok.Data;
import org.bukkit.entity.Entity;

/**
 * Author: SettingDust.
 * Date: 2018/5/3.
 */
@Data
public class SkinPacket {
    private String UUID;
    private String name;
    private int id;
    private EntitySkin skin;

    private boolean isEmpty = true;

    public SkinPacket(String UUID, String name, int id, EntitySkin skin) {
        this.UUID = UUID;
        this.name = name;
        this.id = id;
        this.skin = skin;
        this.isEmpty = false;
    }

    public SkinPacket(Entity entity) {
        this.UUID = entity.getUniqueId().toString();
        this.name = entity.getName();
        this.id = entity.getEntityId();
        this.skin = SkinNBTAPI.getSkin(entity);
        this.isEmpty = false;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this).replaceFirst(SkinTags.NBTPrefix, "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SkinPacket packet = (SkinPacket) o;

        if (id != packet.id) return false;
        if (isEmpty != packet.isEmpty) return false;
        if (UUID != null ? !UUID.equals(packet.UUID) : packet.UUID != null) return false;
        if (name != null ? !name.equals(packet.name) : packet.name != null) return false;
        return skin != null ? skin.equals(packet.skin) : packet.skin == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (UUID != null ? UUID.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + (skin != null ? skin.hashCode() : 0);
        result = 31 * result + (isEmpty ? 1 : 0);
        return result;
    }
}
