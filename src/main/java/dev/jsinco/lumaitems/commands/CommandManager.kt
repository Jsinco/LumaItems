package dev.jsinco.lumaitems.commands

import dev.jsinco.lumaitems.LumaItems
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class CommandManager(val plugin: LumaItems) : CommandExecutor, TabCompleter {

    val commands: MutableMap<String, SubCommand> = mutableMapOf()

    init {
        commands["give"] = GiveItemCommand()
        commands["pinatafile"] = PinataFileCommand()
        commands["stellarstar"] = StellarStarGiveCommand()
        commands["debug"] = DebugCommand()
        commands["relic"] = RelicCommand()
        commands["giveastral"] = GiveAstralCommand()
        commands["hotswapp"] = HotSwappCommand()
        commands["addtier"] = AddTier()
        commands["upgrade"] = UpgradeCommand()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) return false

        val subCommand = commands[args[0]] ?: return false

        if (subCommand.playerOnly() && sender !is Player) return false
        else if (subCommand.permission() != null && !sender.hasPermission(subCommand.permission()!!)) return false

        subCommand.execute(plugin, sender, args)
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): List<String>? {
        if (args.size == 1) return commands.keys.toList()

        val subCommand = commands[args[0]] ?: return null

        if (subCommand.playerOnly() && sender !is Player) return null
        else if (subCommand.permission() != null && !sender.hasPermission(subCommand.permission()!!)) return null

        return subCommand.tabComplete(plugin, sender, args)
    }

}