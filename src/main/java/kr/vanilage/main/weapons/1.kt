package kr.vanilage.main.weapons

import kr.vanilage.main.Weapon
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
        bullet.isVisibleByDefault = false
        bullet.isInvulnerable = true

        val entities = bullet.getNearbyEntities(10.0, 10.0, 10.0)

        val filteredEntity = entities.filter { it.uniqueId != player.uniqueId && it is LivingEntity } as ArrayList<LivingEntity>

        val attack = object : Runnable {
            override fun run() {
                if (!bullet.isDead) {
                    while (entities)
                }
            }
        }
    }
}