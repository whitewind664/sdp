package com.github.gogetters.letsgo.matchmaking

import com.github.gogetters.letsgo.database.Database
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlin.math.pow

class Matchmaking {
    companion object {
        // TODO if time is available, use glicko2 instead of elo
        // https://lichess.org/page/rating-systems
        // http://www.glicko.net/glicko/glicko2.pdf

        private val eloChangeConstant = 30

        private fun eloWinProbability(rating1: Double, rating2: Double): Double {
            return 1.0 / (1.0 + 10.0.pow((rating1 - rating2) / 400.0))
        }

        /**
         * Calculates the elo changes after the conclusion of a game
         * @param rating1 elo rating of player1
         * @param rating2 elo rating of player2
         * @param result 1 if player1 wins, 0 if player2 wins
         * @return a pair representing the corresponding elo changes
         */
        fun eloChange(rating1: Double, rating2: Double, result: Int): Pair<Double, Double> {

            val probability2 = eloWinProbability(rating1, rating2)
            val probability1 = eloWinProbability(rating2, rating1)

            val newRating1 = rating1 + eloChangeConstant * (result - probability1)
            val newRating2 = rating2 + eloChangeConstant * ((1 - result) - probability2)


            return Pair(newRating1, newRating2)
        }

        fun findMatch(onMatchFound: (String) -> Unit, playerRating: Int) {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                val onDataChange: (DataSnapshot) -> Unit = { dataSnapshot: DataSnapshot ->
                    val gameId = dataSnapshot.value
                    if (gameId is String) {
                        onMatchFound(gameId)
                    }
                }
                val onCancelled = { databaseError: DatabaseError ->
                    TODO("no idea what this should do")
                }

                Database.addEventListener("/matchmaking/currentGamesPerUser/${user.uid}", onDataChange, onCancelled)

                Database.findMatch(user.uid, playerRating) { _, _, _ ->
                    TODO()
                }
            }
        }
    }

}