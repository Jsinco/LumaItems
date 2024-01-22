package dev.jsinco.lumaitems.items.misc

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class LoversAlluringItem : CustomItem {
    companion object {
        private val plugin: LumaItems = LumaItems.getPlugin()
    }

    override fun createItem(): Pair<String, ItemStack> {
        val item = ItemFactory(
            "&#fb3535&lL&#f83552&lo&#f5346e&lv&#f2348b&le&#f033a7&lr&#ed33c4&l'&#ea32e0&ls &#e732fd&lA&#ea32e0&ll&#ed33c4&ll&#f033a7&lu&#f2348b&lr&#f5346e&li&#f83552&ln&#fb3535&lg",
            mutableListOf("&cWorker", "&cCoupled"),
            mutableListOf("When fishing with this rod, bites", "will automatically be reeled in", "", "Fish caught with this rod have", "a small chance to be doubled"),
            Material.FISHING_ROD,
            mutableListOf("lovers-alluring"),
            mutableMapOf(Enchantment.LURE to 5, Enchantment.LUCK to 5, Enchantment.DURABILITY to 9, Enchantment.MENDING to 1),
        )
        item.tier = "&#fd4c4c&lB&#fd515e&le&#fd5571&la&#fc5a83&lu &#fc5e96&lH&#fc63a8&le&#fc67bb&la&#fb6ccd&lr&#fb70e0&lt&#fb75f2&ls"
        return Pair("lovers-alluring", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.FISH -> {
                event as PlayerFishEvent
                when (event.state) {
                    PlayerFishEvent.State.BITE -> {
                        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                            LumaItems.getProtocolManager()?.receiveClientPacket(player, PacketContainer(PacketType.Play.Client.BLOCK_PLACE))
                        }, 1L)
                    }
                    PlayerFishEvent.State.CAUGHT_FISH -> {
                        if (Random.nextInt(100) > 10) return false
                        val item = event.caught as Item
                        if (item.itemStack.getMaxStackSize() > 1) item.itemStack.amount = 2
                    }
                    else -> return false
                }
            }
            else -> return false
        }
        return true
    }
}