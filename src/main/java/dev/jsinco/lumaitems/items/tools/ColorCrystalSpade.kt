package dev.jsinco.lumaitems.items.tools

import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack

class ColorCrystalSpade : CustomItem {

    companion object {
        val glassTypes: List<Material> = listOf(
                //Material.GLASS,
                //Material.TINTED_GLASS,
                Material.LIGHT_BLUE_STAINED_GLASS,
                Material.BLACK_STAINED_GLASS,
                Material.BLUE_STAINED_GLASS,
                Material.BROWN_STAINED_GLASS,
                Material.CYAN_STAINED_GLASS,
                Material.GRAY_STAINED_GLASS,
                Material.GREEN_STAINED_GLASS,
                Material.LIGHT_GRAY_STAINED_GLASS,
                Material.LIME_STAINED_GLASS,
                Material.MAGENTA_STAINED_GLASS,
                Material.ORANGE_STAINED_GLASS,
                Material.PINK_STAINED_GLASS,
                Material.PURPLE_STAINED_GLASS,
                Material.RED_STAINED_GLASS,
                Material.WHITE_STAINED_GLASS,
                Material.YELLOW_STAINED_GLASS
        )
    }
    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#EE9393&lC&#EE9D90&lo&#EDA68C&ll&#EDB089&lo&#EDBC85&lr &#EDC982&lC&#EDD67E&lr&#E0DD7E&ly&#CCE17F&ls&#B7E480&lt&#A8E38E&la&#A3DBAE&ll &#9DD3CE&lS&#9ECAE8&lp&#B8BAE8&la&#D2ABE9&ld&#EC9BE9&le",
            mutableListOf("&#ec9be9Style"),
            mutableListOf("Breaking sand of any kind will", "automatically convert it to", "smelted and colored glass"),
            Material.NETHERITE_SHOVEL,
            mutableListOf("colorcrystalspade"),
            mutableMapOf(Enchantment.DIG_SPEED to 7, Enchantment.DURABILITY to 8, Enchantment.SILK_TOUCH to 1, Enchantment.MENDING to 1)
        )
        item.tier = "&#FF9A9A&lE&#FFBAA6&la&#FFD9B2&ls&#FFF9BE&lt&#E5FAD4&le&#CAFCE9&lr &#B0FDFF&l2&#C7E8FF&l0&#DED4FF&l2&#F5BFFF&l4"
        return Pair("colorcrystalspade", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.BREAK_BLOCK -> {
                event as BlockBreakEvent
                val block = event.block
                if (block.type == Material.SAND || block.type == Material.RED_SAND) {
                    block.world.dropItemNaturally(block.location, ItemStack(glassTypes.random()))
                    event.isDropItems = false
                }
            }
            else -> return false
        }
        return true
    }

}