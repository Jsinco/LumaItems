package dev.jsinco.lumaitems.commands

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.manager.ItemManager
import dev.jsinco.lumaitems.util.Util
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GiveItemCommand : SubCommand {
    private val customItems: MutableMap<String, CustomItem> = mutableMapOf()
    private val customItemsByName: MutableMap<String, ItemStack> = mutableMapOf()

    init {
        refreshItems()
    }

    fun refreshItems() {
        for (customItem in ItemManager.customItems) {
            val item: ItemStack = customItem.value.createItem().second
            if (!item.hasItemMeta()) continue
            customItemsByName[
                ChatColor.stripColor(item.itemMeta.displayName)
                ?.replace(" ", "_")
                ?.lowercase() ?: item.type.toString().lowercase()
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
            Util.giveItem(player, item)
        } else {
            for (customItem in customItems) {
                Util.giveItem(player, customItem.value.createItem().second)
            }
        }
        refreshItems()
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