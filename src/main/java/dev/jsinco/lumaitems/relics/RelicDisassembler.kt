package dev.jsinco.lumaitems.relics

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.manager.FileManager
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*
import kotlin.random.Random

object RelicDisassembler {
    val disassemblerBlocks: MutableList<Block> = mutableListOf()
    private val file = FileManager("relics.yml").generateYamlFile()

    private val confirmCooldownTasks: MutableMap<UUID, Int> = mutableMapOf()
    private val plugin: LumaItems = LumaItems.getPlugin()

    @JvmStatic fun setupDisassemblerBlocks() {
        if (file.getConfigurationSection("disassembler.blocks") == null) {
            plugin.logger.warning("disassembler.blocks config section is null!")
            return
        }
        for (key in file.getConfigurationSection("disassembler.blocks")?.getKeys(false)!!) {
            val loc = Location(
                Bukkit.getWorld(file.getString("disassembler.blocks.$key.world")!!),
                file.getDouble("disassembler.blocks.$key.x"),
                file.getDouble("disassembler.blocks.$key.y"),
                file.getDouble("disassembler.blocks.$key.z")
            ).toCenterLocation()

            disassemblerBlocks.add(loc.block)
        }
    }

    // returns a command to be executed
    fun getCommandToExecute(itemStack: ItemStack, action: Action, player: Player): String? {
        val rarity = Rarity.valueOf(
            itemStack.itemMeta?.persistentDataContainer?.get(
                NamespacedKey(plugin, "relic-rarity"),
                PersistentDataType.STRING
            ) ?: return null
        )
        if (!rescheduleCooldownTask(player)) {
            return null
        } else if (rarity == Rarity.ASTRAL && !action.isLeftClick) {
            player.sendMessage(Util.colorcode("${Util.prefix} You must left click to disassemble &#F7FFC9Astral &#E2E2E2Relics"))
            return null
        }

        val weight = rarity.algorithmWeight * 5


        val commands: MutableMap<Int, String> = mutableMapOf()
        val configSec = file.getConfigurationSection("disassembler.commands")?.getKeys(false) ?: return null
        for (key in configSec) {
            val chance = Integer.parseInt(key)
            if (chance == 0) continue
            commands[chance] = file.getString("disassembler.commands.$key") ?: "non"
        }
        if (rarity == Rarity.ASTRAL) {
            commands[100] = "lumaitems relic %player% core astral"
        }

        // TODO: rework formula
        val selectedCommand: String
        for (commandWeight in commands.keys) {
            if (commandWeight > Random.nextInt(100)) {
                selectedCommand = commands[commandWeight] ?: continue
                return selectedCommand.replace("%player%", player.name)
            }
        }
        Bukkit.broadcastMessage("debug")
        return commands.values.random().replace("%player%", player.name)
    }

    private fun rescheduleCooldownTask(player: Player): Boolean {
        var returnValue = false
        if (confirmCooldownTasks.contains(player.uniqueId)) {
            Bukkit.getScheduler().cancelTask(confirmCooldownTasks[player.uniqueId]!!)
            returnValue = true
        } else {
            player.sendMessage("${Util.prefix} Are you sure you want to disassemble this item? Click again to confirm.")
        }

        confirmCooldownTasks[player.uniqueId] = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
            confirmCooldownTasks.remove(player.uniqueId)
        }, 200L)
        return returnValue
    }
}