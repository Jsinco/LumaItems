package dev.jsinco.lumaitems.items.astral.sets

import dev.jsinco.lumaitems.items.astral.AstralSet
import dev.jsinco.lumaitems.manager.Ability
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class BlitzSet : AstralSet {

    override fun setItems(): List<ItemStack> {
        TODO("Not yet implemented")
    }

    override fun identifier(): String {
        return "blitz-set"
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        TODO("Not yet implemented")
    }
}