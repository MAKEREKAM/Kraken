package kr.vanilage.main.weapons

import kr.vanilage.main.Main
import kr.vanilage.main.Weapon
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent

class `11`(override val coolTick : Int) : Weapon{
    override fun setup() {

    }

    override fun skill(player : Player) {
        val bullet = player.world.spawn(player.location, Snowball::class.java)
        bullet.addScoreboardTag("11Bullet")
        bullet.isVisibleByDefault = false
        bullet.setGravity(false)
        bullet.velocity = player.location.direction.normalize().multiply(150)

        Bukkit.getPluginManager().registerEvents(
            object : Listener {
                @EventHandler
                fun onProjectileHit(e : ProjectileHitEvent) {
                    if (e.hitBlock != null) return

                    if (e.entity.uniqueId == bullet.uniqueId) {
                        val location = e.hitBlock!!.location
                        val distance = location.distance(player.location)
                    }
                }
            }, Main.instance
        )
    }
}