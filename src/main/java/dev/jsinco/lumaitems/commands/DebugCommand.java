package dev.jsinco.lumaitems.commands;

import dev.jsinco.lumaitems.LumaItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DebugCommand implements SubCommand {
    @Override
    public void execute(@NotNull LumaItems plugin, @NotNull CommandSender sender, @NotNull String[] args) {
        Player player = (Player) sender;

        int size = Integer.parseInt(args[1]);
        int seg = Integer.parseInt(args[2]);
        spawnCircle(size, player.getLocation(), seg);
    }

    public void spawnCircle(int size, Location center, int segment){
        for (int radius = 0; radius < size; radius++) {
            for (int i = 0; i < 360; i += 360 / segment) {
                double angle = (i * Math.PI / 180);
                double x = Math.round(radius * Math.cos(angle));
                double z = Math.round(radius * Math.sin(angle));
                Location loc = center.clone().add(x, -1, z);
                loc.getBlock().setType(Material.DIAMOND_BLOCK);
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
        return true;
    }
}
