package com.lalameow.skintags;

import com.lalameow.skintags.command.SkinCommand;
import com.lalameow.skintags.listener.InteractListener;
import com.lalameow.skintags.network.handler.EntityHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;


public final class SkinTags extends JavaPlugin {
    public static String NBTPrefix = "SkinData:";
    @Getter
    private static SkinTags instance;

    @Getter
    private static HashMap<UUID, String[]> setMap = new HashMap<>();
    @Getter
    private static HashMap<UUID, String> removeMap = new HashMap<>();
    @Getter
    private static final String channel = "SkinTags";

    @Getter
    private static boolean isRPGInventoryLoaded;

    @Override
    public void onEnable() {
        instance = this;
        isRPGInventoryLoaded = Bukkit.getPluginManager().isPluginEnabled("RPGInventory");
        this.getServer().getPluginManager().registerEvents(new InteractListener(), this);
        this.getCommand("skintags").setExecutor(new SkinCommand());
        this.getServer().getMessenger().registerIncomingPluginChannel(this,
                channel, new EntityHandler());
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, channel);
    }

    @Override
    public void onDisable() {

    }
}
