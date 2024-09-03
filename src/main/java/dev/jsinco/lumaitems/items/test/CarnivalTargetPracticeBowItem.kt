package dev.jsinco.lumaitems.items.test

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.ItemFactory
import dev.jsinco.lumaitems.manager.Action
import dev.jsinco.lumaitems.manager.CustomItem
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue

class CarnivalTargetPracticeBowItem : CustomItem {

    override fun createItem(): Pair<String, ItemStack> {
        val item: ItemFactory = ItemFactory.builder()
            .name("Carnival Target Practice Bow")
            .customEnchants(mutableListOf("&aThis is a test item"))
            .lore(mutableListOf("This is a test item"))
            .material(Material.BOW)
            .persistentData(mutableListOf("carnivaltargetpracticebow"))
            .vanillaEnchants(mutableMapOf(Enchantment.UNBREAKING to 5))
            .tier("<light_green><bold>Debug")
            .build()
        return Pair("carnivaltargetpracticebow", item.createItem())
    }

    override fun executeAbilities(type: Action, player: Player, event: Any): Boolean {
        when (type) {
            Action.PROJECTILE_LAUNCH -> {
                event as ProjectileLaunchEvent
                event.entity.setMetadata("carnival_target_practice_bow", FixedMetadataValue(LumaItems.getInstance(), true))
            }

            Action.DROP_ITEM -> {
                event as PlayerDropItemEvent
                event.isCancelled = true
            }
            else -> return false
        }
        return true
    }
}