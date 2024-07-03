package dev.jsinco.lumaitems.obj;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class FCircle {

    private final Location center;
    private final int size;
    private final int segment;

    public FCircle(Location center, int size, int segment) {
        this.center = center;
        this.size = size;
        this.segment = segment;
    }


    public List<Block> blockList() {
        List<Block> bL = new ArrayList<>();
        for (int radius = 0; radius < size; radius++) {
            for (int i = 0; i < 360; i += 360 / segment) {
                double angle = (i * Math.PI / 180);
                double x = Math.round(radius * Math.cos(angle));
                double z = Math.round(radius * Math.sin(angle));
                Location loc = center.clone().add(x, 0, z);
                bL.add(loc.getBlock());
            }
        }
        return bL;
    }

    // idk if this works i copied this from chatgpt thanks for the math explanation
    public int getTotalBlockSize() {
        return size * segment;
    }


}
