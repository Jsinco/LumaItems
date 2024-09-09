package dev.jsinco.lumaitems;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dev.jsinco.lumaitems.api.LumaItemsAPI;
import dev.jsinco.lumaitems.commands.CommandManager;
import dev.jsinco.lumaitems.commands.nonsub.UpgradeCMD;
import dev.jsinco.lumaitems.events.ExternalListeners;
import dev.jsinco.lumaitems.events.GeneralListeners;
import dev.jsinco.lumaitems.events.Listeners;
import dev.jsinco.lumaitems.events.PassiveListeners;
import dev.jsinco.lumaitems.guis.AbstractGui;
import dev.jsinco.lumaitems.enums.Action;
import dev.jsinco.lumaitems.manager.FileManager;
import dev.jsinco.lumaitems.manager.GlowManager;
import dev.jsinco.lumaitems.manager.ItemManager;
import dev.jsinco.lumaitems.placeholders.PAPIManager;
import dev.jsinco.lumaitems.relics.RelicCrafting;
import dev.jsinco.lumaitems.relics.RelicDisassembler;
import dev.jsinco.lumaitems.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.logging.Level;

public final class LumaItems extends JavaPlugin {

    // TODO: write a program to go through an replace all legacy rgb with minimessage

    private static LumaItems instance;
    private static boolean withProtocolLib;
    private static boolean withMythicMobs;
    private static PAPIManager papiManager;
    private static PassiveListeners passiveListeners;

    @Override
    public void onEnable() {
        instance = this;
        FileManager.generateDefaultFiles();
        withProtocolLib = getServer().getPluginManager().getPlugin("ProtocolLib") != null;
        withMythicMobs = getServer().getPluginManager().getPlugin("MythicMobs") != null;


        passiveListeners = new PassiveListeners(this);
        final ItemManager itemManager = new ItemManager(this);

        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            log("Players are online, registering items asynchronously");
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                initItemManager(itemManager);
                log("Finished asynchronous item registration!");
            });
        } else {
            initItemManager(itemManager);
        }


        GlowManager.initGlowTeams();
        RelicCrafting.registerRecipes();
        RelicDisassembler.setupDisassemblerBlocks();

        getServer().getPluginManager().registerEvents(new Listeners(this), this);
        getServer().getPluginManager().registerEvents(new GeneralListeners(this), this);
        getServer().getPluginManager().registerEvents(new ExternalListeners(this), this);

        getCommand("lumaitems").setExecutor(new CommandManager(this));
        getCommand("upgrade").setExecutor(new UpgradeCMD());

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            papiManager = new PAPIManager(this);
            papiManager.register();
        }
    }

    private void initItemManager(ItemManager itemManager) {
        try {
            itemManager.registerItems();
            itemManager.registerCustomItemsByName();
            passiveListeners.onPluginAction(Action.PLUGIN_ENABLE); // Fire this as soon as we're done registering our items
            passiveListeners.getPassiveListener(Action.RUNNABLE).runTaskTimer(this, 0L, PassiveListeners.DEFAULT_PASSIVE_LISTENER_TICKS);
            passiveListeners.getPassiveListener(Action.ASYNC_RUNNABLE).runTaskTimerAsynchronously(this, 0L, PassiveListeners.ASYNC_PASSIVE_LISTENER_TICKS);
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "An error occurred while registering items", e);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this); // Immediately disable all listeners to prevent any further events from firing
        passiveListeners.onPluginAction(Action.PLUGIN_DISABLE); // Then fire this for whatever items need to use this


        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory().getTopInventory().getHolder(false) instanceof AbstractGui) {
                player.closeInventory();
            }
        }
        if (papiManager != null) {
            papiManager.unregister();
        }

        try {
            Field singleTonField = LumaItemsAPI.class.getDeclaredField("singleton");
            singleTonField.setAccessible(true);
            if (singleTonField.get(LumaItemsAPI.class) == null) {
                return;
            }
            singleTonField.set(null, null);
            LumaItems.log("API Singleton instance has been reset!");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LumaItems.log("Failed to reset API Singleton instance!", e);
        }
    }

    public static LumaItems getInstance() {
        return instance;
    }

    @Nullable
    public static ProtocolManager getProtocolManager() {
        return withProtocolLib ? ProtocolLibrary.getProtocolManager() : null;
    }

    public static boolean isWithMythicMobs() {
        return withMythicMobs;
    }

    public static void log(String m) {
        Bukkit.getConsoleSender().sendMessage(Util.colorcode("&#f498f6[LumaItems] " + m));
    }

    public static void log(String m, Throwable throwable) {
        log("&#a7d9ff" + m);
        log("&6" + throwable.getMessage());
        for (StackTraceElement ste : throwable.getStackTrace()) {
            log("&#a7d9ff" + ste.toString());
        }
    }
}
