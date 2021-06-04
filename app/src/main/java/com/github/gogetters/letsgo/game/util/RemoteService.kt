package com.github.gogetters.letsgo.game.util

import com.github.gogetters.letsgo.game.Move

interface RemoteService {

    var inputDelegate: InputDelegate

    fun notify(move: Move)
}