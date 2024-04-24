package dev.jsinco.lumaitems;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dev.jsinco.lumaitems.commands.CommandManager;
import dev.jsinco.lumaitems.events.AnvilPrevention;
import dev.jsinco.lumaitems.events.GeneralListeners;
import dev.jsinco.lumaitems.events.Listeners;
import dev.jsinco.lumaitems.events.PassiveListeners;
import dev.jsinco.lumaitems.guis.AbstractGui;
import dev.jsinco.lumaitems.manager.Ability;
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

    private static LumaItems plugin;
    private static boolean withProtocolLib;
    private static boolean withMythicMobs;
    private static PAPIManager papiManager;

    @Override
    public void onEnable() {
        plugin = this;
        FileManager.generateDefaultFiles();
        Util.loadUtils();
        withProtocolLib = getServer().getPluginManager().getPlugin("ProtocolLib") != null;
        withMythicMobs = getServer().getPluginManager().getPlugin("MythicMobs") != null;


        final ItemManager itemManager = new ItemManager(this);
        final PassiveListeners passiveListeners = new PassiveListeners(this);
        try {
            itemManager.registerItems();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "An error occurred while registering items", e);
        }
        passiveListeners.getPassiveListener(Ability.RUNNABLE).runTaskTimer(this, 0L, PassiveListeners.DEFAULT_PASSIVE_LISTENER_TICKS);
        passiveListeners.getPassiveListener(Ability.ASYNC_RUNNABLE).runTaskTimerAsynchronously(this, 0L, PassiveListeners.ASYNC_PASSIVE_LISTENER_TICKS);


        GlowManager.initGlowTeams();
        RelicCrafting.registerRecipes();
        RelicDisassembler.setupDisassemblerBlocks();

        getServer().getPluginManager().registerEvents(new Listeners(this), this);
        getServer().getPluginManager().registerEvents(new AnvilPrevention(this), this);
        getServer().getPluginManager().registerEvents(new GeneralListeners(this), this);

        getCommand("lumaitems").setExecutor(new CommandManager(this));

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            papiManager = new PAPIManager(this);
            papiManager.register();
        }
    }

    @Override
    public void onDisable() {
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
}
