package dev.jsinco.lumaitems.obj;

import dev.jsinco.lumaitems.LumaItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class HollowSphere {

    private final List<Location> locations = new ArrayList<>();


    private final Location center;
    private final double radius;
    private final double density;

    public HollowSphere(Location center, double radius, double density, boolean async) {
        this.center = center;
        this.radius = radius;
        this.density = density;

        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(LumaItems.getPlugin(), this::init);
        } else {
            init();
        }
    }

    private void init() {
        for (double x = -radius; x < radius; x += density) {
            for (double z = -radius; z < radius; z += density) {
                final double y = Math.sqrt(radius*radius - x*x - z*z); // TODO: Resource intensive?
                locations.add(center.clone().subtract(-x, y, -z));
                locations.add(center.clone().add(-x, y, -z));
            }
        }
    }

    public List<Location> getLocations() {
        return locations;
    }

    public Location getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    public double getDensity() {
        return density;
    }
}
