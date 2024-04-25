package dev.jsinco.lumaitems.items.astral.sets

import dev.jsinco.lumaitems.items.astral.AstralSet
import dev.jsinco.lumaitems.items.astral.AstralSetFactory
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.obj.HollowSphere
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class BlitzSet : AstralSet {

    override fun setItems(): List<ItemStack> {
        val factory = AstralSetFactory("Blitz", mutableListOf("&#E97979<Unknown>"))

        factory.commonEnchants = mutableMapOf(
            Enchantment.DURABILITY to 9
        ) // TODO: Change to sphere which picks up nearby items?

        /*factory.astralSetItem(
            Material.DIAMOND_SWORD,
            mutableMapOf(Enchantment.DAMAGE_ALL to 6, Enchantment.LOOT_BONUS_MOBS to 3, Enchantment.SILK_TOUCH to 1),
            mutableListOf("When killing mobs, dropped items", "will automatically be placed", "in your inventory")
        )

        factory.astralSetItem(
            Material.DIAMOND_AXE,
            mutableMapOf(Enchantment.DIG_SPEED to 7, Enchantment.LOOT_BONUS_BLOCKS to 5),
            mutableListOf("When breaking blocks, dropped items", "will automatically be placed", "in your inventory")
        )*/

        return factory.createdAstralItems
    }

    override fun identifier(): String {
        return "blitz-set"
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.LEFT_CLICK -> {
                val sphere = HollowSphere(player.eyeLocation, 3.0, 0.5, false)


                return true
            }
            else -> return false
        }
    }

    private fun handleCast(event: PlayerInteractEvent, player: Player) {

    }
}
