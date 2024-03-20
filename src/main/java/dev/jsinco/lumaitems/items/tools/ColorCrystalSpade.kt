package dev.jsinco.lumaitems.items.tools

import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import org.bukkit.Material
import org.bukkit.block.Block
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
        TODO("Not yet implemented")
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.BREAK_BLOCK -> {
                event as BlockBreakEvent
                val block = event.block
                if (block.type == Material.SAND || block.type == Material.RED_SAND) {
                    block.world.dropItemNaturally(block.location, ItemStack(glassTypes.random()))
                }
            }
            else -> return false
        }
        return true
    }

}