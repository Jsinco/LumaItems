package dev.jsinco.lumaitems.items.misc

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.util.Util
import dev.jsinco.lumaitems.util.Util.createBasicItem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

class HallomaresSwapTeamsItem : CustomItem {


    private val plugin = LumaItems.getPlugin()
    private val msg = Util.colorcode("""
                ${Util.prefix} In chat, type the new name of your team. Options are:
                &7- &#220000d&#2d0101a&#370202r&#420303k_&#4f0303i&#5f0202m&#700101p&#800000s
                &7- &#fb3a20s&#fb4b21u&#fc5c22n&#fc6c23l&#fc7d24i&#fd8e25t&#fd9f26_&#fca225s&#fa9722p&#f88d1fi&#f7821cr&#f57819i&#f36d16t&#f16313s
                &7- &#c1b2ffs&#b8adf4a&#b0a9e9p&#a7a4dep&#9ea0d4h&#959bc9i&#8d97ber&#8492b3e_&#8b98b7s&#929fbch&#99a5c0a&#a0abc4d&#a7b1c8o&#aeb8cdw&#b5bed1s
                &7- &#5c3170w&#663e76o&#704b7dn&#795883d&#836589e&#767085r_&#697c81w&#5c877ci&#4f9278t&#5da67ec&#6cba83h&#7acd89e&#88e18es
                &fType &c&lcancel &fto cancel
                """.trimIndent())

    lateinit var item: ItemStack

    override fun createItem(): Pair<String, ItemStack> {
        item = createBasicItem(
            "&#c46bfb&lS&#cb70e6&lw&#d275d2&la&#d97abd&lp &#e17fa9&lT&#e88394&le&#ef887f&la&#f68d6b&lm&#fd9256&ls",
            listOf("&7Right click to use!", "","&c&oSwapping teams will reset your points!"),
            Material.PAPER,
            listOf("hallomareswapteams"),
            true
        )
        return Pair("hallomareswapteams", item)
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.RIGHT_CLICK -> {
                val item = player.inventory.itemInMainHand
                if (!item.itemMeta.persistentDataContainer.has(NamespacedKey(plugin, "hallomareswapteams"), PersistentDataType.SHORT)) return false
                item.amount -= 1
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ecrates key give ${player.name} hallomares 1")
            }
            //Ability.CHAT -> {
            //    val chatEvent = event as AsyncPlayerChatEvent
            //    newTeamPlayer(chatEvent.message, player)
            //    event.isCancelled = true
            //}
            else -> return false
        }
        return true
    }


    private fun flagPlayer(player: Player) {
        player.persistentDataContainer.set(NamespacedKey(plugin, "hallomareswapteams"), PersistentDataType.SHORT, 1)
        player.sendMessage(msg)
    }


    private fun newTeamPlayer(msg: String, player: Player) {
        if (msg.equals("cancel", ignoreCase = true)) {
            player.persistentDataContainer.remove(NamespacedKey(plugin, "hallomareswapteams"))
            player.sendMessage(Util.prefix + " Cancelled")
            player.inventory.addItem(item)
            return
        }
        var newTeam: String? = null
        when (msg.lowercase(Locale.getDefault())) {
            "dark_imps" -> newTeam = "dark_imps"
            "sunlit_spirits" -> newTeam = "sunlit_spirits"
            "sapphire_shadows" -> newTeam = "sapphire_shadows"
            "wonder_witches" -> newTeam = "wonder_witches"
        }

        if (newTeam != null) {
            player.persistentDataContainer.remove(NamespacedKey(plugin, "hallomareswapteams"))
            Bukkit.getScheduler().runTask(plugin, Runnable { // Asynchronous so we have to sync it :(
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "hallomares setteam " + newTeam + " " + player.name)
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "hallomares points set ${player.name} 0")
            })
        } else {
            player.sendMessage(msg)
            player.sendMessage(Util.prefix + " Invalid team name!")
        }
    }
}