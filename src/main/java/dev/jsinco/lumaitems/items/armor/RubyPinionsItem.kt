package dev.jsinco.lumaitems.items.armor

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent
import dev.jsinco.lumaitems.LumaItems
import dev.jsinco.lumaitems.manager.Ability
import dev.jsinco.lumaitems.items.CreateItem
import dev.jsinco.lumaitems.manager.CustomItem
import org.bukkit.*
import org.bukkit.Particle.DustOptions
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

class RubyPinionsItem : CustomItem {

    companion object {
        val plugin: LumaItems = LumaItems.getPlugin()
    }

    override fun createItem(): Pair<String, ItemStack> {
        val item = CreateItem(
            "&#ff5372&lR&#f5518e&lu&#ec4faa&lb&#e24ec5&ly &#d94ce1&lP&#cf4afd&li&#bd4cfd&ln&#ab4efc&li&#9950fc&lo&#8752fb&ln&#7554fb&ls",
            mutableListOf("&#ff5372V&#f85286e&#f1509al&#ea4faeo&#e44ec1c&#dd4dd5i&#d64be9t&#cf4afdy"),
            mutableListOf("&#ff5372\"&#fd5378A&#fb527es &#f95284f&#f7518aa&#f55190s&#f25196t &#f0509ca&#ee50a2s &#ec4fa8a &#ea4faes&#e84fb4h&#e64ebbo&#e44ec1o&#e24ec7t&#e04dcdi&#de4dd3n&#dc4cd9g &#d94cdfs&#d74ce5t&#d54beba&#d34bf1r&#d14af7!&#cf4afd\"","","The effectiveness of rockets will be", "increased while wearing these wings","","Wearing these wings grants a 50%","chance to not consume a rocket"),
            Material.ELYTRA,
            mutableListOf("rubypinions"),
            mutableMapOf(Enchantment.PROTECTION_ENVIRONMENTAL to 6, Enchantment.PROTECTION_PROJECTILE to 8, Enchantment.DURABILITY to 9, Enchantment.MENDING to 1)
        )
        return Pair("rubypinions", item.createItem())
    }

    override fun executeAbilities(type: Ability, player: Player, event: Any): Boolean {
        val elytraBoostEvent: PlayerElytraBoostEvent? = event as? PlayerElytraBoostEvent

        when (type) {
            Ability.ELYTRA_BOOST -> {
                velocity(player)
                elytraBoostEvent!!.isCancelled = true
            }
            else -> return false
        }
        return true
    }

    private fun velocity(player: Player) {
        player.velocity = player.location.getDirection().multiply(2.1)
        player.world.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1f, 1f)
        player.world.playSound(player.location, Sound.ENTITY_ALLAY_AMBIENT_WITH_ITEM, 1f, 1f)
        if (Random().nextBoolean()) {
            if (player.inventory.itemInMainHand.type == Material.FIREWORK_ROCKET) {
                player.inventory.itemInMainHand.amount -= 1
            } else if (player.inventory.itemInOffHand.type == Material.FIREWORK_ROCKET) {
                player.inventory.itemInOffHand.amount -= 1
            }
        }
        val task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, {
            player.world.spawnParticle(Particle.REDSTONE, player.location.add(0.0, 1.0, 0.0), 1, 0.5, 0.5, 0.5, 0.1, DustOptions(Color.fromRGB(255, 83, 114), 2f))
            player.world.spawnParticle(Particle.REDSTONE, player.location.add(0.0, 1.0, 0.0), 1, 0.5, 0.5, 0.5, 0.1, DustOptions(Color.fromRGB(207, 74, 253), 2f))
        }, 0L, 1L)
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, { Bukkit.getScheduler().cancelTask(task) }, 35L)
    }
}