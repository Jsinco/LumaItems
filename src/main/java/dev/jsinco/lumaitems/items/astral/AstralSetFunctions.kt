package dev.jsinco.lumaitems.items.astral

import dev.jsinco.lumaitems.manager.CustomItemFunctions
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

abstract class AstralSetFunctions : CustomItemFunctions() {

    companion object {
        private val blankItem: ItemStack = ItemStack(Material.AIR)
    }

    /**
     * This method shouldn't be used for astral sets.
     * @see setItems
     */
    override fun createItem(): Pair<String, ItemStack> {
        return Pair(identifier(), blankItem)
    }

    /**
     * Set the items for the set
     * @return A list of itemstacks
     */
    abstract fun setItems(): List<ItemStack>

    /**
     * Set the identifier for the set
     * @return A string identifier
     */
    abstract fun identifier(): String
}
