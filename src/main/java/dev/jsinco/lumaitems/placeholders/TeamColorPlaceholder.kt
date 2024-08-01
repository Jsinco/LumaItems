package dev.jsinco.lumaitems.placeholders

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.manager.GlowManager
import dev.jsinco.lumaitems.util.Util
import org.bukkit.OfflinePlayer

class TeamColorPlaceholder : Placeholder {

    override fun onReceivedRequest(plugin: LumaItems, player: OfflinePlayer?, args: List<String>): String {
        return Util.getColorCodeByChatColor(player?.player?.let { GlowManager.getGlowColor(it) } ?: return "§f")
    }
}