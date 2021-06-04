package com.github.gogetters.letsgo.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.github.gogetters.letsgo.R
import com.github.gogetters.letsgo.game.Stone
import com.github.gogetters.letsgo.matchmaking.Matchmaking

class WaitMatchActivity: BaseActivity() {

    companion object {
        const val EXTRA_IS_RANKED = "com.github.gogetters.letsgo.game.GAME_SIZE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isRanked = intent.getBooleanExtra(EXTRA_IS_RANKED, false)

        val cancelButton: Button = findViewById(R.id.matchmaking_cancel_button)

        cancelButton.setOnClickListener({
            Matchmaking.cancelFindMatch()
            finish()
        })

        Matchmaking.findMatch(isRanked, { gameId, color -> goToGame(gameId, color) })


    }

    override fun onDestroy() {
        super.onDestroy()
        Matchmaking.cancelFindMatch()
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_wait_match
    }

    private fun goToGame(gameId: String, color: Stone) {
        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra(GameActivity.EXTRA_GAME_SIZE, 9)
            putExtra(GameActivity.EXTRA_KOMI, 5.5)
            putExtra(GameActivity.EXTRA_LOCAL_COLOR, color.toString())
            putExtra(GameActivity.EXTRA_GAME_TYPE, "FIREBASE")
            putExtra(GameActivity.EXTRA_GAME_ID, gameId)
        }
        startActivity(intent)
    }
}