package dev.jsinco.lumaitems.items.test

import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Action
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.particles.ParticleDisplay
import dev.jsinco.lumaitems.particles.Particles
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.awt.Color

class ParticleTestItem : CustomItem {
    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "Particle Test",
            mutableListOf("This is a test item"),
            mutableListOf("This is a test item"),
            Material.NETHERITE_SWORD,
            mutableListOf("particle_test"),
            mutableMapOf(Enchantment.SHARPNESS to 2, Enchantment.MENDING to 1)
        )
        item.tier = "&a&lDebug"
        return Pair("particle_test", item.createItem())
    }

    override fun executeAbilities(type: Action, player: Player, event: Any): Boolean {
        when (type) {
            Action.RIGHT_CLICK -> {
                val location = player.location

                val particleDisplayCloud = ParticleDisplay.of(Particle.DUST).withLocation(location).withColor(Color.CYAN)
                //Particles.vortex(INSTANCE, 30, 4.0, particleDisplayCloud)
                //diamond(double radiusRate, double rate, double height, ParticleDisplay display)
                //Particles.sphere(3.0, 30.0, particleDisplayCloud.mixWith(Color.BLUE))
                // Particles.rainbow(double radius, double rate, double curve, double layers, double compact, ParticleDisplay display)
                //Particles.rainbow(3.0, 3.0, 1.0, 3.0, 0.0, particleDisplayCloud)
                //Particles.flower(3, 15.0, particleDisplayCloud) {
                //    Particles.sphere(3.0, 30.0, particleDisplayCloud.mixWith(Color.PINK))
                //}
                return true
            }
            else -> return false
        }
    }

}