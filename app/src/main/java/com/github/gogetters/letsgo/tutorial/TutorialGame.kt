package com.github.gogetters.letsgo.tutorial

import com.github.gogetters.letsgo.game.*
import com.github.gogetters.letsgo.game.Game

/**
 * A game that is used in the tutorial and that helps the given local player to learn to play Go
 */
internal class TutorialGame(localPlayer: TutorialLocalPlayer): Game(Board.Size.SMALL, 0.0, localPlayer, TutorialPlayer(Stone.BLACK)) {
    // TODO what means internal
    // TODO what is komi

}