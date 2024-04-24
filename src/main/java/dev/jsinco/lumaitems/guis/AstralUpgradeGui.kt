package dev.jsinco.lumaitems.guis

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.astral.upgrades.AstralSetUpgradeFactory
import dev.jsinco.lumaitems.items.astral.upgrades.AstralSetUpgradeManager
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class AstralUpgradeGui : AbstractGui {

    companion object {
        private val plugin: LumaItems = LumaItems.getPlugin()
        private val EMPTY_SLOTS: List<Int> = listOf(11, 13, 15)
        private val BORDER: ItemStack = Util.createBasicItem("&0", listOf(), Material.PURPLE_STAINED_GLASS_PANE, listOf("gui-item"), false)
        private val CONFIRM_BUTTON: ItemStack = Util.createBasicItem("&a&lConfirm", listOf(), Material.LIME_STAINED_GLASS_PANE, listOf("gui-item", "confirm"), true)
    }

    init {
        AstralSetUpgradeManager().reloadUpgrades()
    }

    override fun onInventoryClick(event: InventoryClickEvent) {

        val clickedItem = event.currentItem ?: return
        if (clickedItem.itemMeta?.persistentDataContainer?.has(NamespacedKey(plugin, "gui-item"), PersistentDataType.SHORT) == true) {
            event.isCancelled = true
        }

        val astralTool = event.inventory.getItem(11) ?: return

        val upgradeCore = event.inventory.getItem(13) ?: return
        if (!isUpgradeCore(upgradeCore)) return

        val factory = AstralSetUpgradeFactory(astralTool);
        if (factory.upgrade()) {
            upgradeCore.amount -= 1
        }

        event.inventory.setItem(15, astralTool)
        event.inventory.setItem(11, null)
    }

    override fun onInventoryClose(event: InventoryCloseEvent) {
        for (emptySlot in EMPTY_SLOTS) {
            if (event.inventory.getItem(emptySlot) != null) {
                Util.giveItem(event.player as Player, event.inventory.getItem(emptySlot)!!)
            }
        }
    }

    override fun getInventory(): Inventory {
        val inv = Bukkit.createInventory(this, 27, Util.colorcode("&#F670F1&lA&#EB75F3&ls&#DF79F5&lt&#D47EF7&lr&#C882FA&la&#BD87FC&ll &#B28CFE&lU&#A88FFF&lp&#A191FF&lg&#9A93FF&lr&#9394FF&la&#8B96FF&ld&#8498FF&le&#7D9AFF&ls"))
        for (slot in inv.contents.indices) {
            if (!EMPTY_SLOTS.contains(slot)) inv.setItem(slot, BORDER)
        }
        inv.setItem(16, CONFIRM_BUTTON)
        return inv
    }

    private fun isUpgradeCore(item: ItemStack): Boolean {
        return item.itemMeta?.persistentDataContainer?.has(NamespacedKey(plugin, "astralupgradecore"), PersistentDataType.SHORT) ?: false
    }
}