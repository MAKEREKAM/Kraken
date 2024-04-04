package kr.vanilage.main.weapons

import kr.vanilage.main.Main
import kr.vanilage.main.Weapon
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.*
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Transformation
import org.joml.AxisAngle4f
import org.joml.Vector3f

class `10`(override val coolTick : Int) : Weapon{
    override fun setup() {

    }

    override fun skill(player : Player) {
        val bullet = player.world.spawn(player.location, Turtle::class.java)
        bullet.setBaby()
        bullet.isVisibleByDefault = false
        bullet.isInvulnerable = true
        bullet.isSilent = true
        bullet.setGravity(false)

        val display = bullet.world.spawn(bullet.location, ItemDisplay::class.java)
        display.itemStack = ItemStack(Material.CONDUIT)
        display.teleportDuration = 5
        display.transformation = Transformation(
            Vector3f(0F, 0F, 0F),
            AxisAngle4f(0F, 0F, 0F, 0F),
            Vector3f(2F, 2F, 2F),
            AxisAngle4f(0F, 0F, 0F, 0F)
        )

        val entities = player.getNearbyEntities(50.0, 50.0, 50.0)

        val filteredEntity = entities.filter { it.uniqueId != player.uniqueId && it.uniqueId != bullet.uniqueId && it is LivingEntity } as ArrayList<Entity>
        filteredEntity.sortWith(compareBy { it.location.distanceSquared(player.location) })

        val attack = object : Runnable {
            override fun run() {
                if (filteredEntity.isNotEmpty() && !bullet.isDead) {
                    val attackEntity = filteredEntity[0]

                    val attackEntityRunnable = this

                    val listSize = filteredEntity.size

                    Bukkit.getScheduler().runTaskLater(Main.instance, Runnable {
                        if (listSize == filteredEntity.size) {
                            bullet.remove()
                        }
                    }, 100)

                    val setVelocity = object : Runnable {
                        override fun run() {
                            bullet.velocity = attackEntity.location.toVector()
                                .subtract(bullet.location.toVector()).normalize().multiply(3)

                            if (bullet.location.distance(attackEntity.location) >= 5) {
                                Bukkit.getScheduler().runTaskLater(Main.instance, this, 5)
                            }

                            else {
                                (attackEntity as LivingEntity).damage(3.0)
                                filteredEntity.remove(attackEntity)

                                Bukkit.getScheduler().runTaskLater(Main.instance, attackEntityRunnable, 5)
                            }
                        }
                    }

                    Bukkit.getScheduler().runTask(Main.instance, setVelocity)
                }

                else {
                    bullet.remove()
                }
            }
        }

        val teleportDisplay = object : Runnable {
            override fun run() {
                if (!bullet.isDead) {
                    display.teleport(bullet.location)

                    bullet.world.spawnParticle(
                        Particle.CRIT,
                        display.location,
                        10, 0.2, 0.2, 0.2, 0.0, null
                    )

                    bullet.world.spawnParticle(
                        Particle.FLAME,
                        display.location,
                        10, 0.2, 0.2, 0.2, 0.0, null
                    )

                    bullet.world.spawnParticle(
                        Particle.ELECTRIC_SPARK,
                        display.location,
                        10, 0.2, 0.2, 0.2, 0.0, null
                    )

                    Bukkit.getScheduler().runTaskLater(Main.instance, this, 1)
                }

                else {
                    display.remove()
                }
            }
        }

        Bukkit.getScheduler().runTask(Main.instance, attack)
        Bukkit.getScheduler().runTask(Main.instance, teleportDisplay)
    }
}