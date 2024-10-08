package dev.jsinco.lumaitems.manager

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.enums.Action
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface CustomItem {

    val INSTANCE: LumaItems
        get() = LumaItems.getInstance()

    /**
     * Called at startup to initialize and create each custom item
     * @return A pair of the item's nbt key and the itemstack
     */
    fun createItem(): Pair<String, ItemStack>


    /**
     * Called when a listener detects a custom item being used
     * @param type The type of ability to execute
     * @param player The player using the item
     * @param event The event that triggered the ability
     * @return A boolean for return info
     */
    fun executeActions(type: Action, player: Player, event: Any): Boolean
}