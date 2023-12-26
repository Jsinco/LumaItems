package dev.jsinco.lumaitems.relics

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.items.astral.AstralOrbItem
import dev.jsinco.lumaitems.items.astral.LunarOrbItem
import dev.jsinco.lumaitems.util.Util
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe

object RelicCrafting {

    private val plugin: LumaItems = LumaItems.getPlugin()

    val relicShard: ItemStack = Util.createBasicItem(
        "&#AC87FB&lRelic &#F7FFC9Shard",
        mutableListOf(),
        Material.ECHO_SHARD,
        mutableListOf("relicshard"),
        true
    )

    val lunarCore: ItemStack = Util.createBasicItem(
        "&#6255fb&lLunar &#F7FFC9Core",
        mutableListOf(),
        Material.PRISMARINE_SHARD,
        mutableListOf("lunarcore"),
        true
    )

    val astralCore: ItemStack = Util.createBasicItem(
        "&#f498f6&lAstral &#F7FFC9Core",
        mutableListOf(),
        Material.PRISMARINE_SHARD,
        mutableListOf("astralcore"),
        true
    )

    val lunarOrb: ItemStack = Util.createBasicItem(
        "&#6255fb&lLunar &#F7FFC9Orb",
        mutableListOf("&7Right-click to redeem"),
        Material.ENDER_EYE,
        mutableListOf("lunarorb"),
        true
    )

    val astralOrb: ItemStack = Util.createBasicItem(
        "&#f498f6&lAstral &#F7FFC9Orb",
        mutableListOf("&7Right-click to redeem"),
        Material.ENDER_EYE,
        mutableListOf("astralorb"),
        true
    )

    @JvmStatic
    fun registerRecipes() {
        val lunarKey = NamespacedKey(plugin, "lunarorb")
        val astralKey = NamespacedKey(plugin, "astralorb")

        if (Bukkit.getRecipe(lunarKey) != null) Bukkit.removeRecipe(lunarKey)
        if (Bukkit.getRecipe(astralKey) != null) Bukkit.removeRecipe(astralKey)


        val lunarRecipe = ShapedRecipe(lunarKey, lunarOrb)
        lunarRecipe.shape(
            "AAA",
            "ABA",
            "AAA")
        lunarRecipe.setIngredient('A', relicShard)
        lunarRecipe.setIngredient('B', lunarCore)
        Bukkit.addRecipe(lunarRecipe)

        val astralRecipe = ShapedRecipe(astralKey, astralOrb)
        astralRecipe.shape(
            "AAA",
            "ABA",
            "AAA")
        astralRecipe.setIngredient('A', relicShard)
        astralRecipe.setIngredient('B', astralCore)
        Bukkit.addRecipe(astralRecipe)
    }
}