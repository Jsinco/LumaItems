package dev.jsinco.lumaitems.util

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import dev.jsinco.lumaitems.LumaItems
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType


object Util {

    lateinit var prefix: String
    private const val WITH_DELIMITER = "((?<=%1\$s)|(?=%1\$s))"
    private val gearTypes: List<String> = listOf("Helmet", "Chestplate", "Leggings", "Boots", "Sword", "Pickaxe", "Axe", "Shovel", "Hoe", "Rod")
    val armorEquipmentSlots: List<EquipmentSlot> = listOf(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)
    val plugin: LumaItems = LumaItems.getPlugin()

    @JvmStatic
    fun loadUtils() {
        prefix = colorcode(plugin.config.getString("prefix") ?: "")
    }

    /**
     * @param text The string of text to apply color/effects to
     * @return Returns a string of text with color/effects applied
     */
    @JvmStatic
    fun colorcode(text: String): String {
        val texts = text.split(String.format(WITH_DELIMITER, "&").toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val finalText = StringBuilder()
        var i = 0
        while (i < texts.size) {
            if (texts[i].equals("&", ignoreCase = true)) {
                //get the next string
                i++
                if (texts[i][0] == '#') {
                    finalText.append(net.md_5.bungee.api.ChatColor.of(texts[i].substring(0, 7)).toString() + texts[i].substring(7))
                } else {
                    finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]))
                }
            } else {
                finalText.append(texts[i])
            }
            i++
        }
        return finalText.toString()
    }

    @JvmStatic
    fun colorcodeList(list: List<String>): List<String> {
        val coloredList: MutableList<String> = ArrayList()
        for (string in list) {
            coloredList.add(colorcode(string))
        }
        return coloredList
    }

    fun giveItem(player: Player, item: ItemStack) {
        for (i in 0..35) {
            if (player.inventory.getItem(i) == null || player.inventory.getItem(i)!!.isSimilar(item)) {
                player.inventory.addItem(item)
                break
            } else if (i == 35) {
                player.world.dropItem(player.location, item)
            }
        }
    }


    fun getColorCodeByChatColor(colorCode: ChatColor): String {
        return when (colorCode) {
            ChatColor.AQUA -> "§b"
            ChatColor.BLACK -> "§0"
            ChatColor.BLUE -> "§9"
            ChatColor.DARK_AQUA -> "§3"
            ChatColor.DARK_BLUE -> "§1"
            ChatColor.DARK_GRAY -> "§8"
            ChatColor.DARK_GREEN -> "§2"
            ChatColor.DARK_PURPLE -> "§5"
            ChatColor.DARK_RED -> "§4"
            ChatColor.GOLD -> "§6"
            ChatColor.GRAY -> "§7"
            ChatColor.GREEN -> "§a"
            ChatColor.LIGHT_PURPLE -> "§d"
            ChatColor.RED -> "§c"
            ChatColor.YELLOW -> "§e"
            else -> "§f"
        }
    }

    fun getAllEquipmentNBT(player: Player): List<PersistentDataContainer> {
        val nbtList: MutableList<PersistentDataContainer> = mutableListOf()
        for (equipment in player.equipment.armorContents) {
            equipment?.itemMeta?.persistentDataContainer?.let { nbtList.add(it) }
        }
        player.inventory.itemInMainHand.itemMeta?.persistentDataContainer?.let { nbtList.add(it) }
        player.inventory.itemInOffHand.itemMeta?.persistentDataContainer?.let { nbtList.add(it) }
        return nbtList
    }


    fun createBasicItem(
        name: String,
        lore: List<String>,
        material: Material,
        datas: List<String>,
        glint: Boolean
    ): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta!!
        meta.setDisplayName(colorcode(name))
        meta.lore = colorcodeList(lore)
        for (data in datas) {
            meta.persistentDataContainer.set(NamespacedKey(plugin, data), PersistentDataType.SHORT, 1)
        }
        if (glint) {
            meta.addEnchant(Enchantment.DURABILITY, 10, true)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
        item.itemMeta = meta
        return item
    }

    fun getGearType(item: ItemStack): String? {
        return getGearType(item.type)
    }
    fun getGearType(material: Material): String? {
        for (gear in gearTypes) {
            if (material.toString().contains(gear, ignoreCase = true)) return gear
        }
        return null
    }

    fun formatMaterialName(material: Material): String {
        var name = material.toString().lowercase().replace("_", " ")
        name = name.substring(0, 1).uppercase() + name.substring(1)
        for (i in name.indices) {
            if (name[i] == ' ') {
                name =
                    name.substring(0, i) + " " + name[i + 1].toString().uppercase() + name.substring(
                        i + 2
                    ) // Capitalize first letter of each word
            }
        }
        return name
    }

    fun splitRandomList(list: MutableList<*>, retain: Int): MutableList<*> {
        val newList: MutableList<Any> = mutableListOf()
        for (i in 0 until retain) {
            val random = list.indices.random()
            list[random]?.let { newList.add(it) }
            list.removeAt(random)
        }
        return newList
    }

    fun setEntityEquipment(entity: LivingEntity, item: ItemStack, slot: EquipmentSlot) {
        when (slot) {
            EquipmentSlot.HEAD -> entity.equipment?.helmet = item
            EquipmentSlot.CHEST -> entity.equipment?.chestplate = item
            EquipmentSlot.LEGS -> entity.equipment?.leggings = item
            EquipmentSlot.FEET -> entity.equipment?.boots = item
            EquipmentSlot.HAND -> entity.equipment?.setItemInMainHand(item)
            EquipmentSlot.OFF_HAND -> entity.equipment?.setItemInOffHand(item)
            else -> return
        }
    }

    fun isItemInSlot(identifier: String, slot: EquipmentSlot, player: Player): Boolean {
        return player.equipment.getItem(slot).itemMeta?.persistentDataContainer?.has(NamespacedKey(plugin, identifier), PersistentDataType.SHORT) == true
    }
}