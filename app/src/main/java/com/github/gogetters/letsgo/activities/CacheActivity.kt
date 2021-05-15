package com.github.gogetters.letsgo.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.gogetters.letsgo.database.Database

class CacheActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set chaching
        Database.setCache()

    }
}