package com.github.gogetters.letsgo.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.gogetters.letsgo.R

class TutorialActivity : AppCompatActivity() {
    private var tutorialProgress: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        findViewById<Button>(R.id.tutorial_button_next).setOnClickListener { view ->
            val textView = findViewById<TextView>(R.id.tutorial_textView_explanation)
            textView.text = "New text here"
        }
    }
}