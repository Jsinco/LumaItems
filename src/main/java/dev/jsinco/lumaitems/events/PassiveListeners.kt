package dev.jsinco.lumaitems.events

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.manager.Action
import dev.jsinco.lumaitems.manager.ItemManager
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable


class PassiveListeners(val plugin: LumaItems) {

    companion object {
        const val DEFAULT_PASSIVE_LISTENER_TICKS: Long = 70
        const val ASYNC_PASSIVE_LISTENER_TICKS: Long = 30
    }

    private fun fire(dataList: List<PersistentDataContainer>, player: Player, action: Action) {
        for (data: PersistentDataContainer in dataList) {
            for (customItem in ItemManager.customItems) {
                if (!data.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) continue
                customItem.value.executeAbilities(action, player, 0)
            }
        }
    }


    fun getPassiveListener(action: Action): BukkitRunnable {
        return object: BukkitRunnable() {
            override fun run() {
                for (player in Bukkit.getOnlinePlayers()) {
                    fire(Util.getAllEquipmentNBT(player), player, action)
                }
            }
        }
    }

    fun onPluginAction(action: Action) {
        for (player in Bukkit.getOnlinePlayers()) {
            fire(Util.getAllEquipmentNBT(player), player, action)
        }
    }

}