package dev.jsinco.lumaitems.commands

import dev.jsinco.lumaitems.LumaItems
import org.bukkit.command.CommandSender

interface SubCommand {

    fun execute(plugin: LumaItems, sender: CommandSender, args: Array<out String>)

    fun tabComplete(plugin: LumaItems, sender: CommandSender, args: Array<out String>): List<String>?

    fun permission(): String?

    fun playerOnly(): Boolean
}