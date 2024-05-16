package kr.vanilage.main.weapons

import kr.vanilage.main.Main
import kr.vanilage.main.Weapon
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.entity.*
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class `10`(override val coolTick : Int) : Weapon{
    override fun setup() {

    }

    override fun skill(player : Player) {
        val bullet = player.world.spawn(player.location, Bee::class.java)
        bullet.addScoreboardTag("10Bullet")
        bullet.isVisibleByDefault = false
        bullet.isInvulnerable = true
        bullet.isSilent = true
        bullet.addPotionEffect(
            PotionEffect(
                PotionEffectType.SLOW,
                PotionEffect.INFINITE_DURATION,
                255,
                false,
                false
            )
        )
        bullet.isSilent = true

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
                                .subtract(bullet.location.toVector()).normalize().multiply(3.5)

                            if (bullet.location.distance(attackEntity.location) >= 7) {
                                Bukkit.getScheduler().runTaskLater(Main.instance, this, 5)
                            }

                            else {
                                (attackEntity as LivingEntity).damage(3.0)
                                filteredEntity.remove(attackEntity)

                                attackEntity.world.playSound(
                                    attackEntity.location,
                                    "kraken:10attack",
                                    3F, 1F
                                )

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

        val spawnParticle = object : Runnable {
            override fun run() {
                if (!bullet.isDead) {
                    bullet.world.spawnParticle(
                        Particle.CRIT,
                        bullet.location,
                        10, 0.2, 0.2, 0.2, 0.0, null, true
                    )

                    bullet.world.spawnParticle(
                        Particle.FLAME,
                        bullet.location,
                        10, 0.2, 0.2, 0.2, 0.0, null, true
                    )

                    bullet.world.spawnParticle(
                        Particle.ELECTRIC_SPARK,
                        bullet.location,
                        10, 0.2, 0.2, 0.2, 0.0, null, true
                    )

                    Bukkit.getScheduler().runTaskLater(Main.instance, this, 1)
                }
            }
        }

        Bukkit.getScheduler().runTask(Main.instance, attack)
        Bukkit.getScheduler().runTask(Main.instance, spawnParticle)
    }
}