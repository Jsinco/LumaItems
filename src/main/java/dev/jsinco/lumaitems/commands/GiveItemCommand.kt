package dev.jsinco.lumaitems.commands

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.manager.ItemManager
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GiveItemCommand : SubCommand {
    private val customItems = ItemManager.customItems
    private val customItemsByName: MutableMap<String, ItemStack> = mutableMapOf()

    init {
        for (customItem in customItems) {
            val item: ItemStack = customItem.value.createItem().second
            customItemsByName[
                ChatColor.stripColor(item.itemMeta.displayName)!!
                .replace(" ", "_")
                .lowercase()
            ] = item
        }
    }

    override fun execute(plugin: LumaItems, sender: CommandSender, args: Array<out String>) {
        val player = sender as Player


        val item = if (args[1] != "all") {
            customItemsByName[args[1]] ?: return
        } else {
            null
        }

        if (item != null) {
            player.inventory.addItem(item)
        } else {
            for (customItem in customItems) {
                player.inventory.addItem(customItem.value.createItem().second)
            }
        }
    }

    override fun tabComplete(plugin: LumaItems, sender: CommandSender, args: Array<out String>): List<String> {
        val list: MutableList<String> = customItemsByName.keys.toMutableList()
        list.add("all")
        return list
    }

    override fun permission(): String {
        return "lumaitems.command.give"
    }

    override fun playerOnly(): Boolean {
        return true
    }
}