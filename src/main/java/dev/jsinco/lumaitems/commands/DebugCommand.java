package dev.jsinco.lumaitems.commands;

import dev.jsinco.lumaitems.LumaItems;
import dev.jsinco.lumaitems.items.astral.upgrades.AstralSetUpgradeFactory;
import dev.jsinco.lumaitems.items.astral.upgrades.AstralSetUpgradeManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DebugCommand implements SubCommand {
    @Override
    public void execute(@NotNull LumaItems plugin, @NotNull CommandSender sender, @NotNull String[] args) {


        AstralSetUpgradeManager astralSetUpgradeManager = new AstralSetUpgradeManager();
        astralSetUpgradeManager.reloadUpgrades();

        Player player = (Player) sender;

        ItemStack item = player.getInventory().getItemInMainHand();

        AstralSetUpgradeFactory factory = new AstralSetUpgradeFactory(item);
        System.out.println(factory.upgrade());
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
        return true;
    }
}
