package dev.jsinco.lumaitems.items.test

import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.enums.Action
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.inventory.ItemStack

class CarnivalFishingRodItem : CustomItem {
    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "Carnival Fishing Rod",
            mutableListOf("This is a test item"),
            mutableListOf("This is a test item"),
            Material.FISHING_ROD,
            mutableListOf("carnivalfishingrod"),
            mutableMapOf(Enchantment.LURE to 4, Enchantment.MENDING to 1)
        )
        item.tier = "&a&lDebug"
        return Pair("carnivalfishingrod", item.createItem())
    }

    override fun executeActions(type: Action, player: Player, event: Any): Boolean {

        when (type) {
            Action.FISH -> {
                event as PlayerFishEvent
                val item = event.caught as? Item ?: return false
                if (!item.itemStack.type.toString().contains("APPLE")) {
                    player.sendMessage(Util.colorcode("""
                        &eWell, this is awkward... I'm catching something but it isn't an apple. I'm not a really big fan of fish that
                        aren't apples. Why do we even catch fish that aren't apples? I don't know. I'm just a fishing rod. I don't really want to be used
                        for fishing anyways. Why was I made to catch apples anyways? Can you just put me back, I don't want to be a fishing rod anymore.
                    """.trimIndent()))
                    event.isCancelled = true
                }
            }
            else -> return false
        }
        return true
    }
}