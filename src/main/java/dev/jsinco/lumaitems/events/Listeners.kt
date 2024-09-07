package dev.jsinco.lumaitems.events

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent
import com.destroystokyo.paper.event.player.PlayerJumpEvent
import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent
import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.enums.Action
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.manager.ItemManager
import dev.jsinco.lumaitems.util.FireForAllNBT
import dev.jsinco.lumaitems.util.Util
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import io.papermc.paper.event.entity.EntityMoveEvent
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDropItemEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.block.BlockShearEntityEvent
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.event.entity.EntityTargetLivingEntityEvent
import org.bukkit.event.entity.EntityTeleportEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerShearEntityEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

/**
 * Main listeners class for LumaItems
 * We use persistent data containers to store the custom item data and listen for it
 * Blocks cannot store persistent data, so we will have to store in a file (if needed for long term)
 * Or have our listeners fire every single executeAbilities() method every time we need to grab data from a block
 */
class Listeners(val plugin: LumaItems) : Listener {

    companion object {

        // This exists because Kotlin doesn't allow null values unless the variable is nullable, and I'm not going to edit 75+ classes
        // Maybe replace with a class that implements player sometime?
        private var player: Player? = null
        private fun getDummyPlayer(): Player? {
            if (player == null && Bukkit.getOnlinePlayers().isNotEmpty()) {
                player = Bukkit.getOnlinePlayers().random()
            }
            return player
        }
    }

    private fun fire(data: PersistentDataContainer, action: Action, player: Player, event: Any) {
        for (customItem: MutableMap.MutableEntry<NamespacedKey, CustomItem> in ItemManager.customItems) {
            if (!data.has(customItem.key, PersistentDataType.SHORT)) continue
            customItem.value.executeActions(action, player, event)
            break
        }
    }

    private fun fire(data: List<PersistentDataContainer>, action: Action, player: Player, event: Any) {
        for (itemData: PersistentDataContainer in data) {
            for (customItem: MutableMap.MutableEntry<NamespacedKey, CustomItem> in ItemManager.customItems) {
                if (!itemData.has(customItem.key, PersistentDataType.SHORT)) continue
                customItem.value.executeActions(action, player, event)
                break
            }
        }
    }

    private fun fire(key: String, action: Action, player: Player, event: Any) {
        for (customItem in ItemManager.customItems) {
            if (key.equals(customItem.key.key, true)) {
                customItem.value.executeActions(action, player, event)
                break
            }
        }
    }

    @EventHandler
    fun onCrossbowLoad(event: EntityLoadCrossbowEvent) {
        val player = event.entity as? Player ?: return
        val data: PersistentDataContainer = player.inventory.itemInMainHand.itemMeta?.persistentDataContainer ?: return

        fire(data, Action.CROSSBOW_LOAD, player, event)
    }

    @FireForAllNBT
    @EventHandler
    fun onProjectileLaunch(event: ProjectileLaunchEvent) {
        val player: Player = event.entity.shooter as? Player ?: return
        fire(Util.getAllEquipmentNBT(player), Action.PROJECTILE_LAUNCH, player, event)
    }

    @EventHandler
    fun onProjectileHit(event: ProjectileHitEvent) {
        val player = event.entity.shooter as? Player ?: return

        val data = event.entity.persistentDataContainer
        fire(data, Action.PROJECTILE_LAND, player, event)
    }

    @FireForAllNBT
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val dataContainers: List<PersistentDataContainer> = Util.getAllEquipmentNBT(player)
        val action: Action = if (event.action.isLeftClick) Action.LEFT_CLICK else if (event.action.isRightClick) Action.RIGHT_CLICK else Action.GENERIC_INTERACT

