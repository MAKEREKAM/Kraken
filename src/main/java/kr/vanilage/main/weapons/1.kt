package kr.vanilage.main.weapons

import kr.vanilage.main.Weapon
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

        val noAttackEntities = ArrayList<UUID>()
        val entities = bullet.getNearbyEntities(10.0, 10.0, 10.0)

        noAttackEntities.add(player.uniqueId)

        val attack = object : Runnable {
            override fun run() {
                if (!bullet.isDead) {



                }
            }
        }
    }
}