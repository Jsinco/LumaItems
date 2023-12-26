package dev.jsinco.lumaitems.items.astral

import org.bukkit.inventory.ItemStack

/**
 * All astral sets are a list of items. (Chestplate, Sword, etc.)
 * They all share a common NBT tag, meaning all items have the same ability
 */
interface AstralSet {
    fun setItems(): List<ItemStack>
}