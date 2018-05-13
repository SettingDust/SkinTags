package com.lalameow.skintags.skin;

/**
 * Author: SettingDust.
 * Date: 2018/4/2.
 */
public enum SkinType {
    ARROW, BLOCK, BOW, CHEST, FEET, HEAD, LEGS, SHIELD, SWORD, WINGS;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
