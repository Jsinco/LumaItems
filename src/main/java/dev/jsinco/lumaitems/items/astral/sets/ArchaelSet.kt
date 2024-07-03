package dev.jsinco.lumaitems.items.astral.sets

import dev.jsinco.lumaitems.items.astral.AstralSet
import dev.jsinco.lumaitems.items.astral.AstralSetFactory
import dev.jsinco.lumaitems.manager.Action
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ArchaelSet : AstralSet {

    override fun setItems(): List<ItemStack> {
        val astralSetFactory = AstralSetFactory("Archael")

        astralSetFactory.astralSetItem(
            Material.ELYTRA,
            mutableMapOf(Enchantment.PROTECTION_ENVIRONMENTAL to 6, Enchantment.PROTECTION_PROJECTILE to 5, Enchantment.DURABILITY to 5,
                Enchantment.PROTECTION_FALL to 4),
            mutableListOf(),
            true,
            null,
            "&#AC87FB&lArchael &fWings",
            null
        )

        return astralSetFactory.createdAstralItems

    }

    override fun executeAbilities(type: Action, player: Player, event: Any): Boolean {
        return false
    }

    override fun identifier(): String {
        return "archael-set"
    }
}