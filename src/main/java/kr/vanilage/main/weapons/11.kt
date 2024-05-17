package kr.vanilage.main.weapons

import kr.vanilage.main.Main
import kr.vanilage.main.Weapon
import org.bukkit.*
import org.bukkit.Particle.DustOptions
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
        bullet.velocity = player.location.direction.normalize().multiply(20)

        Bukkit.getPluginManager().registerEvents(
            object : Listener {
                @EventHandler
                fun onProjectileHit(e : ProjectileHitEvent) {
                    if (e.hitBlock == null) return

                    if (e.entity.uniqueId == bullet.uniqueId) {
                        val location = e.hitBlock!!.location
                        val distance = location.distance(player.location)
                        if (distance > 50) return

                        val snowball = player.launchProjectile(Snowball::class.java)
                        snowball.isVisibleByDefault = false
                        snowball.setGravity(false)
                        snowball.velocity = player.location.direction.normalize().multiply(distance / 10)

                        val display = snowball.world.spawn(player.location, ItemDisplay::class.java)
                        val itemStack = ItemStack(Material.NETHERITE_AXE)
                        val meta = itemStack.itemMeta
                        meta.setCustomModelData(1101)
                        itemStack.itemMeta = meta
                        display.itemStack = itemStack
                        display.teleportDuration = 1
                        display.transformation = Transformation(
                            Vector3f(0F, 0F, 0F),
                            AxisAngle4f(1.5707963F, 0F, 1F, 0F),
                            Vector3f(1F, 1F, 1F),
                            AxisAngle4f(1F, 0F, 0F, 0F)
                        )

                        var rotation = 0F
                        val playerYaw = player.yaw

                        val teleportDisplay = object : Runnable {
                            override fun run() {
                                if (!snowball.isDead) {
                                    snowball.velocity = snowball.velocity.normalize()
                                    display.teleport(snowball.location.apply {
                                        pitch = rotation
                                        yaw = playerYaw
                                    })

                                    rotation += 70F

                                    snowball.world.spawnParticle(
                                        Particle.FLAME,
                                        snowball.location,
                                        2, 0.1, 0.1, 0.1, 0.0
                                    )

                                    snowball.world.spawnParticle(
                                        Particle.SOUL_FIRE_FLAME,
                                        snowball.location,
                                        2, 0.1, 0.1, 0.1, 0.0
                                    )

                                    snowball.velocity = player.location.direction.normalize().multiply(distance / 10)

                                    Bukkit.getScheduler().runTaskLater(Main.instance, this, 1)
                                }
                                else {
                                    display.remove()
                                }
                            }
                        }

                        Bukkit.getScheduler().runTask(Main.instance, teleportDisplay)

                        Bukkit.getScheduler().runTaskLater(Main.instance, object : Runnable {
                            override fun run() {
                                snowball.remove()

                                location.getNearbyEntities(5.0, 5.0, 5.0).forEach {
                                    if (it is LivingEntity) {
                                        (it as LivingEntity).damage(3.0)
                                    }
                                }
                            }
                        }, 100)
                    }
                }
            }, Main.instance
        )
    }
}