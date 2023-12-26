package dev.jsinco.lumaitems.items.astral

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.relics.RelicCrafting
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class AstralOrbItem : CustomItem {
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

                player.sendMessage("Code I haven't written yet")
            }

            else -> return false
        }
        return true
    }
}