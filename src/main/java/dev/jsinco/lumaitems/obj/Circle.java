package dev.jsinco.lumaitems.obj;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class Circle {

    private final Location center;
    private final int radius;

    public Circle(Location center, int radius) {
        this.center = center;
        this.radius = radius;
    }

    public List<Block> blockList() {
        List<Block> bL = new ArrayList<>(this.getTotalBlockSize());

        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();
        World w = center.getWorld();
        int rSquared = radius * radius;
        for (int x = cx - radius; x <= cx + radius; x++) {
            for (int z = cz - radius; z <= cz + radius; z++) {
                if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {
                    bL.add(w.getBlockAt(x, cy, z));
                }
            }
        }
        return bL;
    }

    public int getTotalBlockSize() {
        return (int) (this.getCircumference() / (2 * Math.PI));
    }

    public int getCircumference() {
        return (int) (2 * Math.PI * radius);
    }

    public boolean isIn(Entity entity) {
        return isIn(entity.getLocation());
    }

    public boolean isIn(Block block) {
        return isIn(block.getLocation());
    }

    public boolean isIn(Location loc) {
        int cx = center.getBlockX();
        int cz = center.getBlockZ();
        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        return (cx - x) * (cx - x) + (cz - z) * (cz - z) <= radius * radius;
    }

    public Location getCenter() {
        return center;
    }

    public int getRadius() {
        return radius;
    }
}
