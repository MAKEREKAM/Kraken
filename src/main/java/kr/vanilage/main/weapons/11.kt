package kr.vanilage.main.weapons

import kr.vanilage.main.Main
import kr.vanilage.main.Weapon
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Transformation
import org.joml.AxisAngle4f
import org.joml.Vector3f

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

                        val snowball = player.launchProjectile(Snowball::class.java)
                        snowball.isVisibleByDefault = false
                        snowball.setGravity(false)
                        snowball.velocity = player.location.direction.normalize().multiply(distance / 20)

                        val display = snowball.world.spawn(player.location, ItemDisplay::class.java)
                        display.itemStack = ItemStack(Material.NETHERITE_AXE)
                        display.teleportDuration = 1
                        display.transformation = Transformation(
                            Vector3f(0F, 0F, 0F),
                            AxisAngle4f(1.5707963F, 0F, 1F, 0F),
                            Vector3f(1F, 1F, 1F),
                            AxisAngle4f(1F, 0F, 0F, 0F)
                        )
                    }
                }
            }, Main.instance
        )
    }
}