package com.github.gogetters.letsgo.game.util.firebase

import com.github.gogetters.letsgo.database.Authentication
import com.github.gogetters.letsgo.database.Database
import com.github.gogetters.letsgo.database.types.GameData
import com.github.gogetters.letsgo.game.Move
import com.github.gogetters.letsgo.game.Point
import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.game.util.InputDelegate
import com.github.gogetters.letsgo.game.util.RemoteService

class FirebaseService(val gameId: String, val color: Stone): RemoteService {

    override lateinit var inputDelegate: InputDelegate

    fun setDelegate(delegate: InputDelegate) {
        inputDelegate = delegate
        val otherMovePath = "/matchmaking/newMoves/$gameId/${color.otherColor()}"
        Database.addEventListener(otherMovePath, { dataSnapshot ->
            val point = dataSnapshot.getValue(String::class.java)
            if (point != null) {
                inputDelegate.saveLatestInput(Point.fromString(point))
                Database.writeData(otherMovePath, null)
            }
        }, { databaseError ->
            TODO("display `check internet connection`")
        })
    }

    override fun notify(move: Move) {
        val myMovePath =  "/matchmaking/newMoves/$gameId/${color}"
        Database.writeData(myMovePath, move.point)
    }
}