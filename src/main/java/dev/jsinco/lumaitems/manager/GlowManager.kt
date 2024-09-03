package dev.jsinco.lumaitems.manager

import dev.jsinco.lumaitems.LumaItems
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team
import java.util.UUID


object GlowManager {

    private val plugin: LumaItems = LumaItems.getInstance()
    val glowColors: List<ChatColor> = listOf(
        ChatColor.AQUA, ChatColor.BLACK, ChatColor.BLUE, ChatColor.DARK_AQUA,
        ChatColor.DARK_BLUE, ChatColor.DARK_GRAY, ChatColor.DARK_GREEN, ChatColor.DARK_PURPLE,
        ChatColor.DARK_RED, ChatColor.GOLD, ChatColor.GRAY, ChatColor.GREEN,
        ChatColor.LIGHT_PURPLE, ChatColor.RED, ChatColor.YELLOW
    )

    private val board = Bukkit.getScoreboardManager().mainScoreboard
    val teams: MutableList<Team> = mutableListOf()

    val playerTeamsTasks: MutableMap<UUID, Int> = mutableMapOf()

    @JvmStatic
    fun initGlowTeams() {
        for (glowColor in glowColors) {
            val team = board.getTeam(glowColor.name)
            if (team == null) {
                board.registerNewTeam(glowColor.name).color = glowColor
                teams.add(board.getTeam(glowColor.name)!!)
            }
        }
    }


    fun setGlowColor(entity: Entity, glowColor: ChatColor) {
        val team = board.getTeam(glowColor.name) ?: run {
            board.registerNewTeam(glowColor.name).color = glowColor
            teams.add(board.getTeam(glowColor.name)!!)
            board.getTeam(glowColor.name)
        }
        team?.addEntry(entity.uniqueId.toString())
    }

    fun removeGlowColor(entity: Entity) {
        val team = board.getEntityTeam(entity) ?: return
        team.removeEntry(entity.uniqueId.toString())
    }

    fun addToTeamForTicks(entity: Entity, glowColor: ChatColor, ticks: Long) {
        setGlowColor(entity, glowColor)
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
            removeGlowColor(entity)
            board.getEntityTeam(entity)?.addEntry(entity.uniqueId.toString())
        }, ticks)
    }

    fun addToTeamForTicks(player: Player, glowColor: ChatColor, ticks: Long) {
        val uuid = player.uniqueId
        if (playerTeamsTasks.contains(uuid)) {
            Bukkit.getScheduler().cancelTask(playerTeamsTasks[uuid]!!)
        }

        setGlowColor(player, glowColor)

        playerTeamsTasks[uuid] = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
            removeGlowColor(player)
        }, ticks)
    }

    fun getGlowColor(entity: Entity): ChatColor? {
        return board.getEntityTeam(entity)?.color
    }
}