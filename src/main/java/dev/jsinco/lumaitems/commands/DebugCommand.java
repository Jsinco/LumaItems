package dev.jsinco.lumaitems.commands;

import dev.jsinco.lumaitems.LumaItems;
import dev.jsinco.lumaitems.guis.AstralUpgradeGUI;
import dev.jsinco.lumaitems.items.astral.SwiftSet;
import dev.jsinco.lumaitems.relics.RelicCreator;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DebugCommand implements SubCommand {
    @Override
    public void execute(@NotNull LumaItems plugin, @NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return;

        /*if (player.getScoreboardTags().contains("lumaitems.debug")) {
            player.removeScoreboardTag("lumaitems.debug");
            player.sendMessage("Debug mode disabled.");
        } else {
            player.addScoreboardTag("lumaitems.debug");
            player.sendMessage("Debug mode enabled.");
        }*/

        for (ItemStack item : new SwiftSet().setItems()) {
            player.getInventory().addItem(item);
        }
        AstralUpgradeGUI.INSTANCE.initGui(player);
    }

    @Nullable
    @Override
    public List<String> tabComplete(@NotNull LumaItems plugin, @NotNull CommandSender sender, @NotNull String[] args) {
        return null;
    }

    @Nullable
    @Override
    public String permission() {
        return "lumaitems.command.debug";
    }

    @Override
    public boolean playerOnly() {
        return false;
    }
}
