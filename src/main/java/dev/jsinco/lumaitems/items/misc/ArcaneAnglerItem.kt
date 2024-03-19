package dev.jsinco.lumaitems.items.misc

import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.Sound
import org.bukkit.Statistic
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.math.max

class ArcaneAnglerItem : CustomItem {
    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#cdf9fb&lA&#c5f1fb&lr&#bde8fc&lc&#b5e0fc&la&#add8fc&ln&#a5d0fd&le &#9cc7fd&lA&#94bffe&ln&#8cb7fe&lg&#84affe&ll&#7ca6ff&le&#749eff&lr",
            mutableListOf("&#cdf9fbW&#b7e2fca&#a1ccfdg&#8ab5fee&#749effr"),
            mutableListOf("&#749eff\"&#79a3ffI&#7ea8fft&#83aefes &#88b3fem&#8db8fea&#93bdfeg&#98c2fdi&#9dc8fdc &#a2cdfds&#a7d2fdt&#acd7fca&#b1dcfcb&#b6e2fci&#bbe7fcl&#c0ecfci&#c5f1fbz&#caf6fbe&#caf6fbs &#c5f1fbf&#c0ecfco&#bbe7fcr&#b6e2fct&#b1dcfcu&#acd7fcn&#a7d2fde&#a2cdfd, &#9dc8fdw&#98c2fdi&#93bdfet&#8db8feh &#88b3fev&#83aefeo&#7ea8ffi&#79a3ffd&#749eff\"","","&fWhile fishing with this rod, you will","&fhave the chance to catch a surplus","&fof fish or lose your catch","","&fYour chances of wagering and a","&ffavorable gamble increase with the","&famount of fish you have caught"),
            Material.FISHING_ROD,
            mutableListOf("arcaneangler"),
            mutableMapOf(Enchantment.LURE to 5, Enchantment.LUCK to 5, Enchantment.DURABILITY to 9, Enchantment.MENDING to 1)
        )
        return Pair("arcaneangler", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        val playerFishEvent: PlayerFishEvent? = event as? PlayerFishEvent

        when (type) {
            Ability.FISH -> {
                if (playerFishEvent!!.state == PlayerFishEvent.State.CAUGHT_FISH) {
                    wager(player, playerFishEvent.caught as Item)
                }
            }
            else -> return false
        }
        return true
    }

    // if someone reaches int limit change to long ig
    private fun wager(player: Player, caught: Item) {
        val chance: Int = if (player.scoreboardTags.contains("lumaitems.debug")) {
            -1
        } else {
            player.getStatistic(Statistic.FISH_CAUGHT) / 1000
        }
        if (Random().nextInt(100) > max(
                (12 + chance).toDouble(),
                20.0
            ) && chance != -1
        ) return  // chance to wager is 12% + 1% per 1000 fish caught, max 20%

        // 70% base chance + 1% per 1000 fish caught
        if (Random().nextInt(100) <= 70 + chance) {
            caught.world.spawnParticle(
                Particle.REDSTONE, caught.location.add(0.0, 1.0, 0.0), 15, 0.5, 0.5, 0.5, 0.1, DustOptions(
                    Color.fromRGB(176, 140, 253), 2f
                )
            )
            caught.world.spawnParticle(
                Particle.REDSTONE, caught.location.add(0.0, 1.0, 0.0), 15, 0.5, 0.5, 0.5, 0.1, DustOptions(
                    Color.fromRGB(189, 244, 251), 2f
                )
            )
            caught.world.playSound(caught.location, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR, 1f, 1f)
            if (caught.itemStack.getMaxStackSize() > 1) caught.itemStack.amount =
                Random().nextInt(2, 5) // set amount to 2-4
        } else {
            caught.world.spawnParticle(
                Particle.REDSTONE, caught.location.add(0.0, 1.0, 0.0), 15, 0.5, 0.5, 0.5, 0.1, DustOptions(
                    Color.fromRGB(255, 10, 10), 2f
                )
            )
            caught.world.spawnParticle(Particle.SPELL_WITCH, caught.location.add(0.0, 1.0, 0.0), 15, 0.5, 0.5, 0.5, 0.1)
            caught.world.playSound(caught.location, Sound.ENTITY_WITCH_CELEBRATE, 0.5f, 1f)
            caught.remove()
        }
    }
}