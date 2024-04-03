package kr.vanilage.main

import org.bukkit.entity.Player

interface Weapon {
    val coolTick : Int
    fun setup()
    fun skill(player : Player)
}