package com.github.gogetters.letsgo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Profile : FirebaseUIActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }
}