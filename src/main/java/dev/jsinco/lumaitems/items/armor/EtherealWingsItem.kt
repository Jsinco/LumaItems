package dev.jsinco.lumaitems.items.armor

import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.items.CreateItem
import dev.jsinco.lumaitems.manager.CustomItem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class EtherealWingsItem : CustomItem {

    companion object {
        private val cooldown: MutableList<Player> = mutableListOf()
    }

    override fun createItem(): Pair<String, ItemStack> {
        val item = CreateItem(
            "&#a0e8bb&lE&#a6e6bd&lt&#ace4bf&lh&#b2e2c1&le&#b8e0c3&lr&#bedec4&le&#c4dcc6&la&#cadac8&ll &#d0d8ca&lA&#d5d5cc&lp&#dbd3ce&lp&#e1d1d0&la&#e7cfd2&lr&#edcdd3&la&#f3cbd5&lt&#f9c9d7&lu&#ffc7d9&ls",
            mutableListOf("&#a0e8bbA&#a8e6bce&#b0e4bdr&#b9e1bdi&#c1dfbea&#c9ddbfl &#d1dbc0A&#d9d9c1s&#e1d7c2c&#ead4c2e&#f2d2c3n&#fad0c4t"),
            mutableListOf("§fCrouch while gliding to launch yourself","§fin the direction you're facing", "","§cCooldown: 10 secs"),
            Material.ELYTRA,
            mutableListOf("etherealwings"),
            mutableMapOf(Enchantment.PROTECTION_ENVIRONMENTAL to 9, Enchantment.PROTECTION_EXPLOSIONS to 8, Enchantment.DURABILITY to 10, Enchantment.MENDING to 1)
        )
        return Pair("etherealwings", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {

        when (type) {
            Ability.PLAYER_CROUCH -> {
                if (!player.isGliding) return false
                ethWingsLaunch(player)
            }
            else -> return false
        }
        return true
    }

    private fun ethWingsLaunch(p: Player) {
        if (cooldown.contains(p)) return
        p.velocity = p.location.getDirection().multiply(3)
        p.playSound(p.location, Sound.ENTITY_ENDER_DRAGON_FLAP, 1f, 1f)
        cooldown.add(p)
        Bukkit.getServer().scheduler.scheduleSyncDelayedTask(LumaItems.getPlugin(), { cooldown.remove(p) }, 200L)
    }
}