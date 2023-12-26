package dev.jsinco.lumaitems.guis

import org.bukkit.event.inventory.InventoryClickEvent

interface GUI {

    fun handleClick(event: InventoryClickEvent)
}