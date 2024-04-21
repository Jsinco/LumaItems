package dev.jsinco.lumaitems.commands;

import dev.jsinco.lumaitems.LumaItems;
import dev.jsinco.lumaitems.items.astral.sets.FalterSet;
import dev.jsinco.lumaitems.items.astral.sets.MistralSet;
import dev.jsinco.lumaitems.items.astral.upgrades.AstralSetUpgradeFactory;
import dev.jsinco.lumaitems.items.astral.upgrades.AstralSetUpgradeManager;
import dev.jsinco.lumaitems.items.astral.upgrades.AstralUpgradeTier;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class DebugCommand implements SubCommand {
    @Override
    public void execute(@NotNull LumaItems plugin, @NotNull CommandSender sender, @NotNull String[] args) {
        MistralSet mistralSet = new MistralSet();
        ItemStack itemOG = mistralSet.setItems().get(0).clone();
        ItemStack item = itemOG.clone();

        AstralSetUpgradeManager astralSetUpgradeManager = new AstralSetUpgradeManager();
        astralSetUpgradeManager.reloadUpgrades();

        AstralSetUpgradeFactory astralSetUpgradeFactory = new AstralSetUpgradeFactory(item);
        System.out.println(astralSetUpgradeFactory.upgrade());

        if (sender instanceof Player player) {
            player.getInventory().addItem(itemOG);
            player.getInventory().addItem(item);
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
        return "lumaitems.command.debug";
    }

    @Override
    public boolean playerOnly() {
        return false;
    }
}
