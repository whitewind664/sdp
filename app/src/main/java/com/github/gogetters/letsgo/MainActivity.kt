package com.github.gogetters.letsgo

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get a reference to the UI components
        val mGoButton = findViewById<Button>(R.id.profileButton)

        // Set the behaviour of the button
        mGoButton.setOnClickListener { clicked: View? ->
            getProfile()
        }

    }

    /**
     * Opens a new [GreetingActivity] saying hello to the specified `userName`
     *
     * @param userName the user to greet
     */
    protected fun getProfile() {
        val intent = Intent(this, Profile::class.java)
        startActivity(intent)
    }
}