package dev.jsinco.lumaitems.items.misc

import com.iridium.iridiumcolorapi.IridiumColorAPI
import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.manager.Action
import dev.jsinco.lumaitems.manager.CustomItem
import dev.jsinco.lumaitems.manager.FileManager
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import kotlin.random.Random


class StellarStarItem : CustomItem {

    companion object {
        val plugin: LumaItems = LumaItems.getPlugin()
        val stellarStarFile = FileManager("stellarstars.yml")
        val stellarFile = stellarStarFile.getFileYaml()
        val stellarTier = listOf(
            "§",
            "&#EEE1D5&m       &r&#EEE1D5⋆⁺₊⋆ ★ ⋆⁺₊⋆&m       ",
            "&#EEE1D5Tier • &#abd7f7&lS&#b6d2f8&lt&#c2ccf9&le&#cdc7fa&ll&#d8c2fb&ll&#e4bcfc&la&#efb7fd&lr",
            "&#EEE1D5&m       &r&#EEE1D5⋆⁺₊⋆ ★ ⋆⁺₊⋆&m       ")
    }

    init {
        stellarStarFile.generateFile() //TODO: cleanup
    }

    lateinit var stellarStar: ItemStack


    override fun createItem(): Pair<String, ItemStack> {
        stellarStar = Util.createBasicItem(
            "&#abd7f7&lS&#b6d2f8&lt&#c2ccf9&le&#cdc7fa&ll&#d8c2fb&ll&#e4bcfc&la&#efb7fd&lr &#E2E2E2Star",
            mutableListOf("&7Right click to open!"),
            Material.NETHER_STAR,
            mutableListOf("stellarstar"),
            glint = true
        )
        return Pair("stellarstar", stellarStar)
    }

    override fun executeAbilities(type: Action, player: Player, event: Any): Boolean {
        when (type) {
            Action.RIGHT_CLICK -> {
                if (player.inventory.itemInMainHand.itemMeta.persistentDataContainer.has(NamespacedKey(plugin, "stellarstar"), PersistentDataType.SHORT)) {
                    val item = getStellarTool()
                    item.itemMeta = generateDecals(item)
                    player.inventory.addItem(item)
                    player.inventory.itemInMainHand.amount -= 1
                }
            }

            else -> return false
        }
        return true
    }

    private fun getStellarTool(): ItemStack {
        val validTools = stellarFile.getStringList("valid-tools")
        val randomTool: String = validTools[Random.nextInt(validTools.size)]

        val toolEnchants: MutableMap<String, Int> = mutableMapOf()
        for (i in 0 until stellarFile.getStringList("$randomTool.Enchants").size) {
            toolEnchants[stellarFile.getStringList("$randomTool.Enchants")[i]] = stellarFile.getIntegerList("$randomTool.Levels")[i]
        }
        // Create item TODO: move to separate function
        // FIXME
        val numOfEnchants = Random.nextInt(3, toolEnchants.size)
        val item = ItemStack(Material.valueOf("DIAMOND_$randomTool"))
        val meta = item.itemMeta

        for (i in 0 until numOfEnchants) {
            var enchant: Enchantment = Enchantment.getByKey(NamespacedKey.minecraft(toolEnchants.keys.random()))!!

            while (meta.hasEnchant(enchant)) {
                enchant = Enchantment.getByKey(NamespacedKey.minecraft(toolEnchants.keys.random()))!!
            }
            //TODO: Works?
            val enchantString = enchant.key.toString().split(":")[1]
            meta.addEnchant(enchant, toolEnchants[enchantString]!!, true)
        }

        if (meta.hasEnchant(Enchantment.SILK_TOUCH) && meta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)) {
            if (Random.nextBoolean()) { // FIXME
                meta.removeEnchant(Enchantment.SILK_TOUCH)
            } else {
                meta.removeEnchant(Enchantment.LOOT_BONUS_BLOCKS)
            }
        }
        item.itemMeta = meta
        return item
    }

    private fun generateDecals(item: ItemStack): ItemMeta {
        val meta = item.itemMeta
        val rgb1 = stellarFile.getStringList("rgb-colors").random()
        val rgb2 = stellarFile.getStringList("rgb-colors").random()
        val prefix = stellarFile.getStringList("prefixes").random()
        val type = item.type.toString().split("_")[1].lowercase().replaceFirstChar { it.uppercase() }
        meta.setDisplayName(IridiumColorAPI.process("<GRADIENT:$rgb1>&l$prefix</GRADIENT:$rgb2> ${Util.colorcode("&#E2E2E2$type")}"))
        meta.lore = Util.colorcodeList(stellarTier)
        meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
        meta.persistentDataContainer.set(NamespacedKey(plugin, "stellar"), PersistentDataType.SHORT, 1)
        return meta
    }
}