package dev.jsinco.lumaitems.items.astral

import dev.jsinco.lumaitems.manager.CustomItem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

interface AstralSet : CustomItem {
    companion object {
        private val blankItem: ItemStack = ItemStack(Material.AIR)
    }
    fun setItems(): List<ItemStack>
    fun identifier(): String
    override fun createItem(): Pair<String, ItemStack> {
        return Pair(identifier(), blankItem)
    }
}