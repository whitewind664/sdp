package com.github.gogetters.letsgo.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.gogetters.letsgo.R

class TutorialActivity : AppCompatActivity() {
    private var tutorialProgressIndex: Int = 0
    private var tutorialTextIds = arrayOf(R.string.tutorial_intro, R.string.tutorial_board, R.string.tutorial_stones, R.string.tutorial_capturing, R.string.tutorial_selfCapture, R.string.tutorial_score1, R.string.tutorial_KoRule, R.string.tutorial_end, R.string.tutorial_score2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        findViewById<Button>(R.id.tutorial_button_next).setOnClickListener { _ ->
            val textView = findViewById<TextView>(R.id.tutorial_textView_explanation)
            if (tutorialProgressIndex + 1 < tutorialTextIds.size) {
                tutorialProgressIndex += 1
                textView.text = resources.getString(tutorialTextIds[tutorialProgressIndex])
            } else {
                // handle end of tutorial
                textView.text = resources.getString(R.string.tutorial_outro)
                val buttonView = findViewById<Button>(R.id.tutorial_button_next)
                buttonView.text = "Back to main menu"
            }
        }
    }
}