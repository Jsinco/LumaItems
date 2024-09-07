package dev.jsinco.lumaitems.items.test

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.enums.Action
import dev.jsinco.lumaitems.enums.Tier
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.CustomItem
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack

class CarnivalMiningPickaxeItem : CustomItem {

    companion object {
        private const val KEY = "carnivalminingpickaxe"
        private val nameSpace = NamespacedKey(LumaItems.getInstance(), KEY)
        private val blockBreakers: MutableMap<Player, Int> = mutableMapOf()
    }

    override fun createItem(): Pair<String, ItemStack> {
        return ItemFactory.builder()
            .name("Carnival Mining Pickaxe")
            .customEnchants(mutableListOf("&aThis is a test item"))
            .lore(mutableListOf("This is a test item"))
            .material(Material.DIAMOND_PICKAXE)
            .persistentData(KEY)
            .vanillaEnchants(mutableMapOf(Enchantment.UNBREAKING to 5))
            .tier(Tier.CARNIVAL_2024)
            .buildPair()
    }

    override fun executeActions(type: Action, player: Player, event: Any): Boolean {
        when (type) {
            Action.BREAK_BLOCK -> {
                event as BlockBreakEvent
                event.isCancelled = true
                val amt = blockBreakers.getOrDefault(player, 0)
                blockBreakers[player] = amt + 1
            }

            Action.RUNNABLE -> {
//                for (blockBreaker in blockBreakers) {
//                    if (!blockBreaker.key.isOnline) {
//                        continue
//                    }
//
//                    //val
//                }
            }
            else -> return false
        }
        return true
    }

}
