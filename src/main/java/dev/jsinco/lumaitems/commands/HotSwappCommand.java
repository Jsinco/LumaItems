package dev.jsinco.lumaitems.commands;

import dev.jsinco.lumaitems.LumaItems;
import dev.jsinco.lumaitems.manager.ItemManager;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;

public class HotSwappCommand implements SubCommand {
    @Override
    public void execute(@NotNull LumaItems plugin, @NotNull CommandSender sender, @NotNull String[] args) {
        ItemManager itemManager = new ItemManager(plugin);
        try {
            itemManager.clearAllItems();
            itemManager.registerItems();
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error registering items!", e);
        }
    }

    @Nullable
    @Override
    public List<String> tabComplete(@NotNull LumaItems plugin, @NotNull CommandSender sender, @NotNull String[] args) {
        return null;
    }

    @Nullable
    @Override
    public String permission() {
        return "lumaitems.command.hotswapp";
    }

    @Override
    public boolean playerOnly() {
        return false;
    }
}
