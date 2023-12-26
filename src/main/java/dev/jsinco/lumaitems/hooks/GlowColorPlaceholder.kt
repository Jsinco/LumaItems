package dev.jsinco.lumaitems.hooks

import dev.jsinco.lumaitems.manager.GlowManager
import dev.jsinco.lumaitems.util.Util
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

class GlowColorPlaceholder : PlaceholderExpansion() {
    override fun getIdentifier(): String {
        return "lumaitems"
    }

    override fun getAuthor(): String {
        return "Jsinco"
    }

    override fun getVersion(): String {
        return "1.2.0"
    }


    override fun onPlaceholderRequest(player: Player, params: String): String? {
        if (params == "glowcolor") {
            val color = GlowManager.getGlowColor(player) ?: return "§f"
            return Util.getColorCodeByChatColor(color)
        }
        return null
    }

}