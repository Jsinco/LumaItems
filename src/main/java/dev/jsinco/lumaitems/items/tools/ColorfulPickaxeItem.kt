package dev.jsinco.lumaitems.items.tools

import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack

class ColorfulPickaxeItem : CustomItem {
    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#F29595&lC&#F2A58F&lo&#F1B58A&ll&#F1C584&lo&#F1D282&lr&#F1DF7F&lf&#E6E77E&lu&#C9E881&ll &#ACE983&lP&#A0E39C&li&#9DDAC4&lc&#9BD0EC&lk&#B3C0F2&la&#D1AFF0&lx&#F09DED&le",
            mutableListOf("&#f09dedForgiving Touch"),
            mutableListOf("Grants the user the ability to", "silk touch budding amethyst"),
            Material.NETHERITE_PICKAXE,
            mutableListOf("colorfulpickaxe"),
            mutableMapOf(Enchantment.DIG_SPEED to 7, Enchantment.SILK_TOUCH to 1, Enchantment.DURABILITY to 10, Enchantment. MENDING to 1)
        )
        item.tier = "&#FF9A9A&lE&#FFBAA6&la&#FFD9B2&ls&#FFF9BE&lt&#E5FAD4&le&#CAFCE9&lr &#B0FDFF&l2&#C7E8FF&l0&#DED4FF&l2&#F5BFFF&l4"
        return Pair("colorfulpickaxe", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.BREAK_BLOCK -> {
                event as BlockBreakEvent
                if (event.block.type == Material.BUDDING_AMETHYST && player.gameMode != GameMode.CREATIVE) {
                    event.block.world.dropItemNaturally(event.block.location, ItemStack(Material.BUDDING_AMETHYST))
                }
            }
            else -> return false
        }
        return true
    }
}