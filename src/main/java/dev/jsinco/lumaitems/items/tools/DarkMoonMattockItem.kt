package dev.jsinco.lumaitems.items.tools

import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.items.CreateItem
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.util.AbilityUtil.breakThreeByThree
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack

class DarkMoonMattockItem : CustomItem {
    override fun createItem(): Pair<String, ItemStack> {
        val item = CreateItem(
            "&#35306d&lD&#45417c&la&#56518b&lr&#66629a&lk&#7673a8&lm&#8684b7&lo&#9794c6&lo&#a7a5d5&ln &#a9a2d4&lM&#ac9fd2&la&#ae9cd1&lt&#b09acf&lt&#b297ce&lo&#b594cc&lc&#b791cb&lk",
            mutableListOf("&7Unbreakable","&#5a6bc6D&#5867c1e&#5763bcs&#555fb7t&#535bb2r&#5257aeu&#5052a9c&#4e4ea4t&#4c4a9fi&#4b469av&#494295e"),
            mutableListOf("&#2d273c\"&#322b43M&#373049a&#3c3450y &#413857t&#463d5dh&#4b4164e &#50456bm&#554a72o&#5a4e78o&#5f527fn &#645686k&#695b8cn&#6e5f93o&#73639aw &#7868a0a&#7d6ca7l&#806fabl &#8371afy&#8674b3o&#8977b8u&#8c79bcr &#8f7cc0s&#927fc4e&#9682c8c&#9984ccr&#9c87d0e&#9f8ad4t&#a28cd9s&#a58fdd.&#a892e1.&#ab94e5.&#ae97e9\"","","§fBreaks blocks in a 3x3 radius"),
            Material.NETHERITE_PICKAXE,
            mutableListOf("darkmoonmattock","cuboid"),
            mutableMapOf(Enchantment.DIG_SPEED to 8, Enchantment.SILK_TOUCH to 1)
        )
        item.unbreakable = true
        return Pair("darkmoonmattock", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        val blockBreakEvent: BlockBreakEvent? = event as? BlockBreakEvent

        when (type) {
            Ability.BREAK_BLOCK -> {
                breakThreeByThree(blockBreakEvent!!.block, player, null)
            }
            else -> return false
        }
        return true
    }
}