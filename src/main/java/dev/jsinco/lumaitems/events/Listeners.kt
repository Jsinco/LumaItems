package dev.jsinco.lumaitems.events

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent
import com.destroystokyo.paper.event.player.PlayerJumpEvent
import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.manager.ItemManager
import dev.jsinco.lumaitems.util.Util
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.*
import org.bukkit.event.player.*
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

/**
 * Main listeners class for LumaItems
 * We use persistent data containers to store the custom item data and listen for it
 * Blocks cannot store persistent data so we will have to store in a file (if needed for long term)
 * Or have our listeners fire every single executeAbilities() method every time we need to grab data from a block
 */
class Listeners(val plugin: LumaItems) : Listener {


    @EventHandler
    fun onCrossbowLoad(event: EntityLoadCrossbowEvent) {
        val player = event.entity as? Player ?: return
        val item = player.inventory.itemInMainHand
        if (!item.hasItemMeta()) return

        val data: PersistentDataContainer = item.itemMeta!!.persistentDataContainer

        for (customItem in ItemManager.customItems) {
            if (data.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) {
                val customItemClass = customItem.value
                customItemClass.executeAbilities(Ability.CROSSBOW_LOAD, player, event)
                break
            }
        }
    }

    @EventHandler
    fun onProjectileLaunch(event: ProjectileLaunchEvent) {
        val player = event.entity.shooter as? Player ?: return

        val item = player.inventory.itemInMainHand
        val offHandItem = player.inventory.itemInOffHand

        if (!item.hasItemMeta() && !offHandItem.hasItemMeta()) return

        val data: PersistentDataContainer? = item.itemMeta?.persistentDataContainer
        val offHandData: PersistentDataContainer? = offHandItem.itemMeta?.persistentDataContainer


        for (customItem in ItemManager.customItems) {
            if (data?.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT) == true) {
                val customItemClass = customItem.value
                customItemClass.executeAbilities(Ability.PROJECTILE_LAUNCH, player, event)
                return
            } else if (offHandData?.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT) == true) {
                val customItemClass = customItem.value
                customItemClass.executeAbilities(Ability.PROJECTILE_LAUNCH, player, event)
                return
            }
        }
    }

    @EventHandler
    fun onProjectileHit(event: ProjectileHitEvent) {
        val projectile = event.entity
        val player = event.entity.shooter as? Player

        val data = projectile.persistentDataContainer
        for (customItemsClass in ItemManager.customItems) {
            if (data.has(NamespacedKey(plugin, customItemsClass.key), PersistentDataType.SHORT)) {
                val customItemClass = customItemsClass.value
                customItemClass.executeAbilities(Ability.PROJECTILE_LAND, player!!, event)
                break
            }
        }
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val dataContainers: List<PersistentDataContainer> = Util.getAllEquipmentNBT(player)
        val ability: Ability = if (event.action.isLeftClick) Ability.LEFT_CLICK else if (event.action.isRightClick) Ability.RIGHT_CLICK else Ability.GENERIC_INTERACT

        for (customItem in ItemManager.customItems) {
            for (dataContainer in dataContainers) {
                if (dataContainer.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) {
                    val customItemClass = customItem.value
                    customItemClass.executeAbilities(ability, player, event)
                    break
                }
            }
        }
    }

    @EventHandler
    fun onPlayerSwapHandItems(event: PlayerSwapHandItemsEvent) {
        val player = event.player

        val item = event.offHandItem ?: return
        if (!item.hasItemMeta()) return

        val data: PersistentDataContainer = item.itemMeta!!.persistentDataContainer

        for (customItem in ItemManager.customItems) {
            if (data.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) {
                val customItemClass = customItem.value
                customItemClass.executeAbilities(Ability.SWAP_HAND, player, event)
                break
            }
        }
    }

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        val player: Player = event.entity.killer ?: return
        val item = player.inventory.itemInMainHand
        if (!item.hasItemMeta()) return

        val data: PersistentDataContainer = item.itemMeta!!.persistentDataContainer
        for (customItem in ItemManager.customItems) {
            if (data.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) {
                val customItemClass = customItem.value
                customItemClass.executeAbilities(Ability.ENTITY_DEATH, player, event)
                break
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val player: Player = if (event.damager is Player) {
            event.damager as Player
        } else if (event.damager is Projectile) {
            val projectile = event.damager as Projectile
            projectile.shooter as? Player ?: return
        } else {
            return
        }

        val item = player.inventory.itemInMainHand
        if (!item.hasItemMeta()) return

        val data: PersistentDataContainer = item.itemMeta!!.persistentDataContainer

        for (customItem in ItemManager.customItems) {
            if (data.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) {
                val customItemClass = customItem.value
                customItemClass.executeAbilities(Ability.ENTITY_DAMAGE, player, event)
                break
            }
        }
    }

    @EventHandler
    fun onPlayerDamagedByEntity(event: EntityDamageByEntityEvent) {
        val player = event.entity as? Player ?: return

        val playerData: List<PersistentDataContainer> = Util.getAllEquipmentNBT(player)

        for (customItem in ItemManager.customItems) {
            for (dataContainer in playerData) {
                if (dataContainer.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) {
                    val customItemClass = customItem.value
                    customItemClass.executeAbilities(Ability.PLAYER_DAMAGED_BY_ENTITY, player, event)
                    break
                }
            }
        }
    }


    @EventHandler
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        val player = event.player

        val item = event.itemDrop.itemStack
        if (!item.hasItemMeta()) return

        val data: PersistentDataContainer = item.itemMeta!!.persistentDataContainer

        for (customItem in ItemManager.customItems) {
            if (data.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) {
                val customItemClass = customItem.value
                customItemClass.executeAbilities(Ability.DROP_ITEM, player, event)
                break
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onPlayerBreakBlock(event: BlockBreakEvent) {
        val player = event.player

        val item = player.inventory.itemInMainHand
        if (!item.hasItemMeta()) return

        val data: PersistentDataContainer = item.itemMeta!!.persistentDataContainer

        for (customItem in ItemManager.customItems) {
            if (data.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) {
                val customItemClass = customItem.value
                customItemClass.executeAbilities(Ability.BREAK_BLOCK, player, event)
                break
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPlayerFish(event: PlayerFishEvent) {
        val player = event.player

        val item = player.inventory.itemInMainHand
        val offHandItem = player.inventory.itemInOffHand

        if (!item.hasItemMeta() && !offHandItem.hasItemMeta()) return

        val data: PersistentDataContainer? = item.itemMeta?.persistentDataContainer
        val offHandData: PersistentDataContainer? = offHandItem.itemMeta?.persistentDataContainer

        for (customItem in ItemManager.customItems) {
            if (data?.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT) == true) {
                val customItemClass = customItem.value
                customItemClass.executeAbilities(Ability.FISH, player, event)
                break
            } else if (offHandData?.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT) == true) {
                val customItemClass = customItem.value
                customItemClass.executeAbilities(Ability.FISH, player, event)
                break
            }
        }
    }

    @EventHandler // The only item that uses this is the Ruby Pinions, only setting up to grab elytra for now
    fun onPlayerElytraBoost(event: PlayerElytraBoostEvent) {
        val player = event.player

        val elytra = player.inventory.chestplate!! // You can't call this event while having a null chestplate??
        if (!elytra.hasItemMeta()) return

        val data: PersistentDataContainer = elytra.itemMeta!!.persistentDataContainer

        for (customItem in ItemManager.customItems) {
            if (data.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) {
                val customItemClass = customItem.value
                customItemClass.executeAbilities(Ability.ELYTRA_BOOST, player, event)
                break
            }
        }
    }

    @EventHandler
    fun onPlayerCrouch(event: PlayerToggleSneakEvent) {
        val player = event.player
        val data: List<PersistentDataContainer> = Util.getAllEquipmentNBT(player)

        for (customItem in ItemManager.customItems) {
            for (itemData in data) {
                if (itemData.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) {
                    val customItemClass = customItem.value
                    customItemClass.executeAbilities(Ability.PLAYER_CROUCH, player, event)
                    break
                }
            }
        }
    }

    @EventHandler
    fun onPlayerPlaceBlock(event: BlockPlaceEvent) {
        val player = event.player
        val data: List<PersistentDataContainer> = Util.getAllEquipmentNBT(player)

        for (customItem in ItemManager.customItems) {
            for (itemData in data) {
                if (itemData.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) {
                    val customItemClass = customItem.value
                    customItemClass.executeAbilities(Ability.PLACE_BLOCK, player, event)
                    break
                }
            }
        }
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        val player = event.entity as? Player ?: return
        val data: List<PersistentDataContainer> = Util.getAllEquipmentNBT(player)

        for (customItem in ItemManager.customItems) {
            for (itemData in data) {
                if (itemData.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) {
                    val customItemClass = customItem.value
                    customItemClass.executeAbilities(Ability.ENTITY_DAMAGE_BY_SELF, player, event)
                    break
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        val player = event.player

        val data: PersistentDataContainer = player.persistentDataContainer

        for (customItem in ItemManager.customItems) {
            if (!data.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) continue
            val customItemClass = customItem.value
            customItemClass.executeAbilities(Ability.CHAT, player, event)
            break
        }
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (!event.hasChangedPosition()) return
        val player = event.player
        val data: List<PersistentDataContainer> = Util.getAllEquipmentNBT(player)
        for (customItem in ItemManager.customItems) {
            for (itemData in data) {
                if (!itemData.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) continue
                val customItemClass = customItem.value
                customItemClass.executeAbilities(Ability.MOVE, player, event)
                break
            }
        }
    }

    @EventHandler
    fun onPlayerConsumeItem(event: PlayerItemConsumeEvent) {
        val player = event.player
        val item = event.item
        if (!item.hasItemMeta()) return

        val data: PersistentDataContainer = item.itemMeta!!.persistentDataContainer

        for (customItem in ItemManager.customItems) {
            if (!data.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) continue
            val customItemClass = customItem.value
            customItemClass.executeAbilities(Ability.CONSUME_ITEM, player, event)
            break
        }
    }

    /**
     * I currently only need to grab the entity for this one.
     * This event does not require a player, so we use a dummy player for it
     */
    @EventHandler
    fun onEntityChangeBlock(event: EntityChangeBlockEvent) {
        val entity = event.entity

        val data: PersistentDataContainer = entity.persistentDataContainer

        for (customItem in ItemManager.customItems) {
            if (!data.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) continue
            customItem.value.executeAbilities(Ability.ENTITY_CHANGE_BLOCK, null as Player, event)
            break
        }
    }

    @EventHandler
    fun onEntityPotionEffect(event: EntityPotionEffectEvent) {
        val player = event.entity as? Player ?: return

        val data = Util.getAllEquipmentNBT(player)

        for (customItem in ItemManager.customItems) {
            for (itemData in data) {
                if (!itemData.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) continue
                customItem.value.executeAbilities(Ability.POTION_EFFECT, player, event)
            }
        }
    }

    @EventHandler
    fun onEntityTargetLivingEntity(event: EntityTargetLivingEntityEvent) {
        val target = event.target as? Player ?: return
        val data = event.entity.persistentDataContainer

        for (customItem in ItemManager.customItems) {
            if (!data.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) continue
            customItem.value.executeAbilities(Ability.ENTITY_TARGET_LIVING_ENTITY, target, event)
        }
    }

    //@EventHandler
    fun onPlayerJump(event: PlayerJumpEvent) {
        val data = Util.getAllEquipmentNBT(event.player)

        for (customItem in ItemManager.customItems) {
            for (itemData in data) {
                if (!itemData.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) continue
                customItem.value.executeAbilities(Ability.JUMP, event.player, event)
            }
        }
    }

    //@EventHandler
    fun onPlayerArmorSwap(event: PlayerArmorChangeEvent) {
        val data = Util.getAllEquipmentNBT(event.player)

        for (customItem in ItemManager.customItems) {
            for (itemData in data) {
                if (!itemData.has(NamespacedKey(plugin, customItem.key), PersistentDataType.SHORT)) continue
                customItem.value.executeAbilities(Ability.ARMOR_SWAP, event.player, event)
            }
        }
    }
}