package dev.jsinco.lumaitems.items.astral

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.manager.FileManager
import dev.jsinco.lumaitems.relics.RelicCrafting
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import kotlin.random.Random

class AstralOrbItem : CustomItem {
    companion object {
        val plugin = LumaItems.getPlugin()
    }

    override fun createItem(): Pair<String, ItemStack> {
        return Pair("astralorb", RelicCrafting.astralOrb)
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        when (type) {
            Ability.RIGHT_CLICK -> {
                event as PlayerInteractEvent
                event.isCancelled = true

                if (!player.inventory.itemInMainHand.itemMeta.persistentDataContainer.has(NamespacedKey(LumaItems.getPlugin(), "astralorb"), PersistentDataType.SHORT)) return false

                player.inventory.itemInMainHand.amount -= 1
                player.playSound(player.location, Sound.ENTITY_EVOKER_CAST_SPELL, 1f, 1f)

                val itemClasses = FileManager("relics.yml").generateYamlFile().getConfigurationSection("astral.item_classes")?.getKeys(true)
                val setsAndWeight: MutableMap<List<ItemStack>, Int> = mutableMapOf()

                if (itemClasses == null) {
                    return false
                }

                for (itemClass in itemClasses) {
                    val items = RelicCrafting.getItemsFromClass(itemClass)
                    val weight = FileManager("relics.yml").generateYamlFile().getInt("astral.item_classes.$itemClass")
                    setsAndWeight[items] = weight
                }

                var selectedSet = setsAndWeight.keys.toList().random()
                while (setsAndWeight[selectedSet]!! < Random.nextInt(1, 100)) {
                    selectedSet = setsAndWeight.keys.toList().random()
                }

                player.inventory.addItem(selectedSet.random())
            }

            else -> return false
        }
        return true
    }
}