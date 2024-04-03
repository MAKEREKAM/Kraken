package kr.vanilage.main.weapons

import kr.vanilage.main.Main
import kr.vanilage.main.Weapon
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Turtle
import java.util.UUID

class `1`(override val coolTick : Int) : Weapon{
    override fun setup() {

    }

    override fun skill(player : Player) {
        val bullet = player.world.spawn(player.location, Turtle::class.java)
        bullet.setBaby()
        bullet.isVisibleByDefault = true
        bullet.isInvulnerable = true

        val entities = player.getNearbyEntities(50.0, 50.0, 50.0)

        val filteredEntity = entities.filter { it.uniqueId != player.uniqueId && it is LivingEntity } as ArrayList<Entity>

        val attack = object : Runnable {
            override fun run() {
                if (!bullet.isDead && filteredEntity.isNotEmpty()) {
                    val attackEntity = filteredEntity[0]

                    val attackEntityRunnable = this

                    val setVelocity = object : Runnable {
                        override fun run() {
                            bullet.velocity =
                                attackEntity.location.toVector()
                                    .subtract(bullet.location.toVector()).normalize().multiply(3)

                            if (bullet.location.distance(attackEntity.location) >= 3) {
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

        Bukkit.getScheduler().runTask(Main.instance, attack)
    }
}