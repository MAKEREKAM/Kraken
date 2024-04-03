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
        bullet.setAI(false)
//        bullet.isVisibleByDefault = false
//        bullet.isInvulnerable = true

        val entities = bullet.getNearbyEntities(10.0, 10.0, 10.0)

        val filteredEntity = entities.filter { it.uniqueId != player.uniqueId && it is LivingEntity } as ArrayList<Entity>

        val attack = object : Runnable {
            override fun run() {
                if (!bullet.isDead && filteredEntity.isNotEmpty()) {
                    val attackEntity = filteredEntity[0]
                    filteredEntity.remove(attackEntity)

                    val attackEntityRunnable = this

                    val setVelocity = object : Runnable {
                        override fun run() {
                            bullet.velocity =
                                attackEntity.location.toVector()
                                    .subtract(bullet.location.toVector()).normalize().multiply(3)

                            if (bullet.location.distance(attackEntity.location) >= 3) {
                                Bukkit.getScheduler().runTaskLater(Main.instance, this, 1)

                                (attackEntity as LivingEntity).damage(3.0)

                                Bukkit.getScheduler().runTaskLater(Main.instance, attackEntityRunnable, 1)
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