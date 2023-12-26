package dev.jsinco.lumaitems.items.misc

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.CreateItem
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class RenameTagItem : CustomItem {

    companion object{
        val plugin: LumaItems = LumaItems.getPlugin()
    }

    private lateinit var renameTag: ItemStack

    override fun createItem(): Pair<String, ItemStack> {
        renameTag = Util.createBasicItem(
            "&#a8ff92&lItem Rename &#E2E2E2Tag",
            listOf("&7Right click to use!"),
            Material.NAME_TAG,
            listOf("renametag"),
            glint = true
        )
        return Pair("renametag", renameTag)
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        //val chatEvent: AsyncPlayerChatEvent? = event as? AsyncPlayerChatEvent

        when (type) {
            Ability.RIGHT_CLICK -> {
                //if (player.inventory.itemInMainHand.itemMeta.persistentDataContainer.has(NamespacedKey(plugin, "renametag"), PersistentDataType.SHORT)) {
                //    startRenamingPlayer(player) // Check if the item is in the MAIN HAND, not just anywhere :P
                //}
                player.sendMessage(Util.colorcode("${Util.prefix} Redeem by making a ticket on &#a8ff92https://discord.gg/solacemc"))
            }
            /*
            Ability.CHAT -> {
                playerSentMessage(chatEvent!!.message, player)
                chatEvent.isCancelled = true
            }
             */
            else -> return false
        }
        return true
    }

    private fun startRenamingPlayer(player: Player) {
        player.persistentDataContainer.set(NamespacedKey(plugin, "renametag"), PersistentDataType.SHORT, 1)
        player.inventory.itemInMainHand.amount -= 1
        val msg = Util.colorcode("""
            ${Util.prefix} Hold the item you want to rename in your hand and type the new name in chat.
            ${Util.prefix} You can use &#084cfbh&#5ba0fce&#adf3fdx &#E2E2E2color codes or &bdefault &#E2E2E2Minecraft color codes.
            ${Util.prefix} Type '&ccancel&#E2E2E2' to cancel.
        """.trimIndent())
        player.sendMessage(msg)
    }

    private fun playerSentMessage(msg: String, player: Player) {
        player.persistentDataContainer.remove(NamespacedKey(plugin, "renametag"))
        val item = player.inventory.itemInMainHand
        var preconditionCheck = true

        if (msg == "cancel") {
            player.sendMessage("${Util.prefix} Renaming cancelled!")
            preconditionCheck = false
        } else if (item.type == Material.AIR) {
            player.sendMessage("${Util.prefix} You must be holding an item to rename it!")
            preconditionCheck = false
        }

        if (!preconditionCheck) {
            player.inventory.addItem(renameTag)
            return
        }

        val meta = item.itemMeta!!
        val name = try {
            Util.colorcode(msg)
        } catch (e: Exception) {
            player.sendMessage("${Util.prefix} Invalid color code!")
            player.inventory.addItem(renameTag)
            return
        }

        meta.setDisplayName(name)
        item.itemMeta = meta
        player.sendMessage("${Util.prefix} Item renamed to ${Util.colorcode(msg)}")
    }
}