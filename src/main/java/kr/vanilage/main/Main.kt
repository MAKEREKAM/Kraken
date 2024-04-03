package kr.vanilage.main

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin
import kr.vanilage.main.weapons.`1`

class Main : JavaPlugin() {
    override fun onEnable() {
        Bukkit.getConsoleSender().sendMessage("Hello, World!")

        registerWeapon(10, `1`(100))
    }

    fun registerWeapon(customModelData : Int, weapon : Weapon) {
        weapon.setup()

        Bukkit.getPluginManager().registerEvents(
            object : Listener {
                @EventHandler
                fun onInteract(e : PlayerInteractEvent) {
                    if (!e.action.isRightClick) return
                    if (e.player.inventory.itemInMainHand.itemMeta == null) return
                    if (e.player.inventory.itemInMainHand.itemMeta.customModelData != customModelData) return
                    if (e.player.hasCooldown(Material.BOOK)) return

                    e.player.setCooldown(Material.BOOK, weapon.coolTick)
                    weapon.skill(e.player)
                }
            }
            , this
        )
    }
}