package kr.vanilage.main.weapons

import kr.vanilage.main.Weapon
import org.bukkit.entity.*

class `11`(override val coolTick : Int) : Weapon{
    override fun setup() {

    }

    override fun skill(player : Player) {
        val bullet = player.world.spawn(player.location, Snowball::class.java)
        bullet.addScoreboardTag("11Bullet")
        bullet.isVisibleByDefault = false

    }
}