        fire(dataContainers, action, player, event)
    }

    @EventHandler
    fun onPlayerSwapHandItems(event: PlayerSwapHandItemsEvent) {
        val player = event.player
        val item = event.offHandItem
        val data: PersistentDataContainer = item.itemMeta?.persistentDataContainer ?: return

        fire(data, Action.SWAP_HAND, player, event)
    }

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        val player: Player = event.entity.killer ?: return
        val item = player.inventory.itemInMainHand
        val data: PersistentDataContainer = item.itemMeta?.persistentDataContainer ?: return

        fire(data, Action.ENTITY_DEATH, player, event)
    }

    @FireForAllNBT
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val player: Player = when (event.damager) {
            is Player -> event.damager as Player
            is Projectile -> (event.damager as? Projectile)?.shooter as? Player ?: return
            else -> return
        }

        val data: List<PersistentDataContainer> = Util.getAllEquipmentNBT(player)

        // TODO: Add special Ability type for when players are damaging an entity with no permission to damage them
        fire(data, Action.ENTITY_DAMAGE, player, event)
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onPlayerDamagedByEntity(event: EntityDamageByEntityEvent) {
        val player: Player = event.entity as? Player ?: return
        val action = if (player.isBlocking) Action.PLAYER_DAMAGED_WHILE_BLOCKING else Action.PLAYER_DAMAGED_BY_ENTITY

        fire(Util.getAllEquipmentNBT(player), action, player, event)
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        val entity = event.entity
        val data: List<PersistentDataContainer>? = if (entity is Player) Util.getAllEquipmentNBT(entity) else null


        if (data != null) {
            fire(data, Action.PLAYER_DAMAGE_GENERIC, entity as? Player ?: return, event)
        } else {
            fire(entity.persistentDataContainer, Action.ENTITY_DAMAGED_GENERIC, (getDummyPlayer() ?: return), event)
        }
    }

    @EventHandler
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        val player = event.player
        val data: PersistentDataContainer = event.itemDrop.itemStack.itemMeta?.persistentDataContainer ?: return

        fire(data, Action.DROP_ITEM, player, event)
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onPlayerBreakBlock(event: BlockBreakEvent) {
        val player = event.player
        val data: PersistentDataContainer? = player.inventory.itemInMainHand.itemMeta?.persistentDataContainer

        if (data != null) {
            fire(data, Action.BREAK_BLOCK, player, event)
        }

        val playerCachedBlocks = BlockCacheManager.playerCachedBlocks[player.uniqueId] ?: return
        val l = event.block.location

        for (loc in playerCachedBlocks.locations) {
            if (loc == l) {
                fire(playerCachedBlocks.id, Action.CACHED_BLOCK_BREAK, player, event)
                break
            }
        }
    }

    @EventHandler
    fun onBlockDropItems(event: BlockDropItemEvent) {
        val player = event.player
        val data = player.inventory.itemInMainHand.itemMeta?.persistentDataContainer

        if (data != null) {
            fire(data, Action.BLOCK_DROP_ITEM, player, event)
        }
    }


    @FireForAllNBT
    @EventHandler
    fun onPlayerPlaceBlock(event: BlockPlaceEvent) {
        val player = event.player
        val data: List<PersistentDataContainer> = Util.getAllEquipmentNBT(player)

        fire(data, Action.PLACE_BLOCK, player, event)
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
            if (data?.has(customItem.key, PersistentDataType.SHORT) == true) {
                val customItemClass = customItem.value
                customItemClass.executeActions(Action.FISH, player, event)
                break
            } else if (offHandData?.has(customItem.key, PersistentDataType.SHORT) == true) {
                val customItemClass = customItem.value
                customItemClass.executeActions(Action.FISH, player, event)
                break
            }
        }
    }

    @EventHandler
    fun onPlayerElytraBoost(event: PlayerElytraBoostEvent) {
        val player = event.player

        val elytra = player.inventory.chestplate ?: return
        val data: PersistentDataContainer = elytra.itemMeta?.persistentDataContainer ?: return

        fire(data, Action.ELYTRA_BOOST, player, event)
    }

    @FireForAllNBT
    @EventHandler
    fun onPlayerCrouch(event: PlayerToggleSneakEvent) {
        val player = event.player
        fire(Util.getAllEquipmentNBT(player), Action.PLAYER_CROUCH, player, event)
    }


    @EventHandler (priority = EventPriority.LOWEST)
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        val player = event.player

        val data: PersistentDataContainer = player.persistentDataContainer

        fire(data, Action.ASYNC_CHAT, player, event)
    }

    @FireForAllNBT
    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (!event.hasChangedPosition()) return

        fire(Util.getAllEquipmentNBT(event.player), Action.MOVE, event.player, event)
    }


    @EventHandler
    fun onEntityMoveEvent(event: EntityMoveEvent) {
        if (!event.hasChangedPosition()) return

        val container: PersistentDataContainer = event.entity.persistentDataContainer
        if (container.isEmpty) return

        fire(container, Action.ENTITY_MOVE, (getDummyPlayer() ?: return), event)
    }

    //@EventHandler
    fun onPlayerJump(event: PlayerJumpEvent) {
        val data = Util.getAllEquipmentNBT(event.player)
        fire(data, Action.JUMP, event.player, event)
    }

    //@EventHandler
    fun onEntityChangeBlock(event: EntityChangeBlockEvent) {
        val entity = event.entity

        val data: PersistentDataContainer = entity.persistentDataContainer
        fire(data, Action.ENTITY_CHANGE_BLOCK, (getDummyPlayer() ?: return), event)
    }

    @EventHandler
    fun onPlayerConsumeItem(event: PlayerItemConsumeEvent) {
        val data: PersistentDataContainer = event.item.itemMeta?.persistentDataContainer ?: return

        fire(data, Action.CONSUME_ITEM, event.player, event)
    }

    @FireForAllNBT
    @EventHandler
    fun onEntityPotionEffect(event: EntityPotionEffectEvent) {
        val player = event.entity as? Player ?: return
        fire(Util.getAllEquipmentNBT(player), Action.POTION_EFFECT, player, event)
    }

    @EventHandler
    fun onEntityTargetLivingEntity(event: EntityTargetLivingEntityEvent) {
        val target = event.target as? Player ?: return
        val data = event.entity.persistentDataContainer

        fire(data, Action.ENTITY_TARGET_PLAYER, target, event)
    }


    @EventHandler
    fun onPlayerArmorSwap(event: PlayerArmorChangeEvent) {
        val data = listOf(event.oldItem, event.newItem).mapNotNull { it.itemMeta?.persistentDataContainer }

        fire(data, Action.ARMOR_CHANGE, event.player, event)
    }

    @EventHandler
    fun onEntityTeleport(event: EntityTeleportEvent) {
        fire(event.entity.persistentDataContainer, Action.ENTITY_TELEPORT, (getDummyPlayer() ?: return), event)
    }

    @EventHandler
    fun onShearEntity(event: PlayerShearEntityEvent) {
        val player = event.player
        val data: PersistentDataContainer = player.inventory.itemInMainHand.itemMeta?.persistentDataContainer ?: return

        fire(data, Action.SHEAR_ENTITY, player, event)
    }

    @EventHandler
    fun onBlockShearEntity(event: BlockShearEntityEvent) {
        val data: PersistentDataContainer = event.tool.itemMeta?.persistentDataContainer ?: return

        fire(data, Action.BLOCK_SHEAR_ENTITY, getDummyPlayer() ?: return, event)
    }

    @FireForAllNBT
    @EventHandler
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        fire(Util.getAllEquipmentNBT(event.player), Action.PLAYER_TELEPORT, event.player, event)
    }

    @FireForAllNBT
    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        fire(Util.getAllEquipmentNBT(event.player), Action.PLAYER_QUIT, event.player, event)
    }

    @FireForAllNBT
    @EventHandler
    fun onPlayerPickupExp(event: PlayerPickupExperienceEvent) {
        fire(Util.getAllEquipmentNBT(event.player), Action.PLAYER_PICKUP_EXP, event.player, event)
    }
}