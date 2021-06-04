package com.github.gogetters.letsgo.matchmaking

import android.content.Context
import com.github.gogetters.letsgo.database.Authentication
import com.github.gogetters.letsgo.database.Database
import com.github.gogetters.letsgo.database.types.GameData
import com.github.gogetters.letsgo.game.Stone
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlin.math.floor
import kotlin.math.pow

class Matchmaking {
    companion object {
        // https://lichess.org/page/rating-systems

        private val eloChangeConstant = 16
        private val rankedBucketSize = 30
        private val unrankedBucketSize = 100
        var isSearching = false;
        var queuePath: String? = null;
        private val listenerPath: String
        private var listener: ValueEventListener? = null
        private var onFind: (String, Stone) -> Unit = { _, _ -> };


        init {
            listenerPath = "/matchmaking/currentGamesPerUser/${Authentication.getUid()}"

            val onDataChange: (DataSnapshot) -> Unit = { dataSnapshot: DataSnapshot ->
                val gameId = dataSnapshot.value
                if (gameId is String) {
                    isSearching = false
                    queuePath = null
                    onMatchFound(gameId)
                }
            }
            val onCancelled = { _: DatabaseError -> }

            listener = Database.addEventListener(listenerPath, onDataChange, onCancelled)
        }

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

        fun onMatchFound(gameId: String) {
            Database.readData("/matchmaking/games/$gameId")
                    .addOnSuccessListener {
                        val game = it.getValue(GameData::class.java)!!

                        val userId = Authentication.getUid()!!
                        val color: Stone;

                        if (game.player1 == userId) {
                            color = Stone.WHITE;
                        } else {
                            color = Stone.BLACK;
                        }

                        onFind(gameId, color)
                    }
        }

        fun findMatch(ranked: Boolean, onFind: (String, Stone) -> Unit, onFail: () -> Unit) {
            val user = Authentication.getCurrentUser()
            this.onFind = onFind
            if (user != null) {
                val ratingPath = "/matchmaking/users/${user.uid}/rating"

                Database.readData(ratingPath)
                        .addOnSuccessListener {
                            var rating = it.getValue(Double::class.java)
                            if (rating == null) {
                                rating = 1500.0
                                Database.writeData(ratingPath, rating)
                            }

                            val bucketSize = if (ranked) rankedBucketSize else unrankedBucketSize
                            val bucket = (rating / bucketSize).toInt() * bucketSize


                            Database.findMatch(user.uid, bucket, ranked) { _, committed, _ ->
                                if (!committed) {
                                    onFail()
                                }
                            }
                        }
                        .addOnFailureListener {
                            onFail()
                        }
            }
        }

        fun cancelFindMatch() {
            if (isSearching && queuePath != null) {
                Database.deleteData(queuePath!!)
                queuePath = null
                isSearching = false
            }
        }

        fun endMatch(): Task<Void> {
            return Database.writeData(listenerPath, null)
        }

        fun updateLossRatings(uid: String, otherUid: String) {
            val myRatingPath = "/matchmaking/users/$uid/rating"
            val otherRatingPath = "/matchmaking/users/$otherUid/rating"
            Database.readData(myRatingPath)
                    .addOnSuccessListener {
                        val rating1 = it.getValue(Double::class.java)
                        Database.readData(otherRatingPath)
                                .addOnSuccessListener {
                                    val rating2 = it.getValue(Double::class.java)
                                    val (newRating1, newRating2) = eloChange(rating1!!, rating2!!, 0)
                                    Database.writeData(myRatingPath, newRating1)
                                    Database.writeData(otherRatingPath, newRating2)
                                }
                    }
        }

        fun surrender() {
            val uid = Authentication.getUid()!!;
            Database.readData(listenerPath)
                    .addOnSuccessListener {
                        val gameId = it.getValue(String::class.java)
                        if (gameId != null) {
                            Database.readData("/matchmaking/games/$gameId")
                                    .addOnSuccessListener {
                                        val game = it.getValue(GameData::class.java)!!
                                        if (game.ranked) {
                                            val otherUid = game.otherPlayer(uid)
                                            updateLossRatings(uid, otherUid)
                                        }
                                    }
                        }
                    }
        }
    }

}