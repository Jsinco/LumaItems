package dev.jsinco.lumaitems;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dev.jsinco.lumaitems.commands.CommandManager;
import dev.jsinco.lumaitems.events.Listeners;
import dev.jsinco.lumaitems.events.PassiveListeners;
import dev.jsinco.lumaitems.events.RelicListeners;
import dev.jsinco.lumaitems.hooks.GlowColorPlaceholder;
import dev.jsinco.lumaitems.hooks.PAPIManager;
import dev.jsinco.lumaitems.manager.FileManager;
import dev.jsinco.lumaitems.manager.GlowManager;
import dev.jsinco.lumaitems.manager.ItemManager;
import dev.jsinco.lumaitems.events.AnvilPrevention;
import dev.jsinco.lumaitems.relics.RelicCrafting;
import dev.jsinco.lumaitems.relics.RelicDisassembler;
import dev.jsinco.lumaitems.util.Util;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public final class LumaItems extends JavaPlugin {

    private static LumaItems plugin;
    private static boolean withProtocolLib;
    private final PAPIManager papiManager = new PAPIManager();

    @Override
    public void onEnable() {
        plugin = this;
        FileManager.generateDefaultFiles();
        withProtocolLib = getServer().getPluginManager().getPlugin("ProtocolLib") != null;

        Util.loadUtils();

        PassiveListeners passiveListeners = new PassiveListeners(this);
        ItemManager itemManager = new ItemManager(this);
        try {
            itemManager.registerItems();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Error registering items!", e);
        }

        passiveListeners.startMainRunnable();

        GlowManager.initGlowTeams();

        // Relics
        RelicCrafting.registerRecipes();
        RelicDisassembler.setupDisassemblerBlocks();

        getServer().getPluginManager().registerEvents(new Listeners(this), this);
        getServer().getPluginManager().registerEvents(new AnvilPrevention(this), this);
        getServer().getPluginManager().registerEvents(new RelicListeners(), this);

        getCommand("lumaitems").setExecutor(new CommandManager(this));

        papiManager.addPlaceholder(new GlowColorPlaceholder());
        papiManager.registerPlaceholders();
    }

    @Override
    public void onDisable() {
        if (papiManager.hasRegisteredPlaceholders()){
            papiManager.unregisterPlaceholders();
        }
    }

    public static LumaItems getPlugin() {
        return plugin;
    }

    @Nullable
    public static ProtocolManager getProtocolManager() {
        return withProtocolLib ? ProtocolLibrary.getProtocolManager() : null;
    }
}
