package dev.jsinco.lumaitems.commands;

import dev.jsinco.lumaitems.LumaItems;
import dev.jsinco.lumaitems.items.astral.upgrades.AstralSetUpgradeManager;
import dev.jsinco.lumaitems.items.astral.upgrades.AstralUpgradeTier;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class DebugCommand implements SubCommand {
    @Override
    public void execute(@NotNull LumaItems plugin, @NotNull CommandSender sender, @NotNull String[] args) {
        AstralSetUpgradeManager astralSetUpgradeManager = new AstralSetUpgradeManager();
        astralSetUpgradeManager.reloadUpgrades();
        Map<String, List<AstralUpgradeTier>> listMap = AstralSetUpgradeManager.getUpgrades();
        for (Map.Entry<String, List<AstralUpgradeTier>> astralUpgradeTierEntry : listMap.entrySet()) {
            sender.sendMessage(astralUpgradeTierEntry.getKey());
            for (AstralUpgradeTier astralUpgradeTier : astralUpgradeTierEntry.getValue()) {
                sender.sendMessage(astralUpgradeTier.toString());
            }
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
