package dev.jsinco.lumaitems.guis

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class GUIHolder(
    val guiClass: GUI
) : InventoryHolder {


    override fun getInventory(): Inventory {
        TODO("Not yet implemented")
    }
}