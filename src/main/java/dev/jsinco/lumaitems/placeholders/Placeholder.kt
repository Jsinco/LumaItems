package dev.jsinco.lumaitems.placeholders

import dev.jsinco.lumaitems.LumaItems
import org.bukkit.OfflinePlayer

interface Placeholder {
    fun onReceivedRequest(plugin: LumaItems, player: OfflinePlayer?, args: List<String>): String?
}