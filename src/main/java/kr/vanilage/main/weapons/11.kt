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
        val bullet = player.launchProjectile(Snowball::class.java)
        bullet.addScoreboardTag("11Bullet")
        bullet.isVisibleByDefault = false
        bullet.setGravity(false)
        bullet.velocity = player.location.direction.normalize().multiply(20)

        Bukkit.getPluginManager().registerEvents(
            object : Listener {
                @EventHandler
                fun onProjectileHit(e : ProjectileHitEvent) {
                    if (e.entity.uniqueId == bullet.uniqueId) {
                        val location =
                        if (e.hitBlock != null) {
                            e.hitBlock!!.location
                        } else if (e.hitEntity != null) {
                            e.hitEntity!!.location
                        } else {
                            player.location
                        }
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
                        val playerDirection = player.location.direction

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

                                    snowball.velocity = playerDirection.normalize().multiply(distance / 10)

                                    Bukkit.getScheduler().runTaskLater(Main.instance, this, 1)
                                }
                                else {
                                    display.remove()
                                }
                            }
                        }

                        Bukkit.getScheduler().runTask(Main.instance, teleportDisplay)

                        player.world.playSound(
                            player.location,
                            "kraken:11start",
                            1F, 1F
                        )

                        Bukkit.getScheduler().runTaskLater(Main.instance, object : Runnable {
                            override fun run() {
                                val snowballLocation = snowball.location
                                snowball.remove()

                                location.getNearbyEntities(5.0, 5.0, 5.0).forEach {
                                    if (it is LivingEntity) {
                                        (it as LivingEntity).damage(3.0)
                                    }
                                }

                                player.world.playSound(
                                    location,
                                    "kraken:11attack",
                                    1F, 1F
                                )

                                player.world.playSound(
                                    location,
                                    Sound.ENTITY_GENERIC_EXPLODE,
                                    1F, 0.7F
                                )

                                player.world.playSound(
                                    location,
                                    Sound.ENTITY_WARDEN_SONIC_BOOM,
                                    1F, 2F
                                )

                                player.playSound(
                                    player.location,
                                    "kraken:11attack",
                                    1F, 1F
                                )

                                player.playSound(
                                    player.location,
                                    Sound.ENTITY_GENERIC_EXPLODE,
                                    1F, 0.7F
                                )

                                player.playSound(
                                    player.location,
                                    Sound.ENTITY_WARDEN_SONIC_BOOM,
                                    1F, 2F
                                )

                                player.world.spawnParticle(
                                    Particle.EXPLOSION_LARGE,
                                    snowballLocation,
                                    15, 1.5, 1.5, 1.5, 0.0, null
                                )

                                player.world.spawnParticle(
                                    Particle.SONIC_BOOM,
                                    snowballLocation,
                                    15, 1.5, 1.5, 1.5, 0.0, null
                                )

                                player.world.spawnParticle(
                                    Particle.FLAME,
                                    snowballLocation,
                                    150, 0.0, 0.0, 0.0, 1.0, null
                                )

                                val firework = player.world.spawn(snowballLocation, Firework::class.java)
                                firework.isSilent = true
                                val fireworkMeta = firework.fireworkMeta

                                val builder = FireworkEffect.builder()
                                builder.withColor(Color.RED)
                                builder.with(FireworkEffect.Type.BALL_LARGE)

                                fireworkMeta.addEffect(builder.build())
                                fireworkMeta.power = 0

                                firework.fireworkMeta = fireworkMeta

                                firework.detonate()

                                player.world.spawnParticle(
                                    Particle.SOUL_FIRE_FLAME,
                                    snowballLocation,
                                    150, 0.0, 0.0, 0.0, 1.0, null
                                )

                                Bukkit.getScheduler().runTaskLater(Main.instance, Runnable {
                                    player.world.playSound(
                                        location,
                                        "kraken:11attack",
                                        0.5F, 1F
                                    )

                                    player.world.playSound(
                                        location,
                                        Sound.BLOCK_PISTON_CONTRACT,
                                        2F, 1F
                                    )
                                }, 4)
                            }
                        }, 11)
                    }
                }
            }, Main.instance
        )
    }
}