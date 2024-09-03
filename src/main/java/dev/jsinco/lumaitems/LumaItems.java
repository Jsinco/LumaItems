package dev.jsinco.lumaitems;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dev.jsinco.lumaitems.commands.CommandManager;
import dev.jsinco.lumaitems.commands.nonsub.UpgradeCMD;
import dev.jsinco.lumaitems.events.ExternalListeners;
import dev.jsinco.lumaitems.events.GeneralListeners;
import dev.jsinco.lumaitems.events.Listeners;
import dev.jsinco.lumaitems.events.PassiveListeners;
import dev.jsinco.lumaitems.guis.AbstractGui;
import dev.jsinco.lumaitems.manager.Action;
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

import java.util.logging.Level;

public final class LumaItems extends JavaPlugin {

    // TODO: write a program to go through an replace all legacy rgb with minimessage

    private static LumaItems plugin;
    private static boolean withProtocolLib;
    private static boolean withMythicMobs;
    private static PAPIManager papiManager;
    private static PassiveListeners passiveListeners;

    @Override
    public void onEnable() {
        plugin = this;
        FileManager.generateDefaultFiles();
        Util.loadUtils();
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
            itemManager.initPhysicalItemsByName();
            passiveListeners.getPassiveListener(Action.RUNNABLE).runTaskTimer(this, 0L, PassiveListeners.DEFAULT_PASSIVE_LISTENER_TICKS);
            passiveListeners.getPassiveListener(Action.ASYNC_RUNNABLE).runTaskTimerAsynchronously(this, 0L, PassiveListeners.ASYNC_PASSIVE_LISTENER_TICKS);
            passiveListeners.onPluginAction(Action.PLUGIN_ENABLE);
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "An error occurred while registering items", e);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        passiveListeners.onPluginAction(Action.PLUGIN_DISABLE);

        if (papiManager != null) {
            papiManager.unregister();
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory().getTopInventory().getHolder(false) instanceof AbstractGui) {
                player.closeInventory();
            }
        }
        HandlerList.unregisterAll(this);
    }

    public static LumaItems getPlugin() {
        return plugin;
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
}
