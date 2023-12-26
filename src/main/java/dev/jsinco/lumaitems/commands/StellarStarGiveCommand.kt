package dev.jsinco.lumaitems.commands

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.misc.StellarStarItem
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

class StellarStarGiveCommand : SubCommand {
    override fun execute(plugin: LumaItems, sender: CommandSender, args: Array<out String>) {
        val player = Bukkit.getPlayerExact(args[1])
        val amount = if (args.size > 2) args[2].toInt() else 1

        if (player == null) {
            sender.sendMessage("${Util.prefix} Player not found!")
            return
        }

        val stellarStar = StellarStarItem().createItem().second
        stellarStar.amount = amount

        player.inventory.addItem(stellarStar)
    }

    override fun tabComplete(plugin: LumaItems, sender: CommandSender, args: Array<out String>): List<String>? {
        if (args.size == 3) {
            return listOf("<amount>")
        }
        return null
    }

    override fun permission(): String? {
        return "lumaitems.command.stellarstar"
    }

    override fun playerOnly(): Boolean {
        return false
    }
}