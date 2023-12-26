package dev.jsinco.lumaitems.guis

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.manager.FileManager
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object AstralUpgradeGUI : GUI {

    private val plugin = LumaItems.getPlugin()



    fun initGui(player: Player) {
        val inv: Inventory = Bukkit.createInventory(GUIHolder(this), 27, "Astral Upgrade")

        val item = ItemStack(Material.LIME_STAINED_GLASS)
        val meta = item.itemMeta!!
        meta.persistentDataContainer.set(NamespacedKey(plugin, "confirm") , PersistentDataType.BOOLEAN, true)
        item.itemMeta = meta
        inv.setItem(1, item)
        player.openInventory(inv)
    }


    override fun handleClick(event: InventoryClickEvent) {
        Bukkit.broadcastMessage("Clicked")
        val inventory = event.inventory

        if (event.currentItem?.itemMeta?.persistentDataContainer?.has(NamespacedKey(plugin, "confirm"), PersistentDataType.BOOLEAN) == true) {
            inventory.getItem(0)?.let { upgradeItem(it) }
        }
    }

    fun upgradeItem(item: ItemStack) {
        val meta = item.itemMeta ?: return

        val key: String = meta.persistentDataContainer.get(NamespacedKey(plugin, "set"), PersistentDataType.STRING) ?: return
        val file = FileManager("astral.yml").generateYamlFile()

        val increaseEnchantsBY: Int = file.getInt("upgrades.$key.increase-enchants-by")
        val newMaterial: String = file.getString("upgrades.$key.new-material") ?: "NETHERITE"


        for (enchant in meta.enchants) {
            val level = enchant.value + increaseEnchantsBY
            meta.addEnchant(enchant.key, level, true)
        }

        val newEnchants: MutableMap<Enchantment, Int> = mutableMapOf()
        val configSec = file.getConfigurationSection("upgrades.$key.add-enchants")?.getKeys(false)
        if (configSec != null) {
            for (enchant in configSec) {
                val level = file.getInt("upgrades.$key.add-enchants.$enchant")
                newEnchants[Enchantment.getByKey(NamespacedKey.minecraft(enchant))!!] = level
            }
        }
        for (enchant in newEnchants) {
            meta.addEnchant(enchant.key, enchant.value, true)
        }

        item.itemMeta = meta
        val gearType: String = Util.getGearType(item) ?: return
        item.type = Material.valueOf("${newMaterial}_${gearType}")
    }
}