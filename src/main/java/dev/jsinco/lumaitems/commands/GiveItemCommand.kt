package dev.jsinco.lumaitems.commands

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.manager.ItemManager
import dev.jsinco.lumaitems.util.MiniMessageUtil
import dev.jsinco.lumaitems.util.Util
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GiveItemCommand : SubCommand {

    override fun execute(plugin: LumaItems, sender: CommandSender, args: Array<out String>) {
        val player = if (args.size == 3) {
            plugin.server.getPlayerExact(args[2]) ?: return
        } else {
            sender as Player
        }


        val item = if (args[1] != "all") {
            ItemManager.getItemByName(args[1]) ?: return
        } else {
            null
        }

        if (item != null) {
            Util.giveItem(player, item)
            MiniMessageUtil.msg(player, item.itemMeta?.displayName()?.let { Component.text("You have been given ").append(it) })
        } else {
            for (customItem in ItemManager.getAllItems()) {
                Util.giveItem(player, customItem)
            }
            player.sendMessage("${Util.prefix} You have been given all custom items")
        }
    }

    override fun tabComplete(plugin: LumaItems, sender: CommandSender, args: Array<out String>): List<String>? {
        when (args.size) {
            2 -> {
                val list: MutableList<String> = ItemManager.physicalItemsByName.keys.toMutableList()
                list.add("all")
                return list
            }
        }
        return null
    }

    override fun permission(): String {
        return "lumaitems.command.give"
    }

    override fun playerOnly(): Boolean {
        return false
    }
